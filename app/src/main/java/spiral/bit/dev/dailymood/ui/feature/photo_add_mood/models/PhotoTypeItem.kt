package spiral.bit.dev.dailymood.ui.feature.photo_add_mood.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
            createEmotionByPhotoTitleTextView.apply { text = context.getString(model.title) }
            createEmotionByPhotoDescriptionTextView.apply { text = context.getString(model.description) }
            selectThisTypeImageView.setImageResource(model.resourceId)
        }
    }
}

data class PhotoTypeModel(
    val id: Int,
    @DrawableRes val resourceId: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
)