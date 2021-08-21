package spiral.bit.dev.dailymood.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment<STATE : StateMarker, SIDE_EFFECT : SideEffectMarker>(
    fragmentResourceId: Int
) : Fragment(fragmentResourceId) {

    abstract val viewModel: BaseViewModel<STATE, SIDE_EFFECT>
}