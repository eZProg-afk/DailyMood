package spiral.bit.dev.dailymood.ui.feature.creationMood.voiceAddMood.view

import android.annotation.SuppressLint
import android.content.Context
import com.projects.alshell.vokaturi.Vokaturi
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.common.resolvers.VokaturiEmotionResolver
import spiral.bit.dev.dailymood.ui.feature.creationMood.voiceAddMood.models.mvi.VoiceEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.voiceAddMood.models.mvi.VoiceState
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @SuppressLint("StaticFieldLeak") //this is application context, not leak
@Inject constructor(
    private val moodRepository: MoodRepository,
    @ApplicationContext private val context: Context
) : BaseViewModel<VoiceState, VoiceEffect>() {

    private val emotionTypeMapper = EmotionTypeMapper()
    private val emotionResolver = VokaturiEmotionResolver()
    private var _vokaturiApi: Vokaturi? = null
    private val vokaturiApi: Vokaturi get() = checkNotNull(_vokaturiApi)

    override val container =
        container<VoiceState, VoiceEffect>(
            VoiceState(
                question = getRandomQuestion(),
                questionCount = 0,
                isRecorded = false,
                arrayListOf()
            )
        )

    fun initVokaturiApi() = runCatching {
        _vokaturiApi = Vokaturi.getInstance(context)
    }.onFailure {
        Logger.logError(it)
    }

    private fun getRandomQuestion(): String {
        val randomNumber = (0..QUESTION_ARRAY_SIZE).random()
        val questions = context.resources.getStringArray(R.array.questions)
        return questions[randomNumber]
    }

    fun onRecordClick(questionCount: Int = 0) = intent {
        if (questionCount != 0) reduce { state.copy(questionCount = questionCount) }
        if (!state.isRecorded) onBeginAnswer()
        else onEndAnswer()
    }

    private fun onEndAnswer() = intent {
        runCatching {
            vokaturiApi.stopListeningAndAnalyze()
        }.onSuccess { emotionProbabilities ->
            val recognizedEmotion = Vokaturi.extractEmotion(emotionProbabilities)
            val emotionType = emotionResolver.toDailyMood(recognizedEmotion)
            val moodValue = emotionTypeMapper.mapToMoodValue(emotionType)
            val emotion = MoodEntity(moodType = moodValue)

            val emotionsList = state.resultMoodEntities
            emotionsList.add(emotion)

            var questionCount = state.questionCount
            questionCount++

            reduce {
                state.copy(
                    isRecorded = false,
                    resultMoodEntities = emotionsList,
                    questionCount = questionCount,
                    question = getRandomQuestion()
                )
            }.also {
                isNeedToAsk(state.questionCount)
            }
        }.onFailure {
            reduce { state.copy(isRecorded = false) }
            postSideEffect(VoiceEffect.TooQuiet)
        }
    }

    private fun onBeginAnswer() = intent {
        vokaturiApi.startListeningForSpeech()
        reduce {
            state.copy(isRecorded = true)
        }
    }

    private fun isNeedToAsk(questionCount: Int) = intent {
        Logger.logDebug(state.questionCount.toString())
        if (questionCount == QUESTION_SUMMARY_COUNT) {
            calculateResultEmotion()
        }
    }

    private fun calculateResultEmotion() = intent {
        val emotionTypes = state.resultMoodEntities.map { it.moodType }
        val emotionType = emotionTypes.groupingBy { it }.eachCount().filter { it.value > 1 }
        insertEmotion(MoodEntity(moodType = emotionType.entries.first().key))
        //TODO THIS IS NOT WORK OR WORK?
    }

    private fun insertEmotion(moodEntity: MoodEntity) = intent {
        moodRepository.insert(moodEntity)
        reduce { state.copy(questionCount = 1) }
        postSideEffect(VoiceEffect.NavigateToMain)
    }

    companion object {
        private const val QUESTION_SUMMARY_COUNT = 3
        private const val QUESTION_ARRAY_SIZE = 60
    }
}