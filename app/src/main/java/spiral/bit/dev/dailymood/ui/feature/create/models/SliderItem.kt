package spiral.bit.dev.dailymood.ui.feature.create.models

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ViewSliderImageBinding

class SliderItem(@DrawableRes val imageResource: Int) : AbstractBindingItem<ViewSliderImageBinding>() {

    override val type: Int = R.layout.view_slider_image

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ViewSliderImageBinding {
        return ViewSliderImageBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ViewSliderImageBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.sliderImageView.setImageResource(imageResource)
    }
}