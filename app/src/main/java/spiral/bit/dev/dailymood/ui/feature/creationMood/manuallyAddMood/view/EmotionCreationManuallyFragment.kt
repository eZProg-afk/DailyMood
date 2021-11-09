package spiral.bit.dev.dailymood.ui.feature.creationMood.manuallyAddMood.view

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.egorblagochinnov.validators.validateBy
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentMoodCreationManuallyBinding
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.base.extensions.hasPermissions
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

    private val getPermissions =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) getContent.launch(MIME_TYPE_IMAGE)
        }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            viewModel.onImageSelect(uri)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViewPager()
        bindValidators()
    }

    private fun bindValidators() = binding {
        reasonEditText.validateBy(viewLifecycleOwner, viewModel.reasonInputValidator)
    }

    private fun setUpClicks() = binding {
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

    override fun handleSideEffect(sideEffect: ManuallyEffect) = binding {
        when (sideEffect) {
            is ManuallyEffect.NavigateToMain -> {
                EmotionCreationManuallyFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
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
        addPhotoImageView.loadByUri(state.imageUri)
        sliderAdapter.items = state.sliderItems
    }

    companion object {
        private const val MIME_TYPE_IMAGE = "image/*"
        private const val READ_PERMISSION = "android.Manifest.permission.READ_EXTERNAL_STORAGE"
    }
}