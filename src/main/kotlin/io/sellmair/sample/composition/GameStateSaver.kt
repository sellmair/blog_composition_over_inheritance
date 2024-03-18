@file:Suppress("FunctionName")

package io.sellmair.sample.composition

import io.sellmair.sample.models.GameState
import io.sellmair.sample.models.SaveStateHeader
import io.sellmair.sample.composition.GameStateEncoder.GameEntityEncoder
import java.io.File

class GameStateSaver(
    private val encoder: Encoder,
    private val gameStateWriter: Writer
) {
    interface Encoder {
        fun encodeSaveState(gameState: GameState): ByteArray

        companion object
    }

    interface Writer {
        fun writeSaveState(encoded: ByteArray)

        companion object
    }

    fun save(gameState: GameState) {
        gameStateWriter.writeSaveState(
            encoder.encodeSaveState(gameState)
        )
    }
}

fun AutoSaveJsonSaveStateFile(file: File): GameStateSaver {
    return GameStateSaver(
        gameStateWriter = GameStateSaver.Writer.file(file),
        encoder = GameStateEncoder(
            gameEntityEncoder = GameEntityEncoder.javaSerialization,
            saveStateHeaderFactory = SaveStateHeader.factory(
                isAutoSave = true
            ),
        ),
    )
}

fun AutoSaveJavaSerializableSaveStateFile(file: File): GameStateSaver {
    return GameStateSaver(
        gameStateWriter = GameStateSaver.Writer.file(file),
        encoder = GameStateEncoder(
            gameEntityEncoder = GameEntityEncoder.json,
            saveStateHeaderFactory = SaveStateHeader.factory(
                isAutoSave = true
            ),
        ),
    )
}

fun ManualSaveJsonSaveStateFile(file: File): GameStateSaver {
    return GameStateSaver(
        gameStateWriter = GameStateSaver.Writer.file(file),
        encoder = GameStateEncoder(
            gameEntityEncoder = GameEntityEncoder.json,
            saveStateHeaderFactory = SaveStateHeader.factory(
                isAutoSave = false
            ),
        )
    )
}

fun ManualSaveJavaSerializableSaveStateFile(file: File): GameStateSaver {
    return GameStateSaver(
        gameStateWriter = GameStateSaver.Writer.file(file),
        encoder = GameStateEncoder(
            gameEntityEncoder = GameEntityEncoder.json,
            saveStateHeaderFactory = SaveStateHeader.factory(
                isAutoSave = false
            ),
        )
    )
}
