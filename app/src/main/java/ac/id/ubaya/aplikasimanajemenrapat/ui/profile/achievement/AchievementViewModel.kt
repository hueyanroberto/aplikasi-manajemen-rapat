package ac.id.ubaya.aplikasimanajemenrapat.ui.profile.achievement

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AchievementViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    fun getAchievement(token: String) = userUseCase.getAchievements(token)
}