package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.*
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingRemoteDataSource @Inject constructor(private val apiService: ApiService){
    suspend fun getListMeeting(
        token: String,
        organizationId: Int
    ): Flow<ApiResponse<MeetingResponse>> {
        return flow {
            try {
                val meetingResponse = apiService.getListMeeting("Bearer $token", organizationId)
                if (meetingResponse.dataMeeting.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getListMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMemberToBeChosen (
        token: String,
        organizationId: Int
    ): Flow<ApiResponse<UserListResponse>> {
        return flow {
            try {
                val userResponses = apiService.getMemberToBeChosen("Bearer $token", organizationId)
                if (userResponses.userData.isNotEmpty()) {
                    emit(ApiResponse.Success(userResponses))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getMemberToBeChoose: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): Flow<ApiResponse<MeetingResponse>> {

        val jsonAgenda = JSONArray(agenda)
        val jsonParticipant = JSONArray(participant)
        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("start_time", startTime)
        jsonObject.put("end_time", endTime)
        jsonObject.put("location", location)
        jsonObject.put("description", description)
        jsonObject.put("organization_id", organizationId)
        jsonObject.put("agendas", jsonAgenda)
        jsonObject.put("participants", jsonParticipant)
        val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        return flow {
            try {
                val meetingResponse = apiService.createMeeting("Bearer $token", body)
                if (meetingResponse.dataMeeting.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "createMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMeetingDetail(token: String, meetingId: Int): Flow<ApiResponse<MeetingDetailResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.getMeetingDetail("Bearer $token", meetingId)
                if (meetingDetailResponse.meetingDetailData != null) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getMeetingDetail: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addAgenda(token: String, meetingId: Int, agendas: ArrayList<String>): Flow<ApiResponse<AgendaResponse>> {
        val jsonAgenda = JSONArray(agendas)
        val jsonObject = JSONObject()
        jsonObject.put("meeting_id", meetingId)
        jsonObject.put("agendas", jsonAgenda)
        val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        return flow {
            try {
                val agendaResponse = apiService.addAgenda("Bearer $token", body)
                if (agendaResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(agendaResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "addAgenda: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getListSuggestion(token: String, agendaId: Int): Flow<ApiResponse<SuggestionListResponse>> {
        return flow {
            try {
                val suggestionListResponse = apiService.getListSuggestion("Bearer $token", agendaId)
                if (suggestionListResponse.suggestionItemList.isNotEmpty()) {
                    emit(ApiResponse.Success(suggestionListResponse))
                }else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getListSuggestion: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addSuggestion(token: String, agendaId: Int, suggestion: String): Flow<ApiResponse<SuggestionResponse>> {
        return flow {
            try {
                val suggestionResponse = apiService.addSuggestion("Bearer $token", agendaId, suggestion)
                if (suggestionResponse.suggestionItem != null) {
                    emit(ApiResponse.Success(suggestionResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "addSuggestion: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun acceptSuggestion(token: String, suggestionId: Int) : Flow<ApiResponse<SuggestionResponse>> {
        return flow {
            try {
                val suggestionResponse = apiService.acceptSuggestion("Bearer $token", suggestionId)
                if (suggestionResponse.suggestionItem != null) {
                    emit(ApiResponse.Success(suggestionResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "acceptSuggestion: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteSuggestion(token: String, suggestionId: Int) : Flow<ApiResponse<SuggestionResponse>> {
        return flow {
            try {
                val suggestionResponse = apiService.deleteSuggestion("Bearer $token", suggestionId)
                if (suggestionResponse.suggestionItem != null) {
                    emit(ApiResponse.Success(suggestionResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "deleteSuggestion: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun startMeeting(token: String, meetingId: Int, date: Date) : Flow<ApiResponse<MeetingDetailResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.startMeeting("Bearer $token", meetingId, date)
                if (meetingDetailResponse.meetingDetailData != null) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "startMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun joinMeeting(token: String, meetingId: Int, meetingCode: String, date: Date) : Flow<ApiResponse<MeetingDetailResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.joinMeeting("Bearer $token", meetingId, meetingCode, date)
                if (meetingDetailResponse.meetingDetailData != null) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "joinMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun endMeeting(token: String, meetingId: Int, date: Date, meetingNote: String) : Flow<ApiResponse<MeetingDetailResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.endMeeting("Bearer $token", meetingId, date, meetingNote)
                if (meetingDetailResponse.meetingDetailData != null) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "endMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMinutes(token: String, meetingId: Int): Flow<ApiResponse<AgendaResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.getMinutes("Bearer $token", meetingId)
                if (meetingDetailResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getMinutes: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getMeetingPointLog(token: String, meetingId: Int): Flow<ApiResponse<MeetingPointResponse>> {
        return flow {
            try {
                val taskResponse = apiService.getMeetingPointLog("Bearer $token", meetingId)
                if (taskResponse.data.isNotEmpty()) {
                    emit(ApiResponse.Success(taskResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "updateTaskStatus: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun editAgenda(token: String, agendaId: Int, task: String): Flow<ApiResponse<AgendaResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.editAgenda("Bearer $token", agendaId, task)
                if (meetingDetailResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "editAgenda: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun deleteAgenda(token: String, agendaId: Int): Flow<ApiResponse<AgendaResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.deleteAgenda("Bearer $token", agendaId)
                if (meetingDetailResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "deleteAgenda: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun updateAgendaStatus(token: String, agendaId: Int): Flow<ApiResponse<AgendaResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.updateAgendaStatus("Bearer $token", agendaId)
                if (meetingDetailResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "updateAgendaStatus: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getAgendaDetail(token: String, agendaId: Int): Flow<ApiResponse<AgendaResponse>> {
        return flow {
            try {
                val meetingDetailResponse = apiService.getAgendaDetail("Bearer $token", agendaId)
                if (meetingDetailResponse.agendaData.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingDetailResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getAgendaDetail: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun editMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, meetingId: Int
    ): Flow<ApiResponse<MeetingResponse>> {

        val jsonObject = JSONObject()
        jsonObject.put("title", title)
        jsonObject.put("start_time", startTime)
        jsonObject.put("end_time", endTime)
        jsonObject.put("location", location)
        jsonObject.put("description", description)
        jsonObject.put("meeting_id", meetingId)
        val body = jsonObject.toString().toRequestBody("application/json".toMediaTypeOrNull())

        return flow {
            try {
                val meetingResponse = apiService.editMeeting("Bearer $token", body)
                if (meetingResponse.dataMeeting.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "editMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deleteMeeting(
        token: String, meetingId: Int
    ): Flow<ApiResponse<MeetingResponse>> {
        return flow {
            try {
                val meetingResponse = apiService.deleteMeeting("Bearer $token", meetingId)
                if (meetingResponse.dataMeeting.isNotEmpty()) {
                    emit(ApiResponse.Success(meetingResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "deleteMeeting: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun uploadFile(token: String, files: List<MultipartBody.Part>, meetingId: RequestBody): Flow<ApiResponse<AttachmentResponse>> {
        return flow {
            try {
                val attachmentResponse = apiService.uploadAttachment("Bearer $token", files, meetingId)
                if (attachmentResponse.listAttachment.isNotEmpty()) {
                    emit(ApiResponse.Success(attachmentResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "uploadFile: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getListTask(token: String, meetingId: Int): Flow<ApiResponse<TaskListResponse>> {
        return flow {
            try {
                val taskResponse = apiService.getListTask("Bearer $token", meetingId)
                if (taskResponse.listTask.isNotEmpty()) {
                    emit(ApiResponse.Success(taskResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "getListTask: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addTask(token: String, meetingId: Int, userId: Int, title: String, description: String, deadline: Date): Flow<ApiResponse<TaskResponse>> {
        return flow {
            try {
                val taskResponse = apiService.addTask("Bearer $token", meetingId, userId, title, description, deadline)
                if (taskResponse.task != null) {
                    emit(ApiResponse.Success(taskResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "addTask: $e")
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun updateTaskStatus(token: String, taskId: Int, date: Date): Flow<ApiResponse<TaskResponse>> {
        return flow {
            try {
                val taskResponse = apiService.updateTaskStatus("Bearer $token", taskId, date)
                if (taskResponse.task != null) {
                    emit(ApiResponse.Success(taskResponse))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("MeetingRDataSource", "updateTaskStatus: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}