package io.sellmair.sample.composition

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.utils.encodeAsJavaObject
import io.sellmair.sample.utils.writeInt
import java.io.ByteArrayOutputStream

val GameStateEncoder.GameEntityEncoder.Companion.javaSerialization: GameStateEncoder.GameEntityEncoder
    get() = JavaSerializationGameEntityEncoder

private object JavaSerializationGameEntityEncoder : GameStateEncoder.GameEntityEncoder {
    override fun encodeGameEntity(gameEntity: GameEntity): ByteArray {
        val javaObjectAsBytes = gameEntity.encodeAsJavaObject()
        return ByteArrayOutputStream().also { out ->
            out.writeInt(javaObjectAsBytes.size)
            out.write(javaObjectAsBytes)
        }.toByteArray()
    }
}