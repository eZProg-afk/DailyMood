package spiral.bit.dev.dailymood.ui.feature.selectCreationType.models

import androidx.annotation.StringRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.databinding.ItemSelectCreationMoodTypeBinding
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem

fun selectionTypeDelegate(onItemClickListener: (SelectionTypeItem) -> Unit) =
    adapterDelegateViewBinding<SelectionTypeItem, ListItem, ItemSelectCreationMoodTypeBinding>({ inflater, container ->
        ItemSelectCreationMoodTypeBinding.inflate(inflater, container, false)
    }) {
        binding.iconNextFrameLayout.setOnClickListener {
            onItemClickListener.invoke(item)
        }
        bind {
            with(binding) {
                manuallyAddEmotionTitleTextView.apply {
                    text = context?.getString(item.title)
                }

                manuallyAddEmotionDescriptionTextView.apply {
                    text = context?.getString(item.description)
                }
            }
        }
    }

data class SelectionTypeItem(
    val id: Int,
    @StringRes val title: Int,
    @StringRes val description: Int
) : ListItem