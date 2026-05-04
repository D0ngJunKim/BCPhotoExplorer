package com.bc.gradle

import org.gradle.api.GradleException
import org.gradle.api.Project
import java.io.File
import java.util.Properties

fun Project.getVersionNameFromFile(): String {
    val versionFile = rootProject.file("$name/version.properties")
    if (!versionFile.exists()) {
        throw GradleException("version.properties 파일이 없습니다.")
    }

    val versionName = versionFile.readProperty("VERSION_NAME")
    if (versionName.isNullOrEmpty()) {
        throw GradleException("VersionName이 존재하지 않습니다.")
    }

    return versionName
}

fun Project.getVersionCodeFromFile(): Int {
    return (getVersionNameFromFile().replace(".", "") + "01").toInt()
}

private fun File.readProperty(key: String): String? {
    if (!exists() || !isFile) return null
    return runCatching {
        inputStream().use { stream ->
            Properties().apply { load(stream) }.getProperty(key)
        }
    }.getOrNull()
}

