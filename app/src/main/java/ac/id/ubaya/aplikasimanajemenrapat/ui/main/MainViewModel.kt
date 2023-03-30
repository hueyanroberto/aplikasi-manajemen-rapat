package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferenceUseCase: UserPreferenceUseCase,
    private val organizationUseCase: OrganizationUseCase,
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

    fun addFirebaseToken(token: String, firebaseToken: String) {
        viewModelScope.launch {
            userUseCase.addFirebaseToken(token, firebaseToken)
        }
    }

    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

    fun getListOrganization(token: String): LiveData<Resource<List<Organization>>> =
        organizationUseCase.getListOrganization(token).asLiveData()

    fun changeGetUserStatus() {
        _isUserGet.value = true
    }
}