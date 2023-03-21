package ac.id.ubaya.aplikasimanajemenrapat.ui.organization

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class EditOrganizationViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase
): ViewModel() {

    fun updateOrganizationProfilePic(token: String, organizationId: Int, profilePic: String): Flow<Resource<Organization>> {
        return organizationUseCase.updateOrganizationProfile(token, organizationId, profilePic)
    }

    fun editOrganization(token: String, organizationId: Int, name: String, desc: String, duration: Int): Flow<Resource<Organization>> {
        return organizationUseCase.editOrganization(token, organizationId, name, desc, duration)
    }
}