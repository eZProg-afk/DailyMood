package spiral.bit.dev.dailymood.ui.feature.creationResult.surveyResultMood.models

import android.widget.TextView
import androidx.annotation.StringRes
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.ItemSurveyResultBinding
import spiral.bit.dev.dailymood.ui.common.adapter.models.ListItem

val surveyResultDelegate =
    adapterDelegateViewBinding<SurveyResultItem, ListItem, ItemSurveyResultBinding>(
        { layoutInflater, root -> ItemSurveyResultBinding.inflate(layoutInflater, root, false) }
    ) {
        bind {
            with(binding) {
                reasonOfRateSectionTextView.apply {
                    text = context.getString(item.surveyReason)
                }
                countOfScoresTextView.apply {
                    text = context.getString(
                        R.string.you_get_scores_format,
                        getPluralsOfScores(item.scoresInThisSection)
                    )
                }
                advicesTextView.apply {
                    text = context.getString(item.advicesText)
                }
            }
        }
    }

data class SurveyResultItem(
    @StringRes val surveyReason: Int,
    val scoresInThisSection: Int,
    @StringRes val advicesText: Int
) : ListItem

fun TextView.getPluralsOfScores(scoresCount: Int): String {
    return resources.getQuantityString(R.plurals.scores, scoresCount, scoresCount)
}