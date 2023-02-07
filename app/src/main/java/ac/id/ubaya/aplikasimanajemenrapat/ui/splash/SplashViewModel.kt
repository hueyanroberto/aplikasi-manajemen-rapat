package ac.id.ubaya.aplikasimanajemenrapat.ui.splash

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.userPreference.UserPreferenceUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userPreferenceUseCase: UserPreferenceUseCase
): ViewModel() {

    fun getUser(): LiveData<User> = userPreferenceUseCase.getUser().asLiveData()

}