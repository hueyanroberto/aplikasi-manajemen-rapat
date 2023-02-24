package ac.id.ubaya.aplikasimanajemenrapat.core.utils

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.local.entity.OrganizationEntity
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.MeetingItem
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.OrganizationData
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.RoleResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserData
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Meeting
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Organization
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.Role
import ac.id.ubaya.aplikasimanajemenrapat.core.domain.model.User
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
}