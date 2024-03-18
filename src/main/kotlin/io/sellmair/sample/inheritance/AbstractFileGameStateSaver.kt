package io.sellmair.sample.inheritance

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.models.GameState
import io.sellmair.sample.models.SaveStateHeader
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.ByteBuffer

/**
 * Base Class for 'File based' Save States:
 * Will encode the [GameEntity] parts of the GameState and write it to the provided [file]
 */
abstract class AbstractFileGameStateSaver(private val file: File) : AbstractGameStateSaver() {

    /**
     * Easy: We know how to store the bytes in a file!
     */
    override fun storeGameState(byteArray: ByteArray) {
        file.writeBytes(byteArray)
    }

    /*
    This abstraction relies on somebody else providing the exact implementation
    of encoding the individual game entities
    */
    abstract fun encodeGameEntity(gameEntity: GameEntity): ByteArray

    /*
    This abstraction relies on somebody else being able to create the "SaveFileHeader"
     */
    abstract fun createSaveStateHeader(): SaveStateHeader

    open fun writeGameEntityHeader(
        stream: ByteArrayOutputStream, gameEntity: GameEntity, encoded: ByteArray
    ) {
        stream.write(ByteBuffer.allocate(4).also { buffer -> buffer.putInt(encoded.size) }.array())
    }

    open fun writeGameEntity(stream: ByteArrayOutputStream, gameEntity: GameEntity, encoded: ByteArray) {
        writeGameEntityHeader(stream, gameEntity, encoded)
        stream.write(encoded)
    }

    override fun encodeGameState(gameState: GameState): ByteArray {
        return ByteArrayOutputStream().also { out ->
            val header = createSaveStateHeader()
            writeGameEntity(out, header, encodeGameEntity(header))
            writeGameEntity(out, gameState.levelState, encodeGameEntity(gameState.levelState))
            writeGameEntity(out, gameState.playerState, encodeGameEntity(gameState.playerState))
        }.toByteArray()
    }
}