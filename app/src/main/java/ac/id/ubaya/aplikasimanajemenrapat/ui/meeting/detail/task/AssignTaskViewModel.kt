package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Participant
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AssignTaskViewModel: ViewModel() {
    private val _listUser = MutableLiveData<List<Participant>>()
    val listUser: LiveData<List<Participant>> get() = _listUser

    private val listUserParent = mutableListOf<Participant>()
    private var listUserSearch = listOf<Participant>()

    fun submitList(listParticipant: List<Participant>) {
        listUserParent.addAll(listParticipant)
        _listUser.value = listUserParent
    }

    fun searchParticipant(query: String) {
        listUserSearch = listUserParent.filter {
            it.name.contains(query, ignoreCase = true)
        }
        _listUser.value = listUserSearch
    }
}