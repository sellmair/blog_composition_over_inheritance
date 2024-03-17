package io.sellmair.sample

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.Serializable
import java.nio.ByteBuffer

/*
Let's imagine programming a game. Let's just pretend we have some kind of
reasonable state that we would like to persist somehow
 */
sealed interface GameEntity : Serializable
class PlayerState : GameEntity
class LevelState : GameEntity

class GameState(
    val playerState: PlayerState,
    val levelState: LevelState
)

/**
 * Meta-Information for a Save State file.
 * It might be important to know when the save state was created and whether or not
 * it was saved automatically or triggered by the player.
 */
class SaveFileHeader(
    val isAutoSave: Boolean,
    val saveTime: Instant
) : GameEntity

/**
 * Base class for saving the current state:
 * See the [save] method accepting a 'GameState' that can be persistet
 */
abstract class AbstractSaveState {
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

/**
 * Base Class for 'File based' Save States:
 * Will encode the [GameEntity] parts of the GameState and write it to the provided [file]
 */
abstract class AbstractSaveStateFile(private val file: File) : AbstractSaveState() {

    /**
     * Easy: We how to store the bytes to a file!
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
    abstract fun createSaveStateHeader(): SaveFileHeader

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

/**
 * Base class for Json based save state files
 */
abstract class AbstractJsonSaveStateFile(file: File) : AbstractSaveStateFile(file) {
    override fun writeGameEntityHeader(
        stream: ByteArrayOutputStream, gameEntity: GameEntity, encoded: ByteArray
    ) {
        stream.write("json".encodeToByteArray())
        super.writeGameEntityHeader(stream, gameEntity, encoded)
    }

    override fun encodeGameEntity(gameEntity: GameEntity): ByteArray {
        return gameEntity.encodeAsJson().encodeToByteArray()
    }
}

/**
 * Base class for java.io.Serializable based save state files.
 */
abstract class AbstractJavaSerializableSaveStateFile(file: File) : AbstractSaveStateFile(file) {
    override fun encodeGameEntity(gameEntity: GameEntity): ByteArray {
        return gameEntity.encodeAsJavaObject()
    }
}

/**
 * Implementation of a save state which is
 *  - file-based
 *  - uses json as encoding
 *  - creates a header with current time and 'isAutoSave = true'
 */
class AutoSaveJsonSaveStateFile(file: File) : AbstractJsonSaveStateFile(file) {
    override fun createSaveStateHeader(): SaveFileHeader {
        return SaveFileHeader(isAutoSave = true, saveTime = Clock.System.now())
    }
}

/**
 * Implementation of a save state which is
 *  - file-based
 *  - uses java.io.Serializable as encoding
 *  - creates a header with current time and 'isAutoSave = true'
 */
class AutoSaveJavaSerializableSaveStateFile(file: File) : AbstractJavaSerializableSaveStateFile(file) {
    override fun createSaveStateHeader(): SaveFileHeader {
        return SaveFileHeader(isAutoSave = true, saveTime = Clock.System.now())
    }
}

/**
 * Implementation of a save state which is
 *  - file-based
 *  - uses json as encoding
 *  - creates a header with current time and 'isAutoSave = false'
 */
class ManualSaveJsonSaveStateFile(file: File) : AbstractJsonSaveStateFile(file) {
    override fun createSaveStateHeader(): SaveFileHeader {
        return SaveFileHeader(isAutoSave = true, saveTime = Clock.System.now())
    }
}


/**
 * Implementation of a save state which is
 *  - file-based
 *  - uses java.io.Serializable as encoding
 *  - creates a header with current time and 'isAutoSave = false'
 */
class ManualSaveJavaSerializableSaveStateFile(file: File) : AbstractJavaSerializableSaveStateFile(file) {
    override fun createSaveStateHeader(): SaveFileHeader {
        return SaveFileHeader(isAutoSave = false, saveTime = Clock.System.now())
    }
}
