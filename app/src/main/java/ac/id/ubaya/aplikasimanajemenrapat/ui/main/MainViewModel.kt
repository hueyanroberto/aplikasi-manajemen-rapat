package ac.id.ubaya.aplikasimanajemenrapat.ui.main

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPreferenceUseCase: UserPreferenceUseCase,
    private val organizationUseCase: OrganizationUseCase
): ViewModel() {
    fun logOut() {
        viewModelScope.launch {
            userPreferenceUseCase.logout()
        }
    }

    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

    fun getListOrganization(userId: Int): LiveData<Resource<List<Organization>>> =
        organizationUseCase.getListOrganization(userId).asLiveData()
}