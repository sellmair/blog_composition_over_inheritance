package io.sellmair.sample.composition

import io.sellmair.sample.models.GameEntity
import io.sellmair.sample.utils.encodeAsJson
import io.sellmair.sample.utils.writeInt
import io.sellmair.sample.utils.writeString
import java.io.ByteArrayOutputStream


val GameStateEncoder.GameEntityEncoder.Companion.json: GameStateEncoder.GameEntityEncoder
    get() = JsonGameEntityEncoder

private object JsonGameEntityEncoder : GameStateEncoder.GameEntityEncoder {
    override fun encodeGameEntity(gameEntity: GameEntity): ByteArray {
        val jsonAsBytes = gameEntity.encodeAsJson().encodeToByteArray()

        return ByteArrayOutputStream().also { out ->
            out.writeString("json")
            out.writeInt(jsonAsBytes.size)
            out.write(jsonAsBytes)
        }.toByteArray()
    }
}