package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.MeetingResponse
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
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
                Log.e("MeetingRDataSource", "createOrganization: $e")
            }
        }.flowOn(Dispatchers.IO)
    }
}