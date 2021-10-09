package spiral.bit.dev.dailymood.ui.feature.registration

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.egorblagochinnov.validators.validateBy
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmailAuthBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.observe
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.common.utils.Animations
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegState

@AndroidEntryPoint
class EmailAuthFragment : BaseFragment<RegState, RegSideEffect, FragmentEmailAuthBinding>(
    FragmentEmailAuthBinding::inflate, R.layout.fragment_email_auth
) {

    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        bindValidators()
        setUpClicks()
    }

    private fun subscribeToObservers() = with(viewModel) {
        observe(viewLifecycleOwner, sideEffect = ::handleSideEffects)

        muxValidator.observe(viewLifecycleOwner) { validationSummaryResult ->
            if (validationSummaryResult.isValid) {
                showAnimateButton()
            }
        }
    }

    private fun showAnimateButton() = binding {
        buttonAuthenticate.isVisible = true
        buttonAuthenticate.startAnimation(Animations.showAnimationInToX())
    }

    private fun setUpClicks() = binding {
        buttonAuthenticate.setOnClickListener {
            viewModel.registrationByEmailAndPassword()
        }

        toggleLoginTextView.setOnClickListener {
            viewModel.toRegistration()
        }
    }

    private fun bindValidators() = binding {
        emailInputEditText.validateBy(viewLifecycleOwner, viewModel.textEmailValidator)
        passwordInputEditText.validateBy(viewLifecycleOwner, viewModel.textPasswordValidator)
    }

    private fun handleSideEffects(effect: RegSideEffect) = binding {
        when (effect) {
            is RegSideEffect.Toast -> root.toast(effect.msg)
            RegSideEffect.NavigateToReg -> {
                EmailAuthFragmentDirections.toEmailRegistration().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.ToMainScreen -> {
                EmailAuthFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.NavigateToLogin -> {
            }
        }
    }
}