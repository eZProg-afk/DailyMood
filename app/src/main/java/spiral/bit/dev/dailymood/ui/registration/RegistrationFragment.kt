package spiral.bit.dev.dailymood.ui.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentRegistrationBinding
import spiral.bit.dev.dailymood.helpers.EMAIL
import spiral.bit.dev.dailymood.helpers.observe
import spiral.bit.dev.dailymood.helpers.showToast
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.registration.common.RegSideEffect
import spiral.bit.dev.dailymood.ui.registration.common.RegState
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<RegState, RegSideEffect>(R.layout.fragment_registration) {

    @Inject lateinit var callbackManager: CallbackManager
    private val regBinding: FragmentRegistrationBinding by viewBinding()
    override val viewModel: RegViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        viewModel.run {
            checkIsAlreadyAuthorized()
            observe(
                viewLifecycleOwner,
                sideEffect = ::handleSideEffects
            )
        }
    }

    private fun setUpViews() = with(regBinding) {
        regToggleLoginTextview.setOnClickListener {
            viewModel.toLogin()
        }
        emailRegBtn.setOnClickListener {
            viewModel.toRegistration()
        }
        fbRegBtn.setReadPermissions(EMAIL)
        fbRegBtn.fragment = this@RegistrationFragment
        fbRegBtn.registerCallback(
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

    private fun handleSideEffects(effect: RegSideEffect) {
        when (effect) {
            is RegSideEffect.Toast -> requireContext().showToast(effect.msg)
            RegSideEffect.NavigateToLogin -> {
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailAuthFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.ToMainScreen -> {
                RegistrationFragmentDirections.actionRegistrationFragmentToMainFragment()
                    .apply {
                        findNavController().navigate(this)
                    }
            }
            RegSideEffect.NavigateToReg -> {
                RegistrationFragmentDirections.actionRegistrationFragmentToEmailRegFragment()
                    .apply {
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