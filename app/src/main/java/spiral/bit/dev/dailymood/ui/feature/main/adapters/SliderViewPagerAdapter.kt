package spiral.bit.dev.dailymood.ui.feature.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.dailymood.databinding.ViewSliderImageBinding
import spiral.bit.dev.dailymood.ui.feature.main.models.other.SliderItem

class SliderViewPagerAdapter(private val sliderItems: List<SliderItem>) :
    RecyclerView.Adapter<SliderViewPagerAdapter.SliderViewHolder>() {

    class SliderViewHolder(private val binding: ViewSliderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: SliderItem) = with(binding) {
            sliderImage.setImageResource(sliderItem.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SliderViewHolder(
            ViewSliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) =
        holder.bind(sliderItems[position])

    override fun getItemCount() = sliderItems.size
}