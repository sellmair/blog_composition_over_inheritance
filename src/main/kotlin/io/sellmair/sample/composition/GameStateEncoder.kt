package io.sellmair.sample.composition

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.models.GameState
import io.sellmair.sample.models.SaveStateHeader
import java.io.ByteArrayOutputStream

class GameStateEncoder(
    private val saveStateHeaderFactory: SaveStateHeaderFactory,
    private val gameEntityEncoder: GameEntityEncoder
) : GameStateSaver.Encoder {

    /**
     * The encoded Game State will include some kind of header.
     * This information will include whether or not the save was done automatically
     * and when the save happened.
     */
    interface SaveStateHeaderFactory {
        fun createSaveStateHeader(): SaveStateHeader
    }

    /**
     * The [GameState] consists out of several parts ([GameEntity]).
     * We will encode each part individually with this encoder
     */
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
