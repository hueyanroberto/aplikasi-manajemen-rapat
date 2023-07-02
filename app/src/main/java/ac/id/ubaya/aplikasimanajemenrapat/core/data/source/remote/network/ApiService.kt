package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.Date

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @FormUrlEncoded
    @POST("login/google")
    @Headers("Accept: application/json")
    suspend fun loginGoogle(
        @Field("email") email: String,
    ): UserResponse

    @FormUrlEncoded
    @POST("register")
    @Headers("Accept: application/json")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
    )

    @FormUrlEncoded
    @PUT("user/token")
    @Headers("Accept: application/json")
    suspend fun insertFirebaseToken (
        @Header("Authorization") token: String,
        @Field("firebase_token") firebaseToken: String
    )


    @FormUrlEncoded
    @PUT("register")
    @Headers("Accept: application/json")
    suspend fun registerNameAndProfilePic(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("profile_pic") profilePic: String
    ): UserResponse

    @GET("organization")
    @Headers("Accept: application/json")
    suspend fun getListOrganization(
        @Header("Authorization") token: String
    ): OrganizationResponse

    @FormUrlEncoded
    @POST("organization/create")
    @Headers("Accept: application/json")
    suspend fun createOrganization(
        @Header("Authorization") token: String,
        @Field("name") name: String,
        @Field("description") description: String,
        @Field("profile_pic") profilePic: String,
        @Field("duration") duration: Int
    ): OrganizationResponse

    @FormUrlEncoded
    @POST("organization/join")
    @Headers("Accept: application/json")
    suspend fun joinOrganization(
        @Header("Authorization") token: String,
        @Field("code") organizationCode: String
    ): OrganizationResponse

    @FormUrlEncoded
    @PUT("organization/profile")
    @Headers("Accept: application/json")
    suspend fun updateOrganizationProfilePic(
        @Header("Authorization") token: String,
        @Field("organization_id") organizationId: Int,
        @Field("profile_pic") profilePic: String
    ): OrganizationResponse

    @FormUrlEncoded
    @PUT("organization")
    @Headers("Accept: application/json")
    suspend fun editOrganization(
        @Header("Authorization") token: String,
        @Field("organization_id") organizationId: Int,
        @Field("name") name: String,
        @Field("description") desc: String,
        @Field("leaderboard_duration") duration: Int
    ): OrganizationResponse

    @GET("organization/meetings/{organization_id}")
    @Headers("Accept: application/json")
    suspend fun getListMeeting(
        @Header("Authorization") token: String,
        @Path("organization_id") organizationId: Int
    ): MeetingResponse

    @GET("organization/members/{organization_id}")
    @Headers("Accept: application/json")
    suspend fun getOrganizationMembers(
        @Header("Authorization") token: String,
        @Path("organization_id") organizationId: Int
    ): UserListResponse

    @FormUrlEncoded
    @PUT("organization/role")
    @Headers("Accept: application/json")
    suspend fun updateRole(
        @Header("Authorization") token: String,
        @Field("organization_id") organizationId: Int,
        @Field("user_id") userId: Int,
        @Field("role_id") roleId: Int
    ): UserResponse

    @GET("organization/leaderboard")
    @Headers("Accept: application/json")
    suspend fun getLeaderboard(
        @Header("Authorization") token: String,
        @Query("organization_id") organizationId: Int
    ): LeaderboardResponse

    @GET("organization/leaderboard/history/{organizationId}")
    @Headers("Accept: application/json")
    suspend fun getLeaderboardHistory(
        @Header("Authorization") token: String,
        @Path("organizationId") organizationId: Int,
        @Query("period") period: Int
    ): LeaderboardResponse

    @GET("meeting/create/member")
    @Headers("Accept: application/json")
    suspend fun getMemberToBeChosen(
        @Header("Authorization") token: String,
        @Query("organizationId") organizationId: Int
    ): UserListResponse

    @POST("meeting")
    @Headers("Accept: application/json")
    suspend fun createMeeting(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): MeetingResponse

    @PUT("meeting")
    @Headers("Accept: application/json")
    suspend fun editMeeting(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): MeetingResponse

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "meeting", hasBody = true)
    @Headers("Accept: application/json")
    suspend fun deleteMeeting(
        @Header("Authorization") token: String,
        @Field("meeting_id") meetingId: Int
    ): MeetingResponse

    @GET("meeting")
    @Headers("Accept: application/json")
    suspend fun getMeetingDetail(
        @Header("Authorization") token: String,
        @Query("meeting_id") meetingId: Int
    ): MeetingDetailResponse

    @POST("agenda")
    @Headers("Accept: application/json")
    suspend fun addAgenda(
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): AgendaResponse

    @GET("agenda/suggestion")
    suspend fun getListSuggestion(
        @Header("Authorization") token: String,
        @Query("agenda_id") agendaId: Int
    ): SuggestionListResponse

    @FormUrlEncoded
    @POST("agenda/suggestion")
    @Headers("Accept: application/json")
    suspend fun addSuggestion(
        @Header("Authorization") token: String,
        @Field("agenda_id") agendaId: Int,
        @Field("suggestion") suggestion: String
    ): SuggestionResponse

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "agenda/suggestion", hasBody = true)
    @Headers("Accept: application/json")
    suspend fun deleteSuggestion(
        @Header("Authorization") token: String,
        @Field("suggestion_id") suggestionId: Int
    ): SuggestionResponse

    @FormUrlEncoded
    @PUT("agenda/suggestion/accept")
    @Headers("Accept: application/json")
    suspend fun acceptSuggestion(
        @Header("Authorization") token: String,
        @Field("suggestion_id") suggestionId: Int
    ): SuggestionResponse

    @FormUrlEncoded
    @POST("meeting/start")
    @Headers("Accept: application/json")
    suspend fun startMeeting(
        @Header("Authorization") token: String,
        @Field("meeting_id") meetingId: Int,
        @Field("date") date: Date
    ): MeetingDetailResponse

    @FormUrlEncoded
    @POST("meeting/join")
    @Headers("Accept: application/json")
    suspend fun joinMeeting(
        @Header("Authorization") token: String,
        @Field("meeting_id") meetingId: Int,
        @Field("meeting_code") meetingCode: String,
        @Field("date") date: Date
    ): MeetingDetailResponse

    @FormUrlEncoded
    @POST("meeting/end")
    @Headers("Accept: application/json")
    suspend fun endMeeting(
        @Header("Authorization") token: String,
        @Field("meeting_id") meetingId: Int,
        @Field("date") date: Date,
        @Field("meeting_note") meetingNote: String
    ): MeetingDetailResponse

    @GET("meeting/minutes")
    @Headers("Accept: application/json")
    suspend fun getMinutes(
        @Header("Authorization") token: String,
        @Query("meeting_id") meetingId: Int
    ): AgendaResponse

    @GET("meeting/point")
    @Headers("Accept: application/json")
    suspend fun getMeetingPointLog(
        @Header("Authorization") token: String,
        @Query("meeting_id") meetingId: Int
    ): MeetingPointResponse

    @FormUrlEncoded
    @PUT("agenda")
    @Headers("Accept: application/json")
    suspend fun editAgenda(
        @Header("Authorization") token: String,
        @Field("agenda_id") agendaId: Int,
        @Field("task") task: String
    ): AgendaResponse

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "agenda", hasBody = true)
    @Headers("Accept: application/json")
    suspend fun deleteAgenda(
        @Header("Authorization") token: String,
        @Field("agenda_id") agendaId: Int
    ): AgendaResponse

    @FormUrlEncoded
    @PATCH("agenda")
    @Headers("Accept: application/json")
    suspend fun updateAgendaStatus(
        @Header("Authorization") token: String,
        @Field("agenda_id") agendaId: Int
    ): AgendaResponse

    @GET("meeting")
    @Headers("Accept: application/json")
    suspend fun getAgendaDetail(
        @Header("Authorization") token: String,
        @Query("agendaId") agendaId: Int
    ): AgendaResponse

    @Multipart
    @POST("meeting/attachment")
    suspend fun uploadAttachment(
        @Header("Authorization") token: String,
        @Part files: List<MultipartBody.Part>,
        @Part("meeting_id") meetingId: RequestBody
    ): AttachmentResponse

    @GET("profile")
    suspend fun getProfile(
        @Header("Authorization") token: String,
    ): ProfileResponse

    @FormUrlEncoded
    @PATCH("profile")
    @Headers("Accept: application/json")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Field("name") name: String
    ): ProfileResponse

    @FormUrlEncoded
    @PUT("profile/profilePic")
    @Headers("Accept: application/json")
    suspend fun updateProfilePic(
        @Header("Authorization") token: String,
        @Field("profile_pic") profilePic: String
    ): ProfileResponse

    @GET("profile/{user_id}")
    suspend fun getOtherProfile(
        @Header("Authorization") token: String,
        @Path("user_id") userId: Int
    ): ProfileResponse

    @GET("user/achievements")
    suspend fun getAchievements(
        @Header("Authorization") token: String,
    ): AchievementListResponse

    @GET("meeting/task/{meetingId}")
    suspend fun getListTask(
        @Header("Authorization") token: String,
        @Path("meetingId") meetingId: Int
    ): TaskListResponse

    @FormUrlEncoded
    @POST("meeting/task")
    @Headers("Accept: application/json")
    suspend fun addTask(
        @Header("Authorization") token: String,
        @Field("meeting_id") meetingId: Int,
        @Field("assign_to") userId: Int,
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("deadline") deadline: Date
    ): TaskResponse

    @FormUrlEncoded
    @PATCH("meeting/task/{taskId}")
    @Headers("Accept: application/json")
    suspend fun updateTaskStatus(
        @Header("Authorization") token: String,
        @Path("taskId") taskId: Int,
        @Field("date") date: Date
    ): TaskResponse
}