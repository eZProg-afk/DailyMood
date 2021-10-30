package spiral.bit.dev.dailymood.ui.feature.photo_add_mood.view

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetector
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.di.FaceDetectorOptionsStatic
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.base.Logger
import spiral.bit.dev.dailymood.ui.base.listenAwait
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.PhotoTypeItem
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi.PhotoEffect
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi.PhotoState
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    @FaceDetectorOptionsStatic private val faceDetector: FaceDetector
) : BaseViewModel<PhotoState, PhotoEffect>() {

    override val container = container<PhotoState, PhotoEffect>(
        PhotoState(
            photoTypes = listOf(
                PhotoTypeItem(
                    0, R.drawable.ic_camera, R.string.camera_type_title,
                    R.string.camera_type_descripion
                ),
                PhotoTypeItem(
                    1, R.drawable.ic_gallery, R.string.gallery_type_title,
                    R.string.gallery_type_descripion
                ),
                PhotoTypeItem(
                    2, R.drawable.ic_realtime, R.string.realtime_type_title,
                    R.string.realtime_type_descripion
                ),
            ),
            galleryImage = null,
            galleryImageUri = null,
            smileProbability = null
        )
    )

    fun detectFace(image: InputImage) = intent {
        runCatching {
            return@runCatching faceDetector.process(image).listenAwait()
        }.onFailure {
            postSideEffect(PhotoEffect.ExceptionHappened(it))
        }.onSuccess { faces ->
            when {
                faces.size > 1 -> postSideEffect(PhotoEffect.Toast(R.string.faces_too_many))
                faces.isEmpty() -> postSideEffect(PhotoEffect.Toast(R.string.your_face_not_visible_label))
                else -> {
                    reduce { state.copy(smileProbability = faces.first().smilingProbability!!) }
                }
            }
        }
    }

    fun buildInputImage(context: Context, uri: Uri) = intent {
        runCatching {
            reduce { state.copy(galleryImageUri = uri) }
            InputImage.fromFilePath(context, uri)
        }.onSuccess { image ->
            reduce { state.copy(galleryImage = image) }
        }.onFailure { throwable ->
            Logger.logError(throwable)
        }
    }

    fun showResultPhoto() = intent {
        postSideEffect(
            PhotoEffect.NavigateToShowMoodRating(
                state.smileProbability!!,
                state.galleryImageUri!! //TODO !!
            )
        ).also {
            reduce {
                state.copy(
                    smileProbability = null,
                    galleryImageUri = null,
                    galleryImage = null
                )
            }
        }
    }

    fun onPhotoTypeClicked(itemId: Int) = intent {
        when (itemId) {
            0 -> postSideEffect(PhotoEffect.AddEmotionByCamera)
            1 -> postSideEffect(PhotoEffect.AddEmotionByGallery)
            2 -> postSideEffect(PhotoEffect.AddEmotionByRealtime)
        }
    }
}