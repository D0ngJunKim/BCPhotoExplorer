package com.bc.env.network.request

typealias Parameters = Map<String, @JvmSuppressWildcards Any?>

sealed interface IParams

data class LoadParams(
    private val params: Parameters = emptyMap()
) : IParams {
    fun put(key: String, value: Any?): LoadParams {
        return if (value == null) {
            copy(params = params - key)
        } else {
            copy(params = params + (key to value))
        }
    }

    fun get(key: String): Any? = params[key]

    fun toMap(): Parameters = params.toMapNotNull()
}

data class PagingLoadParams(
    val page: Int,
    val pageKey: String = "page",
    private val params: Parameters = emptyMap()
) : IParams {
    fun put(key: String, value: Any?): PagingLoadParams {
        return if (value == null) {
            copy(params = params - key)
        } else {
            copy(params = params + (key to value))
        }
    }

    fun get(key: String): Any? = params[key]

    fun increment() = copy(page = page + 1)
    fun decrement() = copy(page = page - 1)

    fun toMap(): Parameters = (params + (pageKey to page)).toMapNotNull()
}

private fun Parameters.toMapNotNull(): Parameters {
    return entries
        .asSequence()
        .mapNotNull { (key, value) ->
            val normalizedValue = value?.normalizeValue() ?: return@mapNotNull null
            key to normalizedValue
        }
        .toMap()
}

private fun Any.normalizeValue(): Any {
    return when (this) {
        is LoadParams -> toMap()
        is PagingLoadParams -> toMap()
        is Map<*, *> -> normalizeMap()
        is Iterable<*> -> mapNotNull { it?.normalizeValue() }
        is Array<*> -> mapNotNull { it?.normalizeValue() }
        else -> this
    }
}

private fun Map<*, *>.normalizeMap(): Parameters {
    return entries
        .asSequence()
        .mapNotNull { (key, value) ->
            val stringKey = key as? String ?: return@mapNotNull null
            val normalizedValue = value?.normalizeValue() ?: return@mapNotNull null
            stringKey to normalizedValue
        }
        .toMap()
}
