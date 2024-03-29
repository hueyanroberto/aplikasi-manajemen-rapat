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
            token = userData.token,
            statusLogin = userData.statusLogin
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

    fun userProfileToModel(userProfileData: ProfileData): User {
        return User(
            id = userProfileData.id,
            email = userProfileData.email,
            name = userProfileData.name,
            profilePic = userProfileData.profilePic,
            exp = userProfileData.exp,
            levelId = userProfileData.levelId,
            level = levelResponseToModel(userProfileData.level),
            achievement = achievementResponseToModel(userProfileData.achievement)
        )
    }

    fun levelResponseToModel(levelResponse: LevelResponse): Level {
        return Level(
            id = levelResponse.id,
            name = levelResponse.name,
            level = levelResponse.level,
            badgeUrl = levelResponse.badgeUrl,
            minExp = levelResponse.minExp,
            maxExp = levelResponse.maxExp
        )
    }

    fun achievementResponseToModel(achievement: List<AchievementItem>): List<Achievement> =
        achievement.map {
            Achievement(
                id = it.id,
                name = it.name,
                description = it.description ?: "",
                badgeUrl = it.badgeUrl,
                milestone = it.milestone,
                progress = it.progress ?: 0,
                status = it.status ?: 0,
                rewardExp = it.rewardExp
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

    fun leaderboardResponseToEntity(listLeaderboard: List<LeaderboardItem>): List<Leaderboard> =
        listLeaderboard.map {
            Leaderboard(
                id = it.id,
                name = it.name,
                profilePic = it.profilePic,
                pointsGet = it.pointsGet,
                levelId = it.levelId
            )
        }

    fun leaderboardResponseToEntity(leaderboardData: LeaderboardData): LeaderboardDetail {
        return leaderboardData.let {
            LeaderboardDetail(
                startDate = it.startDate ?: Date(),
                endDate = it.endDate ?: Date(),
                period = it.period,
                duration = it.duration ?: 0,
                leaderboards = leaderboardResponseToEntity(it.leaderboard)
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
                leaderboardEnd = it.leaderboardEnd?.let { date -> format.format(date) },
                role = it.role?.let { roleResponse -> roleResponseToModel(roleResponse) },
                leaderboardDuration = it.leaderboardDuration,
                leaderboardPeriod = it.leaderboardPeriod
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
                leaderboardEnd = it.leaderboardEnd,
                leaderboardPeriod = 0,
                leaderboardDuration = 0
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

    fun meetingDetailResponseToModel(meetingDetailData: MeetingDetailData): Meeting =
        meetingDetailData.let {
            Meeting(
                id = it.id,
                title = it.title,
                startTime = it.startTime,
                endTime = it.endTime,
                location = it.location,
                description = it.description,
                code = it.code,
                status = it.status,
                userStatus = it.userStatus,
                userRole = it.userRole,
                agenda = agendaResponseToModel(it.agenda),
                participant = participantResponseToModel(it.participant),
                attachments = attachmentResponseToModel(it.attachments),
                meetingNote = it.meetingNote,
                realStart = it.realStart,
                realEnd = it.realEnd,
                point = it.point
            )
        }


    fun agendaResponseToModel(agendaItem: List<AgendaItem>): List<Agenda> =
        agendaItem.map {
            Agenda(
                id = it.id,
                meetingId = it.meetingId,
                task = it.task,
                completed = it.completed,
                suggestions = it.suggestions?.let { suggestions -> suggestionResponseToModel(suggestions) }
            )
        }

    fun participantResponseToModel(participantResponse: List<ParticipantItem>): List<Participant> =
        participantResponse.map {
            Participant(
                id = it.id,
                email = it.email,
                name = it.name,
                profilePic = it.profilePic,
                status = it.status,
                role = it.role,
                levelId = it.levelId
            )
        }

    fun suggestionResponseToModel(listSuggestion: List<SuggestionItem>): List<Suggestion> =
        listSuggestion.map {
            Suggestion(
                id = it.id,
                userId = it.userId,
                agendaId = it.agendaId,
                accepted = it.accepted,
                suggestion = it.suggestion,
                user = it.user
            )
        }

    fun suggestionResponseToModel(suggestion: SuggestionItem): Suggestion =
        Suggestion(
            id = suggestion.id,
            userId = suggestion.userId,
            agendaId = suggestion.agendaId,
            accepted = suggestion.accepted,
            suggestion = suggestion.suggestion,
            user = suggestion.user
        )

    fun attachmentResponseToModel(listAttachment: List<AttachmentItem>): List<Attachment> =
        listAttachment.map {
            Attachment(
                id = it.id,
                meetingId = it.meetingId,
                url = it.url
            )
        }

    fun taskResponseToModel(listTaskResponse: List<TaskItem>): List<Task> {
        return listTaskResponse.map {
            Task(
                id = it.id,
                meetingId = it.meetingId,
                assignedTo = it.assignedTo,
                title = it.title,
                description = it.description,
                status = it.status,
                deadline = it.deadline,
                user = it.user
            )
        }
    }

    fun taskResponseToModel(taskResponse: TaskItem): Task {
        return Task(
            id = taskResponse.id,
            meetingId = taskResponse.meetingId,
            assignedTo = taskResponse.assignedTo,
            title = taskResponse.title,
            description = taskResponse.description,
            status = taskResponse.status,
            deadline = taskResponse.deadline,
            user = taskResponse.user
        )
    }

    fun meetingPointResponseToModel(meetingPointResponse: MeetingPointResponse): List<MeetingPoint> {
        return meetingPointResponse.data.map {
            MeetingPoint(
                point = it.point,
                description = it.description
            )
        }
    }
}