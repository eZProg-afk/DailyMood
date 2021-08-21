package spiral.bit.dev.dailymood.ui.create

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentCreateEmotionBinding
import spiral.bit.dev.dailymood.helpers.*
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.main.MainViewModel
import spiral.bit.dev.dailymood.ui.main.adapters.SliderViewPagerAdapter
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionSideEffect
import spiral.bit.dev.dailymood.ui.main.models.common.EmotionState
import spiral.bit.dev.dailymood.ui.main.models.other.SliderItem
import kotlin.math.abs

@AndroidEntryPoint
class CreateEmotionFragment :
    BaseFragment<EmotionState, EmotionSideEffect>(R.layout.fragment_create_emotion) {

    private var emotionUri: String = ""
    private val sliderItems = listOf(
        SliderItem(R.drawable.ic_emotion_happy),
        SliderItem(R.drawable.ic_emotion_neutral),
        SliderItem(R.drawable.ic_emotion_sad),
        SliderItem(R.drawable.ic_emotion_angry)
    )
    private val createBinding: FragmentCreateEmotionBinding by viewBinding()
    override val viewModel: MainViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    private val getPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) getContent.launch(MIME_TYPE_IMAGE)
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            with(createBinding) {
                emotionUri = uri.toString()
                addPhotoImageView infixLoad emotionUri
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        viewModel.observe(
            viewLifecycleOwner,
            sideEffect = ::handleSideEffects
        )
    }

    private fun setUpViews() = with(createBinding) {
        setUpViewPager()
        fabSaveEmotion.setOnClickListener {
            viewModel.insert(
                carouselViewPager.currentItem,
                inputNote.text.toString(),
                emotionUri
            )
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
            if (requireContext().hasPermissions(PERM_READ)) {
                getContent.launch(MIME_TYPE_IMAGE)
                    .apply { it.isEnabled = false }
            } else {
                getPermissions.launch(PERM_READ)
            }
        }
    }

    private fun setUpViewPager() = with(createBinding) {
        carouselViewPager.apply {
            adapter =
                SliderViewPagerAdapter(sliderItems)
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

    private fun handleSideEffects(effect: EmotionSideEffect) {
        when (effect) {
            is EmotionSideEffect.NavigateToMain -> {
                CreateEmotionFragmentDirections.actionCreateEmotionFragmentToMainFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is EmotionSideEffect.ShowSnackbar -> {
                createBinding.root.snack(effect.msg)
            }
            is EmotionSideEffect.Toast -> {
                requireContext().showToast(effect.msg)
            }
            is EmotionSideEffect.NavigateToCreate -> {
            }
            is EmotionSideEffect.NavigateToDetail -> {
            }
        }
    }
}