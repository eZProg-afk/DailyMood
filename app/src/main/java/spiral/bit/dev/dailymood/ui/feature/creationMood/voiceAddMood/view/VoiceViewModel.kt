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
import spiral.bit.dev.dailymood.data.models.Question
import spiral.bit.dev.dailymood.data.models.Audio
import spiral.bit.dev.dailymood.data.models.AudioData
import spiral.bit.dev.dailymood.data.models.CreationType
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.data.mood.MoodRepository
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
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

    private val emotionTypeMapper = MoodTypeMapper()
    private val emotionResolver = VokaturiEmotionResolver()
    private var _vokaturiApi: Vokaturi? = null
    private val vokaturiApi: Vokaturi get() = checkNotNull(_vokaturiApi)

    override val container =
        container<VoiceState, VoiceEffect>(
            VoiceState(
                question = getRandomQuestion(),
                questionCount = 0,
                isRecorded = false,
                resultMoodEntities = arrayListOf(),
                audioData = null
            )
        )

    fun initVokaturiApi() = runCatching {
        _vokaturiApi = Vokaturi.getInstance(context)
    }.onFailure {
        Logger.logError(it)
    }

    private fun getRandomQuestion(): String {
        val randomNumber = (0..QUESTION_ARRAY_SIZE).random()
        val questions = context.resources.getStringArray(R.array.voice_questions)
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
            val moodType = emotionResolver.toDailyMood(recognizedEmotion)

            val questionsItemsList = state.audioData?.questions?.toMutableList() ?: mutableListOf()
            val questionItem = Question(moodType = moodType, question = state.question)
            questionsItemsList.add(questionItem)
            val audioItemsList =
                state.audioData?.answersAudioPaths?.toMutableList() ?: mutableListOf()
            val audioItem =
                Audio(id = state.questionCount, path = vokaturiApi.recordedAudio.absolutePath)
            audioItemsList.add(audioItem)

            var questionCount = state.questionCount
            questionCount++

            val audioData =
                AudioData(answersAudioPaths = audioItemsList, questions = questionsItemsList)

            val moodValue = emotionTypeMapper.mapToMoodValue(moodType)
            val moodEntity = MoodEntity(
                moodValue = moodValue,
                audioData = audioData,
                creationType = CreationType.BY_VOICE
            )

            val moodEntitiesList = state.resultMoodEntities
            moodEntitiesList.add(moodEntity)

            reduce {
                state.copy(
                    isRecorded = false,
                    resultMoodEntities = moodEntitiesList,
                    questionCount = questionCount,
                    question = getRandomQuestion(),
                    audioData = audioData
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
        if (questionCount == QUESTION_SUMMARY_COUNT) {
            calculateResultEmotion()
        }
    }

    private fun calculateResultEmotion() = intent {
        val resultMoodEntities = state.resultMoodEntities
        val audioData = state.audioData
        val emotionTypes = resultMoodEntities.map { it.moodValue }
        val emotionType = emotionTypes.groupingBy { it }.eachCount().filter { it.value > 1 }
        MoodEntity(
            moodValue = emotionType.entries.first().key,
            audioData = audioData,
            creationType = CreationType.BY_VOICE
        ).apply {
            moodRepository.insert(this)
        }
        reduce { state.copy(questionCount = 1) }
        postSideEffect(VoiceEffect.NavigateToMain)
    }

    fun navigateBack() = intent {
        postSideEffect(VoiceEffect.NavigateBack)
    }

    companion object {
        private const val QUESTION_SUMMARY_COUNT = 3
        private const val QUESTION_ARRAY_SIZE = 60
    }
}