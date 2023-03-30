package ac.id.ubaya.aplikasimanajemenrapat.ui.meeting.detail.task

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Task
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.usecase.meeting.MeetingUseCase
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val meetingUseCase: MeetingUseCase
): ViewModel() {
    private val _task = MutableLiveData<Resource<List<Task>>>()
    val task: LiveData<Resource<List<Task>>> get() = _task

    fun getListTask(token: String, meetingId: Int) {
        viewModelScope.launch {
            meetingUseCase.getListTask(token, meetingId).collect {
                _task.value =  it
            }
        }
    }

    fun addTask(token: String, meetingId: Int, userId: Int, title: String, description: String, deadline: Date): Flow<Resource<Task>> {
        return meetingUseCase.addTask(token, meetingId, userId, title, description, deadline)
    }
}