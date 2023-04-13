package ac.id.ubaya.aplikasimanajemenrapat.core.data.repository

import ac.id.ubaya.aplikasimanajemenrapat.core.data.Resource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.datasource.MeetingRemoteDataSource
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.*
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.repository.IMeetingRepository
import ac.id.ubaya.aplikasimanajemenrapat.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.ArrayList

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

    override fun getMeetingDetail(token: String, meetingId: Int): Flow<Resource<Meeting?>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.getMeetingDetail(token, meetingId).first()) {
                is ApiResponse.Success -> {
                    val meetingData = apiResponse.data.meetingDetailData
                    emit(Resource.Success(DataMapper.meetingDataResponseToModel(meetingData!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(null))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun addAgenda(
        token: String,
        meetingId: Int,
        agendas: ArrayList<String>
    ): Flow<Resource<List<Agenda>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.addAgenda(token, meetingId, agendas).first()) {
                is ApiResponse.Success -> {
                    val agendaData = apiResponse.data.agendaData
                    emit(Resource.Success(DataMapper.agendaResponseToModel(agendaData)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Agenda>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getListSuggestion(token: String, agendaId: Int): Flow<Resource<List<Suggestion>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.getListSuggestion(token, agendaId).first()) {
                is ApiResponse.Success -> {
                    val suggestionList = apiResponse.data.suggestionItemList
                    emit(Resource.Success(DataMapper.suggestionResponseToModel(suggestionList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Suggestion>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun addSuggestion(
        token: String,
        agendaId: Int,
        suggestion: String
    ): Flow<Resource<Suggestion>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.addSuggestion(token, agendaId, suggestion).first()) {
                is ApiResponse.Success -> {
                    val suggestionItem = apiResponse.data.suggestionItem!!
                    emit(Resource.Success(DataMapper.suggestionResponseToModel(suggestionItem)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("Unauthorized"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun acceptSuggestion(token: String, suggestionId: Int): Flow<Resource<Suggestion>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.acceptSuggestion(token, suggestionId).first()) {
                is ApiResponse.Success -> {
                    val suggestionItem = apiResponse.data.suggestionItem!!
                    emit(Resource.Success(DataMapper.suggestionResponseToModel(suggestionItem)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("Unauthorized"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun startMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.startMeeting(token, meetingId, date).first()) {
                is ApiResponse.Success -> {
                    val meetingData = apiResponse.data.meetingDetailData
                    emit(Resource.Success(DataMapper.meetingDataResponseToModel(meetingData!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun joinMeeting(token: String, meetingId: Int, meetingCode: String, date: Date): Flow<Resource<Meeting>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.joinMeeting(token, meetingId, meetingCode, date).first()) {
                is ApiResponse.Success -> {
                    val meetingData = apiResponse.data.meetingDetailData
                    emit(Resource.Success(DataMapper.meetingDataResponseToModel(meetingData!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("wrong code"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun endMeeting(token: String, meetingId: Int, date: Date): Flow<Resource<Meeting>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.endMeeting(token, meetingId, date).first()) {
                is ApiResponse.Success -> {
                    val meetingData = apiResponse.data.meetingDetailData
                    emit(Resource.Success(DataMapper.meetingDataResponseToModel(meetingData!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getMinutes(token: String, meetingId: Int): Flow<Resource<List<Agenda>>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.getMinutes(token, meetingId).first()) {
                is ApiResponse.Success -> {
                    val agendaData = apiResponse.data.agendaData
                    emit(Resource.Success(DataMapper.agendaResponseToModel(agendaData)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Agenda>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun editAgenda(token: String, agendaId: Int, task: String): Flow<Resource<Agenda>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.editAgenda(token, agendaId, task).first()) {
                is ApiResponse.Success -> {
                    val agendaData = apiResponse.data.agendaData
                    emit(Resource.Success(DataMapper.agendaResponseToModel(agendaData)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun deleteAgenda(token: String, agendaId: Int): Flow<Resource<Agenda>> {
        return flow {
            emit(Resource.Loading())
            when (val apiResponse = meetingRemoteDataSource.deleteAgenda(token, agendaId).first()) {
                is ApiResponse.Success -> {
                    val agendaData = apiResponse.data.agendaData
                    emit(Resource.Success(DataMapper.agendaResponseToModel(agendaData)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun editMeeting(
        token: String, title: String, startTime: Date, endTime: Date,
        location: String, description: String, meetingId: Int
    ): Flow<Resource<Meeting>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.editMeeting(
                    token, title, startTime, endTime, location,
                    description, meetingId
                ).first()
            ) {
                is ApiResponse.Success -> {
                    val meetingList = apiResponse.data.dataMeeting
                    emit(Resource.Success(DataMapper.meetingResponseToModel(meetingList)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun deleteMeeting(token: String, meetingId: Int): Flow<Resource<Meeting>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.deleteMeeting(token, meetingId).first()) {
                is ApiResponse.Success -> {
                    val meetingList = apiResponse.data.dataMeeting
                    emit(Resource.Success(DataMapper.meetingResponseToModel(meetingList)[0]))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthenticated"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun uploadFile(
        token: String,
        files: List<MultipartBody.Part>,
        meetingId: RequestBody
    ): Flow<Resource<List<Attachment>>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.uploadFile(token, files, meetingId).first()) {
                is ApiResponse.Success -> {
                    val attachmentList = apiResponse.data.listAttachment
                    emit(Resource.Success(DataMapper.attachmentResponseToModel(attachmentList)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Attachment>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun getListTask(token: String, meetingId: Int): Flow<Resource<List<Task>>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.getListTask(token, meetingId).first()) {
                is ApiResponse.Success -> {
                    val task = apiResponse.data.listTask
                    emit(Resource.Success(DataMapper.taskResponseToModel(task)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Success(listOf<Task>()))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun addTask(
        token: String,
        meetingId: Int,
        userId: Int,
        title: String,
        description: String,
        deadline: Date
    ): Flow<Resource<Task>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.addTask(token, meetingId, userId, title, description, deadline).first()) {
                is ApiResponse.Success -> {
                    val task = apiResponse.data.task
                    emit(Resource.Success(DataMapper.taskResponseToModel(task!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthorized"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }

    override fun updateTaskStatus(token: String, taskId: Int, date: Date): Flow<Resource<Task>> {
        return flow {
            emit(Resource.Loading())
            when (
                val apiResponse = meetingRemoteDataSource.updateTaskStatus(token, taskId, date).first()) {
                is ApiResponse.Success -> {
                    val task = apiResponse.data.task
                    emit(Resource.Success(DataMapper.taskResponseToModel(task!!)))
                }
                is ApiResponse.Empty -> {
                    emit(Resource.Error("unauthorized"))
                }
                is ApiResponse.Error -> {
                    emit(Resource.Error(apiResponse.errorMessage))
                }
            }
        }
    }
}