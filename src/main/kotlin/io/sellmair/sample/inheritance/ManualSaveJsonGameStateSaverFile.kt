package io.sellmair.sample.inheritance

import io.sellmair.sample.models.SaveStateHeader
import kotlinx.datetime.Clock
import java.io.File

/**
 * Implementation of a save state which is
 *  - file-based
 *  - uses json as encoding
 *  - creates a header with current time and 'isAutoSave = false'
 */
class ManualSaveJsonGameStateSaverFile(file: File) : AbstractJsonGameStateSaverFile(file) {
    override fun createSaveStateHeader(): SaveStateHeader {
        return SaveStateHeader(isAutoSave = true, saveTime = Clock.System.now())
    }
}