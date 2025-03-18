package core.common

abstract class Texts(protected open val language: String) {

    protected abstract val texts: Map<String, Map<String, String>>

    fun retrieve(key: String): String = texts[language]?.get(key) ?: "TEXT NOT FOUND"
}