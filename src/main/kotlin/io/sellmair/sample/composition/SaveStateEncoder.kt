package io.sellmair.sample.composition

import io.sellmair.sample.GameEntity
import io.sellmair.sample.GameState
import io.sellmair.sample.SaveStateHeader
import java.io.ByteArrayOutputStream

class SaveStateEncoder(
    private val saveStateHeaderFactory: SaveStateHeaderFactory,
    private val gameEntityEncoder: GameEntityEncoder
) : GameStateSaver.Encoder {

    interface SaveStateHeaderFactory {
        fun createSaveStateHeader(): SaveStateHeader
    }

    interface GameEntityEncoder {
        fun encodeGameEntity(gameEntity: GameEntity): ByteArray
        companion object
    }

    override fun encodeSaveState(gameState: GameState): ByteArray {
        return ByteArrayOutputStream().also { out ->
            val header = saveStateHeaderFactory.createSaveStateHeader()
            out.write(gameEntityEncoder.encodeGameEntity(header))
            out.write(gameEntityEncoder.encodeGameEntity(gameState.levelState))
            out.write(gameEntityEncoder.encodeGameEntity(gameState.playerState))
        }.toByteArray()
    }
}
