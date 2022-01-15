package spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.view

import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import dagger.hilt.android.AndroidEntryPoint
import spiral.bit.dev.dailymood.R
import spiral.bit.dev.dailymood.databinding.FragmentRegistrationBinding
import spiral.bit.dev.dailymood.ui.base.BaseFragment
import spiral.bit.dev.dailymood.ui.base.binding
import spiral.bit.dev.dailymood.ui.base.extensions.toast
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.mvi.RegistrationTypeEffect
import spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models.mvi.RegistrationTypeState
import spiral.bit.dev.dailymood.ui.feature.user.models.User
import spiral.bit.dev.dailymood.ui.feature.user.view.UserViewModel
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationTypeFragment : BaseFragment<RegistrationTypeState, RegistrationTypeEffect, FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate
) {

    override val viewModel: RegistrationTypeViewModel by hiltNavGraphViewModels(R.id.nav_graph)
    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.nav_graph)

    @Inject
    lateinit var googleSignInOptions: GoogleSignInOptions

    @Inject
    lateinit var callbackManager: CallbackManager

    @Inject
    lateinit var loginManager: LoginManager

    private val googleCallback = registerForActivityResult(StartActivityForResult()) { activityResult ->
        viewModel.handleGoogleSignInIntent(activityResult.data)
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpClicks()
    }

    private fun setUpViews() = binding {
        googleSignInButton.setSize(SignInButton.SIZE_WIDE)
    }

    private fun setUpClicks() = binding {
        facebookRegistrationButton.setOnClickListener {
            viewModel.registerFacebookCallback(facebookRegistrationButton)
            loginManager.logInWithReadPermissions(
                this@RegistrationTypeFragment,
                callbackManager,
                permissions
            )
        }

        googleSignInButton.setOnClickListener {
            googleCallback.launch(googleSignInClient.signInIntent)
        }

        toggleLoginTextView.setOnClickListener {
            viewModel.navigateToEmailAuthenticate()
        }

        emailRegistrationButton.setOnClickListener {
            viewModel.navigateToEmailRegistration()
        }
    }

    override fun handleSideEffect(sideEffect: RegistrationTypeEffect) = binding {
        when (sideEffect) {
            is RegistrationTypeEffect.NavigateToEmailAuth -> {
                RegistrationTypeFragmentDirections.toEmailAuthenticate().apply {
                    findNavController().navigate(this)
                }
            }
            is RegistrationTypeEffect.NavigateToRegistration -> {
                RegistrationTypeFragmentDirections.toEmailRegistration().apply {
                    findNavController().navigate(this)
                }
            }
            is RegistrationTypeEffect.NavigateToMain -> {
                RegistrationTypeFragmentDirections.toMain().apply {
                    findNavController().navigate(this)
                }
            }
            is RegistrationTypeEffect.ExceptionHappened -> {
                root.toast(sideEffect.throwable.toString())
            }
        }
    }

    override fun renderState(state: RegistrationTypeState) = binding {
        state.user?.let { firebaseUser ->
            val user = User(userName = firebaseUser.displayName, email = firebaseUser.email, uid = firebaseUser.uid)
            userViewModel.user.value?.value = user
            viewModel.navigateToMain()
        }
    }

    companion object {
        private val permissions = listOf("public_profile", "email")
    }
}