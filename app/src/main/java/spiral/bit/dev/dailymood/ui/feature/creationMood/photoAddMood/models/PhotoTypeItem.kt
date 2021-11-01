package spiral.bit.dev.dailymood.ui.feature.creationMood.photoAddMood.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.databinding.ItemAddByPhotoTypeBinding
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem

fun photoTypesDelegate(onItemClickListener: (PhotoTypeItem) -> Unit) = adapterDelegateViewBinding<PhotoTypeItem, ListItem, ItemAddByPhotoTypeBinding>({ inflater, container ->
    ItemAddByPhotoTypeBinding.inflate(inflater, container, false)
}) {
    binding.iconPhotoFrameLayout.setOnClickListener { onItemClickListener.invoke(item) }
    bind {
        with(binding) {
            createEmotionByPhotoTitleTextView.apply {
                text = context.getString(item.title)
            }
            createEmotionByPhotoDescriptionTextView.apply {
                text = context.getString(item.description)
            }
            selectThisTypeImageView.setImageResource(item.resourceId)
        }
    }
}

data class PhotoTypeItem(
    val id: Int,
    @DrawableRes val resourceId: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) : ListItem