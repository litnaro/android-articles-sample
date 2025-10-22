package com.example.androidarticlessample.core.di

import com.example.androidarticlessample.core.network.NyTimesApi
import com.example.androidarticlessample.data.mapper.ArticleMapper
import com.example.androidarticlessample.data.source.ArticleRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataProvidersModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(
        api: NyTimesApi,
        @NytApiKey apiKey: String
    ): ArticleRemoteDataSource = ArticleRemoteDataSource(api, apiKey)

    @Provides
    @Singleton
    fun provideArticleMapper(): ArticleMapper = ArticleMapper()
}