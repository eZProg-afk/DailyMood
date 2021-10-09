package spiral.bit.dev.dailymood.ui.feature.registration

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
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.feature.registration.common.RegState
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<RegState, RegSideEffect, FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate, R.layout.fragment_registration
) {

    @Inject
    lateinit var callbackManager: CallbackManager
    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClicks()
        viewModel.run {
            checkIsAlreadyAuthorized()
            observe(
                viewLifecycleOwner,
                sideEffect = ::handleSideEffects
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

    private fun handleSideEffects(effect: RegSideEffect) = binding {
        when (effect) {
            is RegSideEffect.Toast -> root.toast(effect.msg)
            RegSideEffect.NavigateToLogin -> {
                RegistrationFragmentDirections.toEmailAuthenticate().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.ToMainScreen -> {
                RegistrationFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            RegSideEffect.NavigateToReg -> {
                RegistrationFragmentDirections.toEmailRegistration().apply {
                    findNavController().navigate(this)
                }
            }
        }
    }

    //Because Facebook didn't update their authorization, I'm using deprecate:(
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}