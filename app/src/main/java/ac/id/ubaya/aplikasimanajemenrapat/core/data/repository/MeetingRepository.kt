package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.MeetingRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IMeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
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
}