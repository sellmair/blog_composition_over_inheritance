package io.sellmair.sample.utils

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream
import java.nio.ByteBuffer

fun Any.encodeAsJson(): String = TODO()

fun Any.encodeAsJavaObject(): ByteArray {
    return ByteArrayOutputStream().also { byteArrayOutputStream ->
        ObjectOutputStream(byteArrayOutputStream).writeObject(this)
    }.toByteArray()
}

fun Int.encodeToByteArray(): ByteArray {
    return ByteBuffer.allocate(4).also { buffer -> buffer.putInt(this) }.array()
}

fun ByteArrayOutputStream.writeInt(value: Int) {
    write(value.encodeToByteArray())
}

fun ByteArrayOutputStream.writeString(value: String) {
    write(value.encodeToByteArray())
}