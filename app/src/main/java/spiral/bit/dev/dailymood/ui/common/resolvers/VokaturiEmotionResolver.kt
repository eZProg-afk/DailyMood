package spiral.bit.dev.dailymood.ui.common.resolvers

import com.projects.alshell.vokaturi.Emotion
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType

class VokaturiEmotionResolver {

    fun toDailyMood(emotion: Emotion) = when (emotion) {
        Emotion.Happy, Emotion.Feared -> MoodType.HAPPY
        Emotion.Neutral -> MoodType.NEUTRAL
        Emotion.Sad -> MoodType.SAD
        Emotion.Angry -> MoodType.ANGRY
        else -> MoodType.NOT_DEFINED
    }

    fun toVokaturi(moodType: MoodType) = when (moodType) {
        MoodType.HAPPY -> Emotion.Happy
        MoodType.GOOD -> Emotion.Feared
        MoodType.NEUTRAL -> Emotion.Neutral
        MoodType.SAD -> Emotion.Sad
        MoodType.ANGRY -> Emotion.Angry
        MoodType.NOT_DEFINED -> Emotion.Happy
    }
}