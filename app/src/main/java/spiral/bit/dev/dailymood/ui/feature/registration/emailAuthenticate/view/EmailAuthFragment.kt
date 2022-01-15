package spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmailAuthBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.toast
import spiral.bit.dev.dailymood.ui.common.utils.Animations
import spiral.bit.dev.dailymood.ui.common.view.LoadingDialogFragment
import spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.models.mvi.EmailAuthEffect
import spiral.bit.dev.dailymood.ui.feature.registration.emailAuthenticate.models.mvi.EmailAuthState
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationType

@AndroidEntryPoint
class EmailAuthFragment : BaseFragment<EmailAuthState, EmailAuthEffect, FragmentEmailAuthBinding>(
    FragmentEmailAuthBinding::inflate
) {

    override val viewModel: EmailAuthViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        setUpViews()
    }

    private fun setUpViews() = binding {
        emailAuthenticateToolbar.titleTextView.text = getString(R.string.email_auth_label)
        buttonAuthenticate.startAnimation(Animations.showAnimationInToX())
    }

    private fun setUpClicks() = binding {
        buttonAuthenticate.setOnClickListener {
            viewModel.validateFields(
                emailInputEditText.text.toString(),
                passwordInputEditText.text.toString()
            )
        }

        toggleLoginTextView.setOnClickListener {
            viewModel.navigateToRegistration()
        }

        emailAuthenticateToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }
    }

    override fun renderState(state: EmailAuthState) = binding {
        val validationResult = state.validationResult
        if (validationResult != null) {
            when (validationResult.type) {
                ValidationType.EMAIL -> emailInputEditText.error = getString(validationResult.error)
                ValidationType.PASSWORD -> passwordInputEditText.error = getString(validationResult.error)
            }
        }
    }

    override fun handleSideEffect(sideEffect: EmailAuthEffect) = binding {
        when (sideEffect) {
            is EmailAuthEffect.ShowLoadingDialog -> {
                LoadingDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    LOADING_DIALOG_TAG
                )
            }
            is EmailAuthEffect.NavigateToMain -> {
                EmailAuthFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is EmailAuthEffect.NavigateToEmailRegistration -> {
                EmailAuthFragmentDirections.toEmailRegistration().apply {
                    findNavController().navigate(this)
                }
            }
            is EmailAuthEffect.ExceptionHappened -> {
                root.toast(sideEffect.throwable.toString())
            }
            is EmailAuthEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
        }
    }

    companion object {
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
    }
}