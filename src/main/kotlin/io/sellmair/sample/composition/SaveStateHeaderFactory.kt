package io.sellmair.sample.composition

import io.sellmair.sample.models.SaveStateHeader
import kotlinx.datetime.Clock

fun SaveStateHeader.Companion.factory(
    isAutoSave: Boolean,
    clock: Clock = Clock.System
): GameStateEncoder.SaveStateHeaderFactory = SaveStateHeaderFactoryImpl(
    isAutoSave = isAutoSave,
    clock = clock
)

private class SaveStateHeaderFactoryImpl(
    private val isAutoSave: Boolean,
    private val clock: Clock = Clock.System
) : GameStateEncoder.SaveStateHeaderFactory {
    override fun createSaveStateHeader() = SaveStateHeader(
        isAutoSave = isAutoSave,
        saveTime = clock.now()
    )
}