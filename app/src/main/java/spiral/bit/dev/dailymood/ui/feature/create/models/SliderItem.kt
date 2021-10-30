package spiral.bit.dev.dailymood.ui.feature.create.models

import androidx.annotation.DrawableRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.databinding.ViewSliderImageBinding
import spiral.bit.dev.dailymood.ui.common.adapter.ListItem

val sliderDelegate = adapterDelegateViewBinding<SliderItem, ListItem, ViewSliderImageBinding>({ inflater, container ->
    ViewSliderImageBinding.inflate(inflater, container, false)
}) {
    bind {
        binding.sliderImageView.setImageResource(item.imageResource)
    }
}

class SliderItem(@DrawableRes val imageResource: Int) : ListItem