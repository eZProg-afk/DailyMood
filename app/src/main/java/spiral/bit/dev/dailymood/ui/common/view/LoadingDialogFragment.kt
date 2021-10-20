package spiral.bit.dev.dailymood.ui.common.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {

    private val binding: FragmentLoadingDialogBinding by viewBinding()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.loading_label))
            .setView(binding.root)
            .create()

        dialog.show()
        return dialog
    }
}