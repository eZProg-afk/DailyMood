package spiral.bit.dev.dailymood.ui.feature.main.models

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.data.emotion.EmotionItem
import spiral.bit.dev.dailymood.databinding.ItemEmotionBinding
import spiral.bit.dev.dailymood.ui.common.mappers.EmotionTypeMapper

data class EmotionUiItem(val emotionItemMainModel: EmotionItem) :
    ModelAbstractBindingItem<EmotionItem, ItemEmotionBinding>(emotionItemMainModel) {

    override val type: Int = R.layout.item_emotion
    private val emotionTypeMapper = EmotionTypeMapper()

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemEmotionBinding {
        return ItemEmotionBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemEmotionBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        val emotionType = emotionTypeMapper.mapToEmotionType(emotionItemMainModel.emotionType)
        with(binding) {
            emotionTime.text = emotionItemMainModel.formattedTime
            emotionDesc.text = emotionType.translate
            when (emotionType) {
                EmotionType.HAPPY -> emotionIv.setImageResource(R.drawable.ic_emotion_happy)
                EmotionType.NEUTRAL -> emotionIv.setImageResource(R.drawable.ic_emotion_neutral)
                EmotionType.SAD -> emotionIv.setImageResource(R.drawable.ic_emotion_sad)
                EmotionType.ANGRY -> emotionIv.setImageResource(R.drawable.ic_emotion_angry)
                else -> EmotionType.NOT_DEFINED
            }
        }
    }
}