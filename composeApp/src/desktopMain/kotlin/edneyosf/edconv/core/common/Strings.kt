package edneyosf.edconv.core.common

abstract class Strings(protected open val language: String) {

    protected abstract val texts: Map<String, Map<Long, String>>
    protected abstract val pt: Map<Long, String>
    protected abstract val en: Map<Long, String>

    operator fun get(id: Long): String = texts[language]?.get(id) ?: "TEXT NOT FOUND"
}