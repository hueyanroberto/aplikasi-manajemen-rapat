package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.*
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.Date

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @POST("logout")
    suspend fun logout(
        @Header("Authorization") token: String
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
        @Field("profile_pic") profilePic: String
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
        @Field("description") desc: String
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
        @Field("date") date: Date
    ): MeetingDetailResponse

    @GET("meeting/minutes")
    @Headers("Accept: application/json")
    suspend fun getMinutes(
        @Header("Authorization") token: String,
        @Query("meeting_id") meetingId: Int
    ): AgendaResponse
}