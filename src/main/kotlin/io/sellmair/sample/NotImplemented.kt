package io.sellmair.sample

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

fun Any.encodeAsJson(): String = TODO()

fun Any.encodeAsJavaObject(): ByteArray {
    return ByteArrayOutputStream().also { byteArrayOutputStream ->
        ObjectOutputStream(byteArrayOutputStream).writeObject(this)
    }.toByteArray()
}