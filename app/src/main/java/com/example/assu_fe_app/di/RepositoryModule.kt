package com.example.assu_fe_app.di

import com.example.assu_fe_app.data.repositoryImpl.AuthRepositoryImpl
import com.example.assu_fe_app.data.repositoryImpl.chatting.ChattingRepositoryImpl
import com.example.assu_fe_app.data.repositoryImpl.deviceToken.DeviceTokenRepositoryImpl
import com.example.assu_fe_app.domain.repository.AuthRepository
import com.example.assu_fe_app.data.repository.chatting.ChattingRepository
import com.example.assu_fe_app.data.repository.deviceToken.DeviceTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
    
    @Binds
    @Singleton
    abstract fun bindChattingRepository(
        chattingRepositoryImpl: ChattingRepositoryImpl
    ): ChattingRepository
    
    @Binds
    @Singleton
    abstract fun bindDeviceTokenRepository(
        deviceTokenRepositoryImpl: DeviceTokenRepositoryImpl
    ): DeviceTokenRepository
}
