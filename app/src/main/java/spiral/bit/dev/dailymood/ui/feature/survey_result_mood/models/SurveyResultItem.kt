package spiral.bit.dev.dailymood.ui.feature.survey_result_mood.models

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ItemSurveyResultBinding

class SurveyResultItem(
    val model: SurveyResult
) : AbstractBindingItem<ItemSurveyResultBinding>() {

    override val type: Int = R.layout.item_survey_result

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemSurveyResultBinding {
        return ItemSurveyResultBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemSurveyResultBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        with(binding) {
            reasonOfRateSectionTextView.apply {
                text = context.getString(model.surveyReason)
            }
            countOfScoresTextView.apply {
                text = context.getString(R.string.you_get_scores_format, getPluralsOfScores(model.scoresInThisSection))
            }
            advicesTextView.apply {
                text = context.getString(model.advicesText)
            }
        }
    }
}

fun TextView.getPluralsOfScores(scoresCount: Int): String {
    return resources.getQuantityString(R.plurals.scores, scoresCount, scoresCount)
}

data class SurveyResult(
    @StringRes val surveyReason: Int,
    val scoresInThisSection: Int,
    @StringRes val advicesText: Int
)