package spiral.bit.dev.dailymood.ui.feature.detail.view

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.databinding.FragmentDetailEmotionBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.common.formatters.DateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.feature.detail.models.mvi.DetailEffect
import spiral.bit.dev.dailymood.ui.feature.detail.models.mvi.DetailState
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType

@AndroidEntryPoint
class DetailEmotionFragment :
    BaseFragment<DetailState, DetailEffect, FragmentDetailEmotionBinding>(
        FragmentDetailEmotionBinding::inflate
    ) {

    private val formatter = DateTimeFormatter()
    private val emotionTypeMapper = EmotionTypeMapper()
    private val args: DetailEmotionFragmentArgs by navArgs()
    override val viewModel: DetailViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getEmotionById(args.emotionId)
        viewModel.observe(
            viewLifecycleOwner,
            state = ::renderState,
            sideEffect = ::handleSideEffect
        )
    }

    private fun setUpViews(moodEntity: MoodEntity) = binding {
        var emotionTypeFeel = ""

        moodEntity.photoPath?.let {
            emotionImage.loadByUri(moodEntity.photoPath.toUri())
            emotionImage.isVisible = true
        } ?: run {
            emotionImage.isVisible = false
        }

        moodEntity.note?.let {
            emotionNote.text = String.format(getString(R.string.your_note, moodEntity.note))
            emotionNote.isVisible = true
        } ?: run {
            emotionNote.isVisible = false
        }

        when (emotionTypeMapper.mapToEmotionType(moodEntity.emotionType)) {
            MoodType.HAPPY -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_happy)
                emotionTypeFeel = getString(R.string.happy_feel_label)
            }
            MoodType.NEUTRAL -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_neutral)
                emotionTypeFeel = getString(R.string.neutral_feel_label)
            }
            MoodType.SAD -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_sad)
                emotionTypeFeel = getString(R.string.sad_feel_label)
            }
            MoodType.ANGRY -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_angry)
                emotionTypeFeel = getString(R.string.angry_feel_label)
            }
        }

        emotionInfoTypeDateTv.text = getString(
            R.string.you_feel_yourself,
            emotionTypeFeel,
            formatter.format(moodEntity.createdTime)
        )
    }

    override fun renderState(state: DetailState) {
        setUpViews(state.moodEntity)
    }

    override fun handleSideEffect(sideEffect: DetailEffect) {

    }
}