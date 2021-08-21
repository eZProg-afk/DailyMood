package spiral.bit.dev.dailymood.ui.main.listeners

import spiral.bit.dev.dailymood.data.Emotion

interface EmotionListener {
    fun onEmotionClicked(emotion: Emotion)
}