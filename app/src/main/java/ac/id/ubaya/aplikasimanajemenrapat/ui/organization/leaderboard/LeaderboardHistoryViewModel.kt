package ac.id.ubaya.aplikasimanajemenrapat.ui.organization.leaderboard

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.LeaderboardDetail
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.organization.OrganizationUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LeaderboardHistoryViewModel @Inject constructor(
    private val organizationUseCase: OrganizationUseCase
): ViewModel() {
    fun getLeaderboardHistory(token: String, organizationId: Int, period: Int): Flow<Resource<LeaderboardDetail>> {
        return organizationUseCase.getLeaderboardHistory(token, organizationId, period)
    }
}