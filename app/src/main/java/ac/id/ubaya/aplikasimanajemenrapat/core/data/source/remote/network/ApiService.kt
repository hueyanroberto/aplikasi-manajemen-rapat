package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.MeetingResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.OrganizationResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserListResponse
import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserResponse
import org.json.JSONObject
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
        @Body body: JSONObject
    ): MeetingResponse
}