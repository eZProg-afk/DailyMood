package spiral.bit.dev.dailymood.ui.feature.detail.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.databinding.FragmentDetailMoodBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.base.extensions.loadByDrawable
import spiral.bit.dev.dailymood.ui.base.extensions.loadByUri
import spiral.bit.dev.dailymood.ui.base.extensions.observe
import spiral.bit.dev.dailymood.ui.common.formatters.AppDateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.feature.detail.models.mvi.DetailEffect
import spiral.bit.dev.dailymood.ui.feature.detail.models.mvi.DetailState
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType

@AndroidEntryPoint
class DetailEmotionFragment :
    BaseFragment<DetailState, DetailEffect, FragmentDetailMoodBinding>(
        FragmentDetailMoodBinding::inflate
    ) {

    private val formatter = AppDateTimeFormatter()
    private val emotionTypeMapper = MoodTypeMapper()
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

//        moodEntity.manuallyData?.manuallyAddedPhotoPath?.let { photoPath ->
//            emotionImage.loadByUri(photoPath.toUri())
//            emotionImage.isVisible = true
//        } ?: run {
//            emotionImage.isVisible = false
//        }

        moodEntity.manuallyData?.note?.let { note ->
            emotionNote.text = String.format(getString(R.string.your_note, note))
            emotionNote.isVisible = true
        } ?: run {
            emotionNote.isVisible = false
        }

        when (emotionTypeMapper.mapToMoodType(moodEntity.moodValue)) {
            MoodType.HAPPY -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_happy)
                emotionTypeFeel = getString(R.string.happy_feel_label)
            }
            MoodType.GOOD -> {
                emotionPhoto.loadByDrawable(R.drawable.ic_emotion_happy)
                emotionTypeFeel = getString(R.string.good_feel_label)
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
            formatter.formatItemTime(moodEntity.createdTime)
        )
    }

    override fun renderState(state: DetailState) {
        state.moodEntity?.let { setUpViews(it) }
    }

    override fun handleSideEffect(sideEffect: DetailEffect) {

    }
}