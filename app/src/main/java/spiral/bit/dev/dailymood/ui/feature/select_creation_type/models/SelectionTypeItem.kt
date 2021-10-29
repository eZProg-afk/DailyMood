package spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.mvi

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ItemSelectAddEmotionTypeBinding

class SelectionTypeItem(
    override var model: SelectionTypeModel,
    override val type: Int = R.layout.item_select_add_emotion_type
) : ModelAbstractBindingItem<SelectionTypeModel, ItemSelectAddEmotionTypeBinding>(model) {

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemSelectAddEmotionTypeBinding {
        return ItemSelectAddEmotionTypeBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemSelectAddEmotionTypeBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        with(binding) {
            manuallyAddEmotionTitleTextView.apply {
                text = context?.getString(model.title)
            }

            manuallyAddEmotionDescriptionTextView.apply {
                text = context?.getString(model.description)
            }
        }
    }
}

data class SelectionTypeModel(val id: Int, @StringRes val title: Int, @StringRes val description: Int)