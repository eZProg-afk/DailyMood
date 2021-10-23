package spiral.bit.dev.dailymood.ui.feature.create.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentCreateEmotionBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper
import spiral.bit.dev.dailymood.ui.feature.create.models.SliderItem
import spiral.bit.dev.dailymood.ui.feature.create.models.mvi.CreateEmotionSideEffect
import spiral.bit.dev.dailymood.ui.feature.create.models.mvi.CreateEmotionState
import spiral.bit.dev.dailymood.ui.feature.main.models.MoodType
import kotlin.math.abs

@AndroidEntryPoint
class CreateEmotionFragment :
    BaseFragment<CreateEmotionState, CreateEmotionSideEffect, FragmentCreateEmotionBinding>(
        FragmentCreateEmotionBinding::inflate
    ) {

    override val viewModel: CreateEmotionViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val itemsAdapter = ItemAdapter<SliderItem>()
    private val sliderAdapter = FastAdapter.with(itemsAdapter)
    private val emotionTypeMapper = EmotionTypeMapper()

    private val getPermissions = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) getContent.launch(MIME_TYPE_IMAGE)
    }

    private val getContent = registerForActivityResult(GetContent()) { uri: Uri? ->
        viewModel.onImageSelect(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViewPager()
    }

    private fun setUpClicks() = binding {
        fabSaveEmotion.setOnClickListener {
            val emotionType = when (carouselViewPager.currentItem) {
                0 -> MoodType.HAPPY
                1 -> MoodType.NEUTRAL
                2 -> MoodType.SAD
                3 -> MoodType.ANGRY
                else -> MoodType.HAPPY
            }
            val moodValue = emotionTypeMapper.mapToMoodValue(emotionType)
            viewModel.insert(moodValue, inputNoteEditText.text.toString())
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

        addPhotoImageView.setOnClickListener {
            if (requireContext().hasPermissions(READ_PERMISSION)) {
                getContent.launch(MIME_TYPE_IMAGE).apply { it.isEnabled = false }
            } else {
                getPermissions.launch(READ_PERMISSION)
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

    override fun handleSideEffect(sideEffect: CreateEmotionSideEffect) = binding {
        when (sideEffect) {
            is CreateEmotionSideEffect.NavigateToMain -> {
                CreateEmotionFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is CreateEmotionSideEffect.ShowSnackbar -> {
                root.snack(sideEffect.msg)
            }
            is CreateEmotionSideEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }

    override fun renderState(state: CreateEmotionState) = binding {
        addPhotoImageView.loadByUri(state.imageUri)
        itemsAdapter.set(state.sliderItems)
    }

    companion object {
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val READ_PERMISSION = "android.Manifest.permission.READ_EXTERNAL_STORAGE"
    }
}