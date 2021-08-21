package spiral.bit.dev.dailymood.ui.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.dailymood.databinding.SliderImageBinding
import spiral.bit.dev.dailymood.ui.main.models.other.SliderItem

class SliderViewPagerAdapter(private val sliderItems: List<SliderItem>) :
    RecyclerView.Adapter<SliderViewPagerAdapter.SliderViewHolder>() {

    class SliderViewHolder(private val binding: SliderImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(sliderItem: SliderItem) = with(binding) {
            sliderImage.setImageResource(sliderItem.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        SliderViewHolder(
            SliderImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) =
        holder.bind(sliderItems[position])

    override fun getItemCount() = sliderItems.size
}