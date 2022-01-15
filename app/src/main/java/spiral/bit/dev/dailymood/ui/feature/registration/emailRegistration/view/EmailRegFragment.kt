package spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.view

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentEmailRegBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.toast
import spiral.bit.dev.dailymood.ui.common.utils.Animations
import spiral.bit.dev.dailymood.ui.common.view.LoadingDialogFragment
import spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.models.mvi.EmailRegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.emailRegistration.models.mvi.EmailRegState
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.ValidationType

@AndroidEntryPoint
class EmailRegFragment : BaseFragment<EmailRegState, EmailRegEffect, FragmentEmailRegBinding>(
    FragmentEmailRegBinding::inflate
) {

    override val viewModel: EmailRegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpClicks()
    }

    private fun setUpViews() = binding {
        emailRegistrationToolbar.titleTextView.text = getString(R.string.email_reg_label)
        buttonRegistration.startAnimation(Animations.showAnimationInToX())
    }

    private fun setUpClicks() = binding {
        buttonRegistration.setOnClickListener {
            viewModel.validateFields(
                emailInputEditText.text.toString(),
                passwordInputEditText.text.toString()
            )
        }

        emailRegistrationToolbar.iconBackImageView.setOnClickListener {
            viewModel.navigateBack()
        }

        toggleLoginTextView.setOnClickListener {
            viewModel.navigateToLogin()
        }
    }

    override fun handleSideEffect(sideEffect: EmailRegEffect) = binding {
        when (sideEffect) {
            is EmailRegEffect.ShowLoadingDialog -> {
                LoadingDialogFragment().show(
                    requireActivity().supportFragmentManager,
                    LOADING_DIALOG_TAG
                )
            }
            is EmailRegEffect.NavigateToEmailAuth -> {
                EmailRegFragmentDirections.toEmailAuthenticate().apply {
                    findNavController().navigate(this)
                }
            }
            is EmailRegEffect.NavigateToMain -> {
                EmailRegFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is EmailRegEffect.NavigateBack -> {
                findNavController().popBackStack()
            }
            is EmailRegEffect.ExceptionHappened -> {
                root.toast(sideEffect.throwable.toString())
            }
        }
    }

    override fun renderState(state: EmailRegState) = binding {
        val validationResult = state.validationResult
        if (validationResult != null) {
            when (validationResult.type) {
                ValidationType.EMAIL -> emailInputEditText.error = getString(validationResult.error)
                ValidationType.PASSWORD -> passwordInputEditText.error = getString(validationResult.error)
            }
        }
    }

    companion object {
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
    }
} 