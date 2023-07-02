package ac.id.ubaya.aplikasimanajemenrapat.core.di

import ac.id.ubaya.aplikasimanajemenrapat.core.data.source.remote.network.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(240, TimeUnit.SECONDS)
            .readTimeout(240, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun provideApiService(client: OkHttpClient): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    companion object {
//        private const val BASE_URL = "http://192.168.191.163:80/api/"
//        private const val BASE_URL = "http://192.168.100.33:80/api/"
        private const val BASE_URL = "http://10.0.2.2:8000/api/"
//        private const val BASE_URL = "http://192.168.194.163:80/api/"
//        private const val BASE_URL = "https://manara.hueyanroberto.my.id/api/"
    }
}