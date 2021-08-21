package spiral.bit.dev.dailymood.ui.detail

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.databinding.FragmentDetailEmotionBinding
import spiral.bit.dev.dailymood.helpers.infixLoad
import spiral.bit.dev.dailymood.helpers.loadDrawable
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.main.MainViewModel
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionSideEffect
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionState
import spiral.bit.dev.dailymood.ui.main.models.other.EmotionType

@AndroidEntryPoint
class DetailEmotionFragment :
    BaseFragment<EmotionState, EmotionSideEffect>(R.layout.fragment_detail_emotion) {

    private val args: DetailEmotionFragmentArgs by navArgs()
    private val detailBinding: FragmentDetailEmotionBinding by viewBinding()
    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews(args.emotion)
    }

    private fun setUpViews(emotion: Emotion) = with(detailBinding) {
        var emotionTypeFeel = ""

        if (emotion.emotionPhoto.isNotEmpty()) {
            emotionImage.isVisible = true
            emotionImage infixLoad emotion.emotionPhoto
        } else {
            emotionImage.isVisible = false
        }

        if (emotion.note.isNotEmpty()) {
            emotionNote.isVisible = true
            emotionNote.text = String.format("Ваша заметка: ${emotion.note}")
        } else {
            emotionNote.isVisible = false
        }

        when (emotion.emotionType) {
            EmotionType.HAPPY -> {
                emotionPhoto.loadDrawable(R.drawable.ic_emotion_happy)
                emotionTypeFeel = getString(R.string.happy_feel_label)
            }
            EmotionType.NEUTRAL -> {
                emotionPhoto.loadDrawable(R.drawable.ic_emotion_neutral)
                emotionTypeFeel = getString(R.string.neutral_feel_label)
            }
            EmotionType.SAD -> {
                emotionPhoto.loadDrawable(R.drawable.ic_emotion_sad)
                emotionTypeFeel = getString(R.string.sad_feel_label)
            }
            EmotionType.ANGRY -> {
                emotionPhoto.loadDrawable(R.drawable.ic_emotion_angry)
                emotionTypeFeel = getString(R.string.angry_feel_label)
            }
        }

        emotionInfoTypeDateTv.text = String.format(
            "Вы чувствовали себя $emotionTypeFeel \n в ${emotion.formattedTime}"
        )
    }
}