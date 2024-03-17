package io.sellmair.sample.models

import kotlinx.datetime.Instant
import java.io.Serializable

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
class SaveStateHeader(
    val isAutoSave: Boolean,
    val saveTime: Instant
) : GameEntity {
    companion object
}