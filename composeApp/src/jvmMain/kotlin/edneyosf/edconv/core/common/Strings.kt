package edneyosf.edconv.core.common

abstract class Strings(protected open val language: String) {

    protected abstract val texts: Map<String, Map<Int, String>>
    protected abstract val pt: Map<Int, String>
    protected abstract val en: Map<Int, String>

    operator fun get(index: Enum<*>): String = texts[language]?.get(index.ordinal) ?: "TEXT NOT FOUND"

    protected infix fun <E : Enum<E>, V> E.to(value: V): Pair<Int, V> = this.ordinal to value
}