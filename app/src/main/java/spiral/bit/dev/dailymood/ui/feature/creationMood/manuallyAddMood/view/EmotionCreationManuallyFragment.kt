package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentMoodCreationManuallyBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.snack
import spiral.bit.dev.dailymood.ui.base.extensions.toast
import spiral.bit.dev.dailymood.ui.common.mappers.MoodTypeMapper
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi.ManuallyEffect
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.mvi.ManuallyState
import spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.models.sliderDelegate
import kotlin.math.abs

@AndroidEntryPoint
class EmotionCreationManuallyFragment :
    BaseFragment<ManuallyState, ManuallyEffect, FragmentMoodCreationManuallyBinding>(
        FragmentMoodCreationManuallyBinding::inflate
    ) {

    override val viewModel: ManuallyViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val sliderAdapter = ListDelegationAdapter(sliderDelegate)
    private val moodTypeMapper = MoodTypeMapper()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViews()
    }

    private fun setUpViews() {
        setUpViewPager()
        setUpToolbar()
    }

    private fun setUpToolbar() = binding {
        manuallyToolbar.titleTextView.text = getString(R.string.create_emotion_label)
    }

    private fun setUpClicks() = binding {
        manuallyToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }

        fabSaveEmotion.setOnClickListener {
            val moodType = moodTypeMapper.mapToMoodType(carouselViewPager.currentItem)
            val moodValue = moodTypeMapper.mapToMoodValue(moodType)
            if (reasonEditText.text.toString().isNotEmpty()) {
                viewModel.insert(
                    moodValue,
                    inputNoteEditText.text.toString(),
                    reasonEditText.text.toString()
                )
            }
        }

        btnLeft.setOnClickListener {
            carouselViewPager.apply {
                setCurrentItem(currentItem.minus(1), true)
            }
        }

        btnRight.setOnClickListener {
            carouselViewPager.apply {
                setCurrentItem(currentItem.plus(1), true)
            }
        }
    }

    private fun setUpViewPager() = binding {
        carouselViewPager.apply {
            adapter = sliderAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 4
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

            CompositePageTransformer().apply {
                addTransformer(MarginPageTransformer(40))
                addTransformer { page, position ->
                    val r = 1 - abs(position)
                    page.scaleY = 0.85f + r * 0.15f
                }
            }.also { setPageTransformer(it) }
        }
    }

    override fun handleSideEffect(sideEffect: ManuallyEffect) = binding {
        when (sideEffect) {
            is ManuallyEffect.NavigateToMain -> {
                EmotionCreationManuallyFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is ManuallyEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
            is ManuallyEffect.ShowSnackbar -> {
                root.snack(sideEffect.msg)
            }
            is ManuallyEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }

    override fun renderState(state: ManuallyState) = binding {
        sliderAdapter.items = state.sliderItems
    }
}