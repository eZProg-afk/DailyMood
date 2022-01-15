package spiral.bit.dev.dailymood.ui.feature.creationMood.photoAddMood.models.mvi

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import spiral.bit.dev.dailymood.ui.feature.creationMood.photoAddMood.models.PhotoTypeItem

data class PhotoState(
    val photoTypes: List<PhotoTypeItem>,
    val galleryImage: InputImage?,
    val galleryImageUri: Uri?,
    val smileProbability: Float?
)