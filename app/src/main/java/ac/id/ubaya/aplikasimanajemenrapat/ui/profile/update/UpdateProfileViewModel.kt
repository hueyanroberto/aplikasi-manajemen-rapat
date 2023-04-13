package ac.id.ubaya.aplikasimanajemenrapat.ui.profile.update

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.user.UserUseCase
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): ViewModel() {
    fun updateProfile(token: String, name: String): Flow<Resource<User>> {
        return userUseCase.updateProfile(token, name)
    }
}