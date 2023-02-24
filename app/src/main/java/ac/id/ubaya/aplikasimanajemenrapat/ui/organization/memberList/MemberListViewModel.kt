package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.memberList

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MemberListViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase
): ViewModel() {
    fun getOrganizationMember(token: String, organizationId: Int): LiveData<Resource<List<User>>> =
        organizationUseCase.getOrganizationMembers(token, organizationId).asLiveData()
}