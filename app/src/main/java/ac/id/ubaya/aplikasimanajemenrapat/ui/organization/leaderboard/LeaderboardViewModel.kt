package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.leaderboard

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.LeaderboardDetail
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase
): ViewModel() {
    fun getLeaderboard(token: String, organizationId: Int): LiveData<Resource<LeaderboardDetail>> {
        return organizationUseCase.getLeaderboard(token, organizationId).asLiveData()
    }
}