package com.android.afreecasampleapp

import com.android.data.datasource.BroadRemoteDataSource
import com.android.data.datasource.BrodRemoteDataSourceImpl
import com.android.data.repository.BroadRepositoryImpl
import com.android.domain.repository.BroadRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindBroadRemoteDataSource(
        source: BrodRemoteDataSourceImpl
    ): BroadRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBroadRepository(
        repository: BroadRepositoryImpl
    ): BroadRepository
}
