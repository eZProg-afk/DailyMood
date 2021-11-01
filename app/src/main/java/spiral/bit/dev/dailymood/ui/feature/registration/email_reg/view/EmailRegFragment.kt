package spiral.bit.dev.dailymood.ui.feature.registration.email_reg.view

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.egorblagochinnov.validators.validateBy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmailRegBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.textChanges
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.common.utils.Animations
import spiral.bit.dev.dailymood.ui.common.view.LoadingDialogFragment
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegState

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EmailRegFragment : BaseFragment<RegState, RegEffect, FragmentEmailRegBinding>(
    FragmentEmailRegBinding::inflate
) {

    override val viewModel: EmailRegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        bindValidators()
        setUpListeners()
        setUpClicks()
    }

    private fun subscribeToObservers() = with(viewModel) {
        muxValidator.observe(viewLifecycleOwner) { validationSummaryResult ->
            if (validationSummaryResult.isValid) {
                showAnimateButton()
            }
        }
    }

    private fun showAnimateButton() = binding {
        buttonRegistration.isVisible = true
        buttonRegistration.startAnimation(Animations.showAnimationInToX())
    }

    private fun bindValidators() = binding {
        emailInputEditText.validateBy(viewLifecycleOwner, viewModel.textEmailValidator)
        userNameInputEditText.validateBy(viewLifecycleOwner, viewModel.textUsernameValidator)
        phoneInputEditText.validateBy(viewLifecycleOwner, viewModel.textPhoneValidator)
        passwordInputEditText.validateBy(viewLifecycleOwner, viewModel.textPasswordValidator)
    }

    private fun setUpListeners() = binding {
        emailInputEditText.textChanges()
            .debounce(DEBOUNCE_TIMEOUT)
            .map { it.toString() }
            .onEach { viewModel.textEmailSource.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        userNameInputEditText.textChanges()
            .debounce(DEBOUNCE_TIMEOUT)
            .map { it.toString() }
            .onEach { viewModel.textUsernameSource.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        phoneInputEditText.textChanges()
            .debounce(DEBOUNCE_TIMEOUT)
            .map { it.toString() }
            .onEach { viewModel.textPhoneSource.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        passwordInputEditText.textChanges()
            .debounce(DEBOUNCE_TIMEOUT)
            .map { it.toString() }
            .onEach { viewModel.textPasswordSource.value = it }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setUpClicks() = binding {
        buttonRegistration.setOnClickListener {
            viewModel.registrationByEmailAndPassword()
        }

        toggleLoginTextView.setOnClickListener {
            viewModel.toLogin()
        }
    }

    override fun handleSideEffect(sideEffect: RegEffect) = binding {
        when (sideEffect) {
            is RegEffect.Toast -> {
                root.toast(sideEffect.msg)
            }
            is RegEffect.NavigateToLogin -> {
                EmailRegFragmentDirections.toEmailAuthenticate().apply {
                    findNavController().navigate(this)
                }
            }
            is RegEffect.ToMainScreen -> {
                EmailRegFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RegEffect.NavigateToReg -> {
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
            is RegScreenState.ErrorAuthorized -> {
                root.toast(state.regScreenState.error.toString())
            }
            is RegScreenState.SuccessAuthorized -> {
                EmailRegFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RegScreenState.BootStrapState -> {
            }
        }
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT = 400L
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
    }
} 