package com.bc.core.data.archive.repostiory

import android.content.Context
import android.graphics.Bitmap
import androidx.core.graphics.drawable.toBitmap
import coil3.asDrawable
import coil3.imageLoader
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.SuccessResult
import com.bc.core.data.archive.db.ArchiveDao
import com.bc.core.data.archive.db.toCollectionEntity
import com.bc.core.data.archive.source.TrackDownloadDataSource
import com.bc.core.domain.model.PhotoItemModel
import com.bc.env.network.request.LoadParams
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArchiveRepository @Inject constructor(
    private val archiveDao: ArchiveDao,
    private val trackDownloadDataSource: TrackDownloadDataSource,
    @ApplicationContext private val context: Context
) {
    val collectionIdSet: Flow<Set<String>> = archiveDao.getCollectionIds()
        .map { it.toSet() }
        .distinctUntilChanged()

    suspend fun insert(photo: PhotoItemModel) {
        val localImagePath = downloadAndSaveImage(photo)
        archiveDao.insert(photo.toCollectionEntity(imagePath = localImagePath ?: photo.imageUrl))
    }

    suspend fun delete(photo: PhotoItemModel) {
        val collection = archiveDao.getById(photo.id) ?: return
        File(collection.imagePath).takeIf { it.exists() }?.delete()
        archiveDao.delete(collection)
    }

    suspend fun trackDownload(trackDownloadUrl: String?) {
        if (trackDownloadUrl.isNullOrBlank()) return
        trackDownloadDataSource.load(
            LoadParams().put(
                TrackDownloadDataSource.KEY_DOWNLOAD_LOCATION,
                trackDownloadUrl
            )
        )
    }

    private suspend fun downloadAndSaveImage(photo: PhotoItemModel): String? {
        val cachedImagePath = loadFromCacheAndSave(photo)
        if (cachedImagePath != null) return cachedImagePath

        return runCatching {
            val maxBytesBudget = (Runtime.getRuntime().maxMemory() / 8)
                .coerceAtLeast(8L * 1024L * 1024L)
            val sampleSize = calculatePowerOfTwoSampleSize(
                width = photo.width,
                height = photo.height,
                maxBytes = maxBytesBudget
            )
            val targetWidth = (photo.width / sampleSize).coerceAtLeast(1)
            val targetHeight = (photo.height / sampleSize).coerceAtLeast(1)

            val request = ImageRequest.Builder(context)
                .data(photo.imageUrl)
                .size(targetWidth, targetHeight)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.ENABLED)
                .build()
            val result = context.imageLoader.execute(request) as? SuccessResult ?: return null
            val bitmap = result.image.asDrawable(context.resources).toBitmap()
            saveBitmap(photo.id, bitmap)
        }.getOrNull()
    }

    private suspend fun loadFromCacheAndSave(photo: PhotoItemModel): String? {
        return runCatching {
            val request = ImageRequest.Builder(context)
                .data(photo.imageUrl)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .diskCachePolicy(CachePolicy.ENABLED)
                .networkCachePolicy(CachePolicy.DISABLED)
                .build()
            val result = context.imageLoader.execute(request) as? SuccessResult ?: return null
            val bitmap = result.image.asDrawable(context.resources).toBitmap()
            saveBitmap(photo.id, bitmap)
        }.getOrNull()
    }

    private fun saveBitmap(photoId: String, bitmap: Bitmap): String {
        val outputFile = File(context.filesDir, "$photoId.png")
        outputFile.outputStream().use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        }
        return outputFile.absolutePath
    }

    private fun calculatePowerOfTwoSampleSize(
        width: Int,
        height: Int,
        bytesPerPixel: Int = 4,
        maxBytes: Long
    ): Int {
        val originalBytes = width.toLong() * height.toLong() * bytesPerPixel.toLong()
        var sampleSize = 1
        while (originalBytes / (sampleSize.toLong() * sampleSize.toLong()) > maxBytes) {
            sampleSize *= 2
        }
        return sampleSize.coerceAtLeast(1)
    }
}
