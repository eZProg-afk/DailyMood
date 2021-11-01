package spiral.bit.dev.dailymood.ui.feature.creationMood.voiceAddMood.models.mvi

import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class VoiceState(
    val question: String,
    val questionCount: Int,
    val isRecorded: Boolean,
    val resultMoodEntities: ArrayList<MoodEntity>
) : StateMarker