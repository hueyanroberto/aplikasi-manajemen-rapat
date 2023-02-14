package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JoinOrganizationViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase,
    private val userPreferenceUseCase: UserPreferenceUseCase
) : ViewModel() {
    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

    fun joinOrganization(userId: Int, organizationCode: String): LiveData<Resource<Organization?>> {
        return organizationUseCase.joinOrganization(userId, organizationCode).asLiveData()
    }
}