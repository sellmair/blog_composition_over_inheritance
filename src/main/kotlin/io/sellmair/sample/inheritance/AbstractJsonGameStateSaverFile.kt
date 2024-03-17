package io.sellmair.sample.inheritance

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.utils.encodeAsJson
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Base class for Json based save state files
 */
abstract class AbstractJsonGameStateSaverFile(file: File) : AbstractGameStateSaverFile(file) {
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