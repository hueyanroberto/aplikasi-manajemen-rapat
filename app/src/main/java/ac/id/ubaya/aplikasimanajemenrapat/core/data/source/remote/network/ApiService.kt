package ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.response.UserResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Header("Authorization") apiKey: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Header("Authorization") apiKey: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): UserResponse

    @FormUrlEncoded
    @PUT("user/register/{userId}")
    suspend fun registerNameAndProfilePic(
        @Header("Authorization") apiKey: String,
        @Path("userId") userId: Int,
        @Field("name") name: String,
        @Field("profilePic") profilePic: String
    ): UserResponse
}