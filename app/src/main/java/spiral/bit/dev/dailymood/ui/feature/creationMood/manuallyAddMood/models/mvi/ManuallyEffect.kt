package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi

import spiral.bit.dev.dailymood.data.mood.MoodEntity

sealed class ManuallyEffect {
    data class Toast(val msg: Int) : ManuallyEffect()
    data class ShowSnackbar(val msg: Int, val moodEntity: MoodEntity? = null) : ManuallyEffect()
    object NavigateToMain : ManuallyEffect()
    object NavigateBack : ManuallyEffect()
}