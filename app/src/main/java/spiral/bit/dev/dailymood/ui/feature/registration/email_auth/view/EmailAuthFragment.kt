package spiral.bit.dev.dailymood.ui.feature.registration.email_auth.view

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
import spiral.bit.dev.dailymood.ui.common.view.LoadingDialogFragment
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegState
import spiral.bit.dev.dailymood.ui.feature.registration.email_reg.view.EmailRegViewModel

@AndroidEntryPoint
class EmailAuthFragment : BaseFragment<RegState, RegEffect, FragmentEmailAuthBinding>(
    FragmentEmailAuthBinding::inflate
) {

    override val viewModel: EmailRegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        bindValidators()
        setUpClicks()
    }

    private fun subscribeToObservers() = with(viewModel) {
        observe(viewLifecycleOwner, sideEffect = ::handleSideEffect)

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

    override fun handleSideEffect(sideEffect: RegEffect) = binding {
        when (sideEffect) {
            is RegEffect.Toast -> root.toast(sideEffect.msg)
            RegEffect.NavigateToReg -> {
                EmailAuthFragmentDirections.toEmailRegistration()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is RegEffect.ToMainScreen -> {
                EmailAuthFragmentDirections.toMain()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is RegEffect.NavigateToLogin -> {
            }
        }
    }

    override fun renderState(state: RegState) = binding {
        when (state.regScreenState) {
            is RegScreenState.Loading -> {
                LoadingDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    LOADING_DIALOG_TAG
                )
            }
            is RegScreenState.SuccessAuthorized -> {
                LoadingDialogFragment().dismiss()
                EmailAuthFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RegScreenState.ErrorAuthorized -> {
                LoadingDialogFragment().dismiss()
                root.toast(state.regScreenState.error.toString())
            }
            is RegScreenState.BootStrapState -> {
            }
        }
        //TODO СДЕЛАТЬ ПОАДЕКВАТНЕЕ дисмисс ЛОАДИНГА
    }

    companion object {
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
    }
}