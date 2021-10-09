package spiral.bit.dev.dailymood.ui.feature.main.listeners

import spiral.bit.dev.dailymood.data.Emotion

interface EmotionListener {
    fun onEmotionClicked(emotion: Emotion)
}