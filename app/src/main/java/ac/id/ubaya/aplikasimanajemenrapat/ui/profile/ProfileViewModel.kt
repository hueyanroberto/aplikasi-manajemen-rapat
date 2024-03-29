package ac.id.ubaya.aplikasimanajemenrapat.ui.profile

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    fun getProfile(token: String): Flow<Resource<User>> = userUseCase.getProfile(token)
    fun getOtherProfile(token: String, userId: Int): Flow<Resource<User>> = userUseCase.getOtherProfile(token, userId)
}