package spiral.bit.dev.dailymood.ui.feature.main.models

import androidx.annotation.StringRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.databinding.ItemEmptyDayBinding
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem

val emptyDayDelegate = adapterDelegateViewBinding<EmptyDayItem, ListItem, ItemEmptyDayBinding>({ inflater, container ->
    ItemEmptyDayBinding.inflate(inflater, container, false)
    }) {
        bind {
            with(binding) {
                emptyDayTitleTextView.apply {
                    text = context?.getString(item.emptyDayTitle)
                }
                emptyDayHintTextView.apply {
                    text = context?.getString(item.emptyDayHint)
                }
            }
        }
    }

data class EmptyDayItem(
    @StringRes val emptyDayTitle: Int,
    @StringRes val emptyDayHint: Int
) : ListItem