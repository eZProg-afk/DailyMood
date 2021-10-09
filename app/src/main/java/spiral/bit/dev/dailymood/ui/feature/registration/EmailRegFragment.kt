package spiral.bit.dev.dailymood.ui.feature.registration

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
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
import spiral.bit.dev.dailymood.ui.base.*
import spiral.bit.dev.dailymood.ui.common.utils.Animations
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegState

@FlowPreview
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class EmailRegFragment : BaseFragment<RegState, RegSideEffect, FragmentEmailRegBinding>(
    FragmentEmailRegBinding::inflate, R.layout.fragment_email_reg
) {

    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        bindValidators()
        setUpListeners()
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

    private fun handleSideEffects(effect: RegSideEffect) = binding {
        when (effect) {
            is RegSideEffect.Toast -> {
                root.toast(effect.msg)
            }
            RegSideEffect.NavigateToLogin -> {
                EmailRegFragmentDirections.toEmailAuthenticate().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.ToMainScreen -> {
                EmailRegFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.NavigateToReg -> {
            }
        }
    }

    companion object {
        private const val DEBOUNCE_TIMEOUT = 400L
    }
} 