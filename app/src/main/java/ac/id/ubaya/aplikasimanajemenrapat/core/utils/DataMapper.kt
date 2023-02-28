package ac.id.ubaya.aplikasimanajemenrapat.core.utils

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity.OrganizationEntity
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.*
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.*
import java.text.SimpleDateFormat
import java.util.*

object DataMapper {
    fun userResponseToModel(userData: UserData): User {
        return User(
            id = userData.id,
            email = userData.email,
            name = userData.name,
            profilePic = userData.profilePic,
            exp = userData.exp,
            levelId = userData.levelId,
            token = userData.token
        )
    }

    fun userResponseToModel(listUserResponse: List<UserData>): List<User> =
        listUserResponse.map { userData ->
            User(
                id = userData.id,
                email = userData.email,
                name = userData.name,
                profilePic = userData.profilePic,
                exp = userData.exp,
                levelId = userData.levelId,
                role = userData.role?.let { roleResponseToModel(it) }
            )
        }

    fun roleResponseToModel(roleResponse: RoleResponse): Role =
        Role(roleResponse.name, roleResponse.id)

    fun organizationResponseToEntity(listOrganizationData: List<OrganizationData>): List<OrganizationEntity> {
        return listOrganizationData.map {
            val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            OrganizationEntity(
                id = it.id,
                name = it.name,
                code = it.code,
                description = it.description,
                profilePic = it.profilePicture,
                leaderboardStart = it.leaderboardStart?.let { date -> format.format(date) },
                leaderboardEnd = it.leaderboardEnd?.let { date -> format.format(date) }
            )
        }
    }

    fun organizationResponseToModel(listOrganizationData: List<OrganizationData>): List<Organization> {
        return listOrganizationData.map {
            val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            Organization(
                id = it.id,
                name = it.name,
                code = it.code,
                description = it.description,
                profilePicture = it.profilePicture,
                leaderboardStart = it.leaderboardStart?.let { date -> format.format(date) },
                leaderboardEnd = it.leaderboardEnd?.let { date -> format.format(date) }
            )
        }
    }

    fun organizationEntityToModel(organizationEntity: List<OrganizationEntity>): List<Organization> {
        return organizationEntity.map {
            Organization(
                id = it.id,
                name = it.name,
                code = it.code,
                description = it.description,
                profilePicture = it.profilePic,
                leaderboardStart = it.leaderboardStart,
                leaderboardEnd = it.leaderboardEnd
            )
        }
    }

    fun meetingResponseToModel(meetingResponse: List<MeetingItem>): List<Meeting> {
        return meetingResponse.map {
            Meeting(
                id = it.id,
                title = it.title,
                startTime = it.startTime,
                endTime = it.endTime,
                location = it.location,
                description = it.description,
                code = it.description,
                status = it.status
            )
        }
    }

    fun meetingDataResponseToModel(meetingDetailData: MeetingDetailData): Meeting =
        meetingDetailData.let {
            Meeting(
                id = it.id,
                title = it.title,
                startTime = it.startTime,
                endTime = it.endTime,
                location = it.location,
                description = it.description,
                code = it.description,
                status = it.status,
                userStatus = it.userStatus,
                agenda = agendaResponseToModel(it.agenda),
                participant = participantResponseToModel(it.participant)
            )
        }


    fun agendaResponseToModel(agendaItem: List<AgendaItem>): List<Agenda> =
        agendaItem.map {
            Agenda(
                id = it.id,
                meetingId = it.meetingId,
                task = it.task,
                completed = it.completed
            )
        }

    fun participantResponseToModel(participantResponse: List<ParticipantItem>): kotlin.collections.List<Participant> =
        participantResponse.map {
            Participant(
                id = it.id,
                email = it.email,
                name = it.name,
                profilePic = it.profilePic,
                status = it.status,
                role = it.role
            )
        }
}