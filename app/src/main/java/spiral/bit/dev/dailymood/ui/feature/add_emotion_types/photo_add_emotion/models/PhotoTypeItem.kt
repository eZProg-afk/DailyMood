package spiral.bit.dev.dailymood.ui.feature.add_emotion_types.photo_add_emotion.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ItemAddByPhotoTypeBinding

class PhotoTypeItem(
    val model: PhotoTypeModel
) : AbstractBindingItem<ItemAddByPhotoTypeBinding>() {

    override val type: Int = R.layout.item_add_by_photo_type

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemAddByPhotoTypeBinding {
        return ItemAddByPhotoTypeBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemAddByPhotoTypeBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        with(binding) {
            createEmotionByPhotoTitleTextView.text = model.title
            createEmotionByPhotoDescriptionTextView.text = model.description
            selectThisTypeImageView.setImageResource(model.resourceId)
        }
    }
}

data class PhotoTypeModel(
    val id: Int,
    @DrawableRes val resourceId: Int,
    val title: String,
    val description: String
)