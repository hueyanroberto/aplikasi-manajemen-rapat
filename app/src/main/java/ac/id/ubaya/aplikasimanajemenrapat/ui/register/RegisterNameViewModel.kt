package ac.id.ubaya.aplikasimanajemenrapat.ui.register

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterNameViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val userPreferenceUseCase: UserPreferenceUseCase
): ViewModel() {
    fun registerName(token: String, userId: Int, name: String, profilePic: String): LiveData<Resource<User?>> =
        userUseCase.registerNameAndProfilePic(token, userId, name, profilePic).asLiveData()

    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

    fun saveUserData(user: User) {
        viewModelScope.launch {
            userPreferenceUseCase.saveName(user)
        }
    }
}