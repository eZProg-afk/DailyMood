package spiral.bit.dev.dailymood.ui.feature.main.models

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.MoodEntity
import spiral.bit.dev.dailymood.databinding.ItemEmotionBinding
import spiral.bit.dev.dailymood.ui.common.formatters.Formatter
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper

class MoodItem(val moodEntity: MoodEntity) : AbstractBindingItem<ItemEmotionBinding>() {

    override val type: Int = R.layout.item_emotion
    private val formatter = Formatter()
    private val emotionTypeMapper = EmotionTypeMapper()

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemEmotionBinding {
        return ItemEmotionBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemEmotionBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        val emotionType = emotionTypeMapper.mapToEmotionType(moodEntity.emotionType)
        with(binding) {
            emotionTime.text = formatter.format(moodEntity.createdTime)
            emotionDesc.text = emotionType.translate
            when (emotionType) {
                MoodType.HAPPY -> emotionIv.setImageResource(R.drawable.ic_emotion_happy)
                MoodType.NEUTRAL -> emotionIv.setImageResource(R.drawable.ic_emotion_neutral)
                MoodType.SAD -> emotionIv.setImageResource(R.drawable.ic_emotion_sad)
                MoodType.ANGRY -> emotionIv.setImageResource(R.drawable.ic_emotion_angry)
                else -> MoodType.NOT_DEFINED
            }
        }
    }
}