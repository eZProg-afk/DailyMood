package spiral.bit.dev.dailymood.ui.feature.user.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import spiral.bit.dev.dailymood.ui.feature.user.managers.UserManager
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userManager: UserManager
) : ViewModel() {

    val user = MutableLiveData(userManager.getUserLiveData())

    fun updateEmail(email: String) = viewModelScope.launch {
        userManager.updateEmail(email)
    }

    fun updatePassword(password: String)  = viewModelScope.launch {
        userManager.updatePassword(password)
    }

    fun updateUserNameAndPhoto(userName: String, photo: String) = viewModelScope.launch {
        userManager.updateUserNameAndPhoto(userName, photo)
    }
}