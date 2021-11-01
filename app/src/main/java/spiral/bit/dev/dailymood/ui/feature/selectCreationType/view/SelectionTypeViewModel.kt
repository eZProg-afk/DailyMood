package spiral.bit.dev.dailymood.ui.feature.selectCreationType.view

import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.ui.base.BaseViewModel
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.SelectionTypeItem
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi.SelectEffect
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi.SelectState

class SelectionTypeViewModel : BaseViewModel<SelectState, SelectEffect>() {

    override val container =
        container<SelectState, SelectEffect>(SelectState(listSelectionTypes))

    private fun toCreateManually() = intent {
        postSideEffect(SelectEffect.NavigateToCreateManually)
    }

    private fun toCreateVoice() = intent {
        postSideEffect(SelectEffect.NavigateToCreateVoice)
    }

    private fun toCreateSurvey() = intent {
        postSideEffect(SelectEffect.NavigateToCreateSurvey)
    }

    private fun toCreatePhoto() = intent {
        postSideEffect(SelectEffect.NavigateToCreatePhoto)
    }

    fun handleClickByItemId(id: Int) {
        when (id) {
            0 -> toCreateManually()
            1 -> toCreateVoice()
            2 -> toCreateSurvey()
            3 -> toCreatePhoto()
        }
    }

    fun navigateBack() = intent {
        postSideEffect(SelectEffect.NavigateBack)
    }

    companion object {
        private val listSelectionTypes = listOf(
            SelectionTypeItem(
                0,
                R.string.manually_option,
                R.string.manually_description
            ),
            SelectionTypeItem(
                1,
                R.string.voice_option,
                R.string.voice_description
            ),
            SelectionTypeItem(
                2,
                R.string.survey_option,
                R.string.survey_description
            ),
            SelectionTypeItem(
                3,
                R.string.photo_option,
                R.string.photo_description
            )
        )
    }
}