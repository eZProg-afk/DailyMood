package spiral.bit.dev.dailymood.ui.common.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.delay
import spiral.bit.dev.dailymood.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    private val binding: FragmentLoadingDialogBinding by viewBinding()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return FragmentLoadingDialogBinding.inflate(inflater, container, false).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenResumed {
            delay(LOADING_DELAY)
            dismiss()
        }
    }

    companion object {
        private const val LOADING_DELAY = 2000L
    }
}