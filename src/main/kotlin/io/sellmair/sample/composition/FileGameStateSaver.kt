package io.sellmair.sample.composition

import java.io.File

fun GameStateSaver.Writer.Companion.file(file: File): GameStateSaver.Writer = FileGameStateSaver(file)

private class FileGameStateSaver(
    private val file: File
) : GameStateSaver.Writer {
    override fun writeSaveState(encoded: ByteArray) {
        file.writeBytes(encoded)
    }
}