package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.MeetingRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IMeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MeetingRepository @Inject constructor(
    private val meetingRemoteDataSource: MeetingRemoteDataSource
): IMeetingRepository {
    override fun getListOrganization(
        token: String,
        organizationId: Int
    ): Flow<Resource<List<Meeting>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.getListMeeting(token, organizationId).first()) {
                is ApiResponse.Success -> {
                    val meetingList = apiResponse.data.dataMeeting
                    emit(Resource.Success(DataMapper.meetingResponseToModel(meetingList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Meeting>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getUserToBeChosen(token: String, organizationId: Int): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.getMemberToBeChosen(token, organizationId).first()) {
                is ApiResponse.Success -> {
                    val userList = apiResponse.data.userData
                    emit(Resource.Success(DataMapper.userResponseToModel(userList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<User>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun createMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, organizationId: Int,
        participant: List<Int>, agenda: List<String>
    ): Flow<Resource<List<Meeting>>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.createMeeting(
                    token, title, startTime, endTime, location,
                    description, organizationId, participant, agenda
                ).first()
            ) {
                is ApiResponse.Success -> {
                    val meetingList = apiResponse.data.dataMeeting
                    emit(Resource.Success(DataMapper.meetingResponseToModel(meetingList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Meeting>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }
}