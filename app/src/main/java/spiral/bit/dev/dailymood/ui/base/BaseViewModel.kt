package spiral.bit.dev.dailymood.ui.base

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost

abstract class BaseViewModel<STATE: Any, SIDE_EFFECT: Any> :
    ContainerHost<STATE, SIDE_EFFECT>,
    ViewModel() {

    abstract override val container: Container<STATE, SIDE_EFFECT>
}