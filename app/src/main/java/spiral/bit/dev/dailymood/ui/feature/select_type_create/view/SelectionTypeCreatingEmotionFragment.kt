package spiral.bit.dev.dailymood.ui.feature.select_type_create.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.listeners.addClickListener
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentSelectionAddEmotionTypeBinding
import spiral.bit.dev.dailymood.databinding.ItemSelectAddEmotionTypeBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.observe
import spiral.bit.dev.dailymood.ui.feature.select_type_create.models.mvi.SelectEffect
import spiral.bit.dev.dailymood.ui.feature.select_type_create.models.mvi.SelectState
import spiral.bit.dev.dailymood.ui.feature.select_type_create.models.mvi.SelectionTypeItem

@AndroidEntryPoint
class SelectionTypeCreatingEmotionFragment :
    BaseFragment<SelectState, SelectEffect, FragmentSelectionAddEmotionTypeBinding>(
        FragmentSelectionAddEmotionTypeBinding::inflate
    ) {

    override val viewModel: SelectionTypeViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val itemsAdapter = ItemAdapter<SelectionTypeItem>()
    private val selectionTypesAdapter = FastAdapter.with(itemsAdapter).apply {
        addClickListener<ItemSelectAddEmotionTypeBinding, SelectionTypeItem>({
            it.iconNextFrameLayout
        }) { _, _, _, item ->
            viewModel.handleClickByItemId(item.model.id)
        }
    }

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
        itemsAdapter.set(state.selectionTypeItems)
    }
}