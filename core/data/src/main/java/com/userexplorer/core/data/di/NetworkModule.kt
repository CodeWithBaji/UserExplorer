package com.userexplorer.core.data.di

import com.google.gson.Gson
import com.userexplorer.core.data.NetworkConfig
import com.userexplorer.core.data.remote.UserApi
import com.userexplorer.core.data.remote.datasource.UserRemoteDataSource
import com.userexplorer.core.data.remote.datasource.UserRemoteDataSourceImpl
import com.userexplorer.core.data.repository.UserRepositoryImpl
import com.userexplorer.core.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * Hilt module providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideUserRemoteDataSource(api: UserApi): UserRemoteDataSource =
        UserRemoteDataSourceImpl(api = api)

    @Provides
    @Singleton
    fun provideUserRepository(remoteDataSource: UserRemoteDataSource): UserRepository =
        UserRepositoryImpl(remoteDataSource = remoteDataSource)
}