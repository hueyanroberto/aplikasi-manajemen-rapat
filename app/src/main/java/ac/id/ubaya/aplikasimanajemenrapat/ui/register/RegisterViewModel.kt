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
class RegisterViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val userPreferenceUseCase: UserPreferenceUseCase
): ViewModel() {
    fun register(email: String, password: String): LiveData<Resource<User?>> =
        userUseCase.register(email, password).asLiveData()

    fun saveUserData(user: User) {
        viewModelScope.launch {
            userPreferenceUseCase.saveUser(user)
        }
    }

}