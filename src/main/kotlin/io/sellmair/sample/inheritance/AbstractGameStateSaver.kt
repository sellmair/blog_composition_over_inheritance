package io.sellmair.sample.inheritance

import io.sellmair.sample.models.GameState


/**
 * Base class for saving the current state:
 * See the [save] method accepting a 'GameState' that can be persistet
 */
abstract class AbstractGameStateSaver {
    open fun save(state: GameState) {
        storeGameState(encodeGameState(state))
    }

    /*
    To persist the "GameState" we need to encode the state to bytes and then store
    those bytes 'somewhere'
     */
    protected abstract fun encodeGameState(gameState: GameState): ByteArray
    protected abstract fun storeGameState(byteArray: ByteArray)
}
