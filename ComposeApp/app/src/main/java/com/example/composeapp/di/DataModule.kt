package com.example.composeapp.di

import com.example.composeapp.repositories.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideMainRepository(httpClient: OkHttpClient) = MainRepository(httpClient)

    @Provides
    fun provideOkHttp() = OkHttpClient()

}