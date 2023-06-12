package ac.id.ubaya.aplikasimanajemenrapat.ui

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userPreferenceUseCase: UserPreferenceUseCase,
    private val userUseCase: UserUseCase
): ViewModel() {
    private val _isUserGet = MutableLiveData<Boolean>()
    val isUserGet: LiveData<Boolean> get() = _isUserGet

    init {
        _isUserGet.value = false
    }

    fun logOut(token: String) {
        viewModelScope.launch {
            userPreferenceUseCase.logout()
            userUseCase.logout(token)
        }
    }

    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

    fun changeGetUserStatus() {
        _isUserGet.value = true
    }

    fun changeName(name: String) {
        viewModelScope.launch {
            userPreferenceUseCase.updateName(name)
        }
    }

    fun changeProfile(profile: String) {
        viewModelScope.launch {
            userPreferenceUseCase.updateProfile(profile)
        }
    }
}