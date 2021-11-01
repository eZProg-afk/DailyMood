package spiral.bit.dev.dailymood.ui.feature.main.models

import androidx.recyclerview.widget.DiffUtil
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.mood.MoodEntity
import spiral.bit.dev.dailymood.databinding.ItemMoodBinding
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem
import spiral.bit.dev.dailymood.ui.common.formatters.AppDateTimeFormatter
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper

private val formatter = AppDateTimeFormatter()
private val emotionTypeMapper = EmotionTypeMapper()

fun moodItemDelegate(onItemClickListener: (MoodItem) -> Unit) =
    adapterDelegateViewBinding<MoodItem, ListItem, ItemMoodBinding>({ inflater, container ->
        ItemMoodBinding.inflate(inflater, container, false)
    }) {
        binding.root.setOnClickListener { onItemClickListener.invoke(item) }
        bind {
            val emotionType = emotionTypeMapper.mapToEmotionType(item.moodEntity.moodType)
            with(binding) {
                createdTimeTextView.text = formatter.formatMoodItem(item.moodEntity.createdTime)
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

val diffCallback = object : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem) =
        oldItem::class == newItem::class

    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem) =
        oldItem.hashCode() == newItem.hashCode()
}

data class MoodItem(
    val moodEntity: MoodEntity
) : ListItem