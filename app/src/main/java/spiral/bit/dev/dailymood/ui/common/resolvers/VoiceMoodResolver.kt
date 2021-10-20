package spiral.bit.dev.dailymood.ui.common.resolvers

import com.projects.alshell.vokaturi.Emotion
import spiral.bit.dev.dailymood.ui.feature.main.models.EmotionType

class VoiceMoodResolver {

    fun toMyEmotionType(emotion: Emotion) = when (emotion) {
        Emotion.Happy, Emotion.Feared -> EmotionType.HAPPY
        Emotion.Neutral -> EmotionType.NEUTRAL
        Emotion.Sad -> EmotionType.SAD
        Emotion.Angry -> EmotionType.ANGRY
        else -> EmotionType.NOT_DEFINED
    }

    fun toVokaturiEmotion(emotionType: EmotionType) = when (emotionType) {
        EmotionType.HAPPY -> Emotion.Happy
        EmotionType.GOOD -> Emotion.Feared
        EmotionType.NEUTRAL -> Emotion.Neutral
        EmotionType.SAD -> Emotion.Sad
        EmotionType.ANGRY -> Emotion.Angry
        EmotionType.NOT_DEFINED -> Emotion.Happy
    }
}