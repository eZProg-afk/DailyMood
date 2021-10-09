package spiral.bit.dev.dailymood.ui.base

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost

fun <STATE : Any, SIDE_EFFECT : Any> ContainerHost<STATE, SIDE_EFFECT>.observe(
    lifecycleOwner: LifecycleOwner,
    state: (suspend (state: STATE) -> Unit)? = null,
    sideEffect: (suspend (sideEffect: SIDE_EFFECT) -> Unit)? = null
) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            state?.let { launch { container.stateFlow.collect(state) } }
            sideEffect?.let { launch { container.sideEffectFlow.collect(sideEffect) } }
        }
    }
}