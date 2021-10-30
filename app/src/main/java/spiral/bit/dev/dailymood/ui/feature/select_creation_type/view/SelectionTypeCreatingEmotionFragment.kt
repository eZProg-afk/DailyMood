package spiral.bit.dev.dailymood.ui.feature.select_creation_type.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentSelectionAddEmotionTypeBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.observe
import spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.mvi.SelectEffect
import spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.mvi.SelectState
import spiral.bit.dev.dailymood.ui.feature.select_creation_type.models.selectionTypeDelegate

@AndroidEntryPoint
class SelectionTypeCreatingEmotionFragment :
    BaseFragment<SelectState, SelectEffect, FragmentSelectionAddEmotionTypeBinding>(
        FragmentSelectionAddEmotionTypeBinding::inflate
    ) {

    override val viewModel: SelectionTypeViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val selectionTypesAdapter = ListDelegationAdapter(selectionTypeDelegate { item ->
        viewModel.handleClickByItemId(item.id)
    })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        setUpRecyclerView()
    }

    private fun subscribeToObservers() = with(viewModel) {
        observe(viewLifecycleOwner, state = ::renderState, sideEffect = ::handleSideEffect)
    }

    private fun setUpRecyclerView() = binding {
        selectionTypesRecyclerView.apply {
            setHasFixedSize(true)
            adapter = selectionTypesAdapter
        }
    }

    override fun handleSideEffect(effect: SelectEffect) {
        when (effect) {
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
        }
    }

    override fun renderState(state: SelectState) {
        selectionTypesAdapter.items = state.selectionTypeItems
    }
}