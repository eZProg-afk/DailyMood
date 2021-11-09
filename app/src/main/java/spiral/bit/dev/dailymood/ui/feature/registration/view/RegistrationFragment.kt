package spiral.bit.dev.dailymood.ui.feature.registration.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.appevents.UserDataStore.EMAIL
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentRegistrationBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.observe
import spiral.bit.dev.dailymood.ui.base.toast
import spiral.bit.dev.dailymood.ui.common.view.LoadingDialogFragment
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegEffect
import spiral.bit.dev.dailymood.ui.feature.registration.models.mvi.RegState
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<RegState, RegEffect, FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate
) {

    @Inject
    lateinit var callbackManager: CallbackManager
    override val viewModel: RegistrationViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        viewModel.run {
            checkIsAlreadyAuthorized()
            observe(
                viewLifecycleOwner,
                sideEffect = ::handleSideEffect
            )
        }
    }

    private fun setUpClicks() = binding {
        toggleLoginTextView.setOnClickListener {
            viewModel.toLogin()
        }

        emailRegistrationButton.setOnClickListener {
            viewModel.toRegistration()
        }

        facebookRegistrationButton.setPermissions(EMAIL)
        facebookRegistrationButton.fragment = this@RegistrationFragment
        facebookRegistrationButton.registerCallback(
            callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) {
                    loginResult?.accessToken?.let {
                        viewModel.handleFBToken(it)
                    } ?: viewModel.errorRegisterCallback()
                }

                override fun onError(exception: FacebookException) {
                    viewModel.errorRegisterCallback()
                }

                override fun onCancel() {
                }
            })
    }

    //Because Facebook didn't update their authorization, I'm using deprecate:(
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun handleSideEffect(sideEffect: RegEffect) = binding {
        when (sideEffect) {
            is RegEffect.Toast -> root.toast(sideEffect.msg)
            RegEffect.NavigateToLogin -> {
                RegistrationFragmentDirections.toEmailAuthenticate()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is RegEffect.ToMainScreen -> {
                RegistrationFragmentDirections.toMain()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            is RegEffect.NavigateToReg -> {
                RegistrationFragmentDirections.toEmailRegistration()
                    .apply {
                        findNavController().navigate(this)
                    }
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
                RegistrationFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RegScreenState.ErrorAuthorized -> {
                LoadingDialogFragment().dismiss()
                root.toast(state.regScreenState.error.toString())
            }
            is RegScreenState.BootStrapState -> {
            }
            //TODO СДЕЛАТЬ ПОАДЕКВАТНЕЕ ДИСМИСС ЛОАДИНГА
        }
    }

    companion object {
        private const val LOADING_DIALOG_TAG = "LOADING_DIALOG_TAG"
    }
}