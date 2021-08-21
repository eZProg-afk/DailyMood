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
import spiral.bit.dev.dailymood.databinding.FragmentEmailAuthBinding
import spiral.bit.dev.dailymood.helpers.observe
import spiral.bit.dev.dailymood.helpers.showToast
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.registration.common.RegState

@AndroidEntryPoint
class EmailAuthFragment : BaseFragment<RegState, RegSideEffect>(R.layout.fragment_email_auth) {

    private val emailAuthBinding: FragmentEmailAuthBinding by viewBinding()
    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observe(viewLifecycleOwner, sideEffect = ::handleSideEffects)
        setUpViews()
    }

    private fun setUpViews() = with(emailAuthBinding) {
        btnAuth.setOnClickListener { authIfFieldsValid() }
        authToggleLoginTextview.setOnClickListener {
            viewModel.toRegistration()
        }
    }

    private fun authIfFieldsValid() = with(emailAuthBinding) {
        if (regEtEmail.text.toString()
                .isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(regEtEmail.text.toString())
                .matches()
        ) {
            regEtEmail.error = getString(R.string.invalid_email_error)
        } else if (regEtPass.text.toString().isEmpty()) {
            regEtPass.error = getString(R.string.password_empty_error)
        } else if (regEtPass.text.toString().length < 7) {
            regEtPass.error = getString(R.string.password_not_enough_large_error)
        } else {
            viewModel.loginByEmailAndPassword(
                regEtEmail.text.toString(),
                regEtPass.text.toString()
            )
        }
    }

    private fun handleSideEffects(effect: RegSideEffect) {
        when (effect) {
            is RegSideEffect.Toast -> requireContext().showToast(effect.msg)
            RegSideEffect.NavigateToReg -> {
                EmailAuthFragmentDirections.actionEmailAuthFragmentToEmailRegFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.ToMainScreen -> {
                EmailAuthFragmentDirections.actionEmailAuthFragmentToMainFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.NavigateToLogin -> {
            }
        }
    }
}