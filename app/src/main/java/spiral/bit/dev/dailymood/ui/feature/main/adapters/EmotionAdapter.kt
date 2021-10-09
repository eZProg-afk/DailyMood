package spiral.bit.dev.dailymood.ui.feature.main.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.Emotion
import spiral.bit.dev.dailymood.databinding.ItemEmotionBinding
import spiral.bit.dev.dailymood.ui.feature.main.listeners.EmotionListener
import spiral.bit.dev.dailymood.ui.feature.main.models.other.EmotionType

class EmotionAdapter(private val emotionListener: EmotionListener) :
    ListAdapter<Emotion, EmotionAdapter.MainHolder>(DiffCallback()) {

    inner class MainHolder(private val binding: ItemEmotionBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val emotion = getItem(position)
                    emotionListener.onEmotionClicked(emotion)
                }
            }
        }

        fun bind(emotion: Emotion) = with(binding) {
            emotionTime.text = emotion.formattedTime
            emotionDesc.text = emotion.emotionType.translate
            when (emotion.emotionType) {
                EmotionType.HAPPY -> emotionIv.setImageResource(R.drawable.ic_emotion_happy)
                EmotionType.NEUTRAL -> emotionIv.setImageResource(R.drawable.ic_emotion_neutral)
                EmotionType.SAD -> emotionIv.setImageResource(R.drawable.ic_emotion_sad)
                EmotionType.ANGRY -> emotionIv.setImageResource(R.drawable.ic_emotion_angry)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MainHolder(ItemEmotionBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MainHolder, position: Int) =
        holder.bind(currentList[position])

    private class DiffCallback : DiffUtil.ItemCallback<Emotion>() {
        override fun areItemsTheSame(oldItem: Emotion, newItem: Emotion) =
            oldItem.emotionId == newItem.emotionId

        override fun areContentsTheSame(oldItem: Emotion, newItem: Emotion) =
            oldItem == newItem

    }
}