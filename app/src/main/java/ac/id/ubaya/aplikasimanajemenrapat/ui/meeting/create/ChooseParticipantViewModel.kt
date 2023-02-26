package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.create

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChooseParticipantViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
) : ViewModel() {

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> get() = _listUser

    private val listUserParent = mutableListOf<User>()
    private var listUserSearch = listOf<User>()

    fun getUserToBeChosen(token: String, organizationId: Int): LiveData<Resource<List<User>>> {
        return meetingUseCase.getUserToBeChosen(token, organizationId).asLiveData()
    }

    fun updateListUser(listUser: List<User>) {
        listUserParent.addAll(listUser)
        listUserSearch = listUserParent
        _listUser.value = listUserParent
    }

    fun searchUser(query: String) {
        listUserSearch = listUserParent.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _listUser.value = listUserSearch
    }
}