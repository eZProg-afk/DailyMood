package spiral.bit.dev.dailymood.ui.registration

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmailRegBinding
import spiral.bit.dev.dailymood.helpers.observe
import spiral.bit.dev.dailymood.helpers.showToast
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.registration.common.RegState

@AndroidEntryPoint
class EmailRegFragment : BaseFragment<RegState, RegSideEffect>(R.layout.fragment_email_reg) {

    private val emailRegBinding: FragmentEmailRegBinding by viewBinding()
    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, sideEffect = ::handleSideEffects)
        setUpViews()
    }

    private fun setUpViews() = with(emailRegBinding) {
        emailRegBinding.btnReg.setOnClickListener { regIfFieldsValid() }
        emailRegBinding.regToggleLoginTextview.setOnClickListener {
            viewModel.toLogin()
        }
    }

    private fun regIfFieldsValid() = with(emailRegBinding) {
        if (regEtEmail.text.toString().isEmpty()
            || !Patterns.EMAIL_ADDRESS.matcher(regEtEmail.text.toString()).matches()
        ) {
            regEtEmail.error = getString(R.string.invalid_email_error)
        } else if (regEtPass.text.toString().isEmpty() || regEtSubmitPass.text.toString()
                .isEmpty()
        ) {
            regEtPass.error = getString(R.string.password_empty_error)
            regEtSubmitPass.error = getString(R.string.and_submit_password_error)
        } else if (regEtPass.text.toString().length < 7) {
            regEtPass.error = getString(R.string.password_not_enough_large_error)
        } else if (regEtPass.text.toString() != regEtSubmitPass.text.toString()) {
            regEtPass.error = getString(R.string.passwords_must_be_equals_error)
            regEtSubmitPass.error = getString(R.string.passwords_must_be_equals_error)
        } else {
            viewModel.regByEmailAndPassword(
                regEtEmail.text.toString(),
                regEtPass.text.toString()
            )
        }
    }

    private fun handleSideEffects(effect: RegSideEffect) {
        when (effect) {
            is RegSideEffect.Toast -> {
                requireContext().showToast(effect.msg)
            }
            RegSideEffect.NavigateToLogin -> {
                EmailRegFragmentDirections.actionEmailRegFragmentToEmailAuthFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.ToMainScreen -> {
                EmailRegFragmentDirections.actionEmailRegFragmentToMainFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.NavigateToReg -> {
            }
        }
    }
}