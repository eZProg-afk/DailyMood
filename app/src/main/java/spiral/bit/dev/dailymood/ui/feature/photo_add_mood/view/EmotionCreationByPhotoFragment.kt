package spiral.bit.dev.dailymood.ui.feature.photo_add_mood.view

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageView
import com.canhub.cropper.options
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmotionCreationByPhotoBinding
import spiral.bit.dev.dailymood.databinding.ItemAddByPhotoTypeBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.hasPermissions
import spiral.bit.dev.dailymood.ui.base.extensions.safeLet
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.PhotoTypeItem
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi.PhotoEffect
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi.PhotoState

@AndroidEntryPoint
class EmotionCreationByPhotoFragment :
    BaseFragment<PhotoState, PhotoEffect, FragmentEmotionCreationByPhotoBinding>(
        FragmentEmotionCreationByPhotoBinding::inflate
    ) {

    override val viewModel: PhotoViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val galleryPermission = registerForActivityResult(RequestPermission()) { granted ->
        if (granted) analyzeFromGallery.launch(IMAGE_MIME_TYPE)
    }

    private val analyzeFromGallery = registerForActivityResult(GetContent()) { uri: Uri? ->
        uri?.let { startCropper(it) }
    }

    private val cropImage = registerForActivityResult(CropImageContract()) { cropResult ->
        if (cropResult.isSuccessful) {
            cropResult.uriContent?.let { uri -> viewModel.buildInputImage(requireContext(), uri) }
        }
    }

    private fun startCropper(imageUri: Uri) {
        cropImage.launch(
            options(uri = imageUri) {
                setCropShape(CropImageView.CropShape.RECTANGLE)
                setGuidelines(CropImageView.Guidelines.ON)
                setAllowFlipping(true)
            }
        )
    }

    private val itemAdapter = ItemAdapter<PhotoTypeItem>()
    private val photoTypesAdapter = FastAdapter.with(itemAdapter).apply {
        addClickListener<ItemAddByPhotoTypeBinding, PhotoTypeItem>({
            it.iconPhotoFrameLayout
        }) { _, _, _, item ->
            viewModel.onPhotoTypeClicked(item.model.id)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() = binding {
        addByPhotoTypesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = photoTypesAdapter
        }
    }

    private fun handleGalleryPermissions(): Boolean {
        return if (requireContext().hasPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            true
        } else {
            galleryPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            false
        }
    }

    override fun renderState(state: PhotoState) = binding {
        itemAdapter.set(state.photoTypes)
        state.galleryImage?.let { inputImage -> viewModel.detectFace(inputImage) }
        safeLet(state.galleryImageUri, state.smileProbability) { _, _ ->
            viewModel.showResultPhoto()
        }
    }

    override fun handleSideEffect(sideEffect: PhotoEffect) = binding {
        when (sideEffect) {
            is PhotoEffect.NavigateToShowMoodRating -> {
                EmotionCreationByPhotoFragmentDirections.toShowMoodRatingCameraFragment(
                    sideEffect.smilingProbabilities, sideEffect.galleryImage.toString()
                ).apply {
                    findNavController().navigate(this)
                }
            }
            is PhotoEffect.AddEmotionByCamera -> {
                EmotionCreationByPhotoFragmentDirections.toEmotionCreationByCamera().apply {
                    findNavController().navigate(this)
                }
            }
            is PhotoEffect.AddEmotionByRealtime -> {
                EmotionCreationByPhotoFragmentDirections.toEmotionCreationByRealtime().apply {
                    findNavController().navigate(this)
                }
            }
            is PhotoEffect.AddEmotionByGallery -> {
                if (handleGalleryPermissions()) analyzeFromGallery.launch(IMAGE_MIME_TYPE)
            }
            is PhotoEffect.ExceptionHappened -> {
                error(sideEffect.error)
            }
            is PhotoEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
        }
    }

    companion object {
        private const val IMAGE_MIME_TYPE = "image/*"
    }
}