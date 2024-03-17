package io.sellmair.sample.inheritance

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.utils.encodeAsJavaObject
import java.io.File

/**
 * Base class for java.io.Serializable based save state files.
 */
abstract class AbstractJavaSerializableGameStateSaverFile(file: File) : AbstractGameStateSaverFile(file) {
    override fun encodeGameEntity(gameEntity: GameEntity): ByteArray {
        return gameEntity.encodeAsJavaObject()
    }
}