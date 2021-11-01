package spiral.bit.dev.dailymood.ui.feature.selectCreationType.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentSelectionCreationMoodTypeBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.observe
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi.SelectEffect
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.mvi.SelectState
import spiral.bit.dev.dailymood.ui.feature.selectCreationType.models.selectionTypeDelegate

@AndroidEntryPoint
class SelectionTypeCreatingEmotionFragment :
    BaseFragment<SelectState, SelectEffect, FragmentSelectionCreationMoodTypeBinding>(
        FragmentSelectionCreationMoodTypeBinding::inflate
    ) {

    override val viewModel: SelectionTypeViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val selectionTypesAdapter = ListDelegationAdapter(selectionTypeDelegate { item ->
        viewModel.handleClickByItemId(item.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setUpViews()
        setUpClicks()
    }

    private fun setUpViews() {
        setUpRecyclerView()
        setUpToolbar()
    }

    private fun setUpToolbar() = binding {
        selectionToolbar.titleTextView.text = getString(R.string.how_you_want_to_add_emotion)
    }

    private fun setUpRecyclerView() = binding {
        selectionTypesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = selectionTypesAdapter
        }
    }

    private fun setUpClicks() = binding {
        selectionToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    private fun subscribeToObservers() = with(viewModel) {
        observe(viewLifecycleOwner, state = ::renderState, sideEffect = ::handleSideEffect)
    }

    override fun handleSideEffect(sideEffect: SelectEffect) {
        when (sideEffect) {
            SelectEffect.NavigateToCreateManually -> {
                SelectionTypeCreatingEmotionFragmentDirections.toManually().apply {
                    findNavController().navigate(this)
                }
            }
            SelectEffect.NavigateToCreateVoice -> {
                SelectionTypeCreatingEmotionFragmentDirections.toVoice().apply {
                    findNavController().navigate(this)
                }
            }
            SelectEffect.NavigateToCreateSurvey -> {
                SelectionTypeCreatingEmotionFragmentDirections.toSurvey().apply {
                    findNavController().navigate(this)
                }
            }
            SelectEffect.NavigateToCreatePhoto -> {
                SelectionTypeCreatingEmotionFragmentDirections.toPhoto().apply {
                    findNavController().navigate(this)
                }
            }
            SelectEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }

    override fun renderState(state: SelectState) {
        selectionTypesAdapter.items = state.selectionTypeItems
    }
}