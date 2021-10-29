package spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.mvi

import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models.PhotoTypeItem

data class PhotoState(
    val photoTypes: List<PhotoTypeItem>,
    val galleryImage: InputImage?,
    val galleryImageUri: Uri?,
    val smileProbability: Float?
) : StateMarker