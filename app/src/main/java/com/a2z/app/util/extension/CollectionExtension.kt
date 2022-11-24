package com.a2z.app.util.extension

fun <K, V : Any> Map<K, V?>.toVarargArray() = map { it.key to it.value }.toTypedArray()