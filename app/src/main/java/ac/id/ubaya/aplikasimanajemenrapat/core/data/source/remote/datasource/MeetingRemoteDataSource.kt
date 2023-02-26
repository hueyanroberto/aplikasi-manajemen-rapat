package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.MeetingResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserListResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
        val body = JSONObject()
        body.put("title", title)
        body.put("start_time", startTime)
        body.put("end_time", endTime)
        body.put("location", location)
        body.put("description", description)
        body.put("organization_id", organizationId)
        body.put("agendas", jsonAgenda)
        body.put("participants", jsonParticipant)

        return flow {
            try {
                val meetingResponse = apiService.createMeeting(token, body)
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
}