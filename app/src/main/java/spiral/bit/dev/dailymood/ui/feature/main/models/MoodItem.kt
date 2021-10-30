package spiral.bit.dev.dailymood.ui.feature.main.models

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.databinding.ItemEmotionBinding
import spiral.bit.dev.dailymood.ui.common.adapter.ListItem
import spiral.bit.dev.dailymood.ui.common.formatters.DateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper

private val formatter = DateTimeFormatter()
private val emotionTypeMapper = EmotionTypeMapper()

fun moodItemDelegate(onItemClickListener: (MoodItem) -> Unit) =
    adapterDelegateViewBinding<MoodItem, MoodItem, ItemEmotionBinding>({ inflater, container ->
        ItemEmotionBinding.inflate(inflater, container, false)
    }) {
        binding.root.setOnClickListener { onItemClickListener.invoke(item) }
        bind {
            val emotionType = emotionTypeMapper.mapToEmotionType(item.moodEntity.moodType)
            with(binding) {
                createdTimeTextView.text = formatter.format(item.moodEntity.createdTime)
                feelLabelTextView.text = emotionType.translate
                when (emotionType) {
                    MoodType.HAPPY -> iconMoodImageView.setImageResource(R.drawable.ic_emotion_happy)
                    MoodType.GOOD -> iconMoodImageView.setImageResource(R.drawable.ic_emotion_good)
                    MoodType.NEUTRAL -> iconMoodImageView.setImageResource(R.drawable.ic_emotion_neutral)
                    MoodType.SAD -> iconMoodImageView.setImageResource(R.drawable.ic_emotion_sad)
                    MoodType.ANGRY -> iconMoodImageView.setImageResource(R.drawable.ic_emotion_angry)
                    else -> MoodType.NOT_DEFINED
                }
            }
        }
    }

val diffCallback = object : DiffUtil.ItemCallback<MoodItem>() {
    override fun areItemsTheSame(oldItem: MoodItem, newItem: MoodItem) =
        oldItem::class == newItem::class && oldItem.moodEntity.id == newItem.moodEntity.id

    override fun areContentsTheSame(oldItem: MoodItem, newItem: MoodItem) =
        oldItem.moodEntity.createdTime == newItem.moodEntity.createdTime
}

data class MoodItem(
    val moodEntity: MoodEntity
) : ListItem