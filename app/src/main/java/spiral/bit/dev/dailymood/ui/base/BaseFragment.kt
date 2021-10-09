package spiral.bit.dev.dailymood.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<STATE : StateMarker, SIDE_EFFECT : SideEffectMarker, BINDING :  ViewBinding>(
    private val inflateBinding: (LayoutInflater, ViewGroup?, Boolean) -> BINDING,
    @LayoutRes private val fragmentResourceId: Int
) : Fragment(fragmentResourceId) {

    var binding: BINDING? = null
        private set

    abstract val viewModel: BaseViewModel<STATE, SIDE_EFFECT>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflateBinding(inflater, container, false).also { binding = it }.root
}