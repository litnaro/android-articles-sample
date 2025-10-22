package com.example.androidarticlessample.core.di

import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.usecase.GetArticleByIdUseCase
import com.example.androidarticlessample.domain.usecase.GetMostViewedArticlesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMostViewedArticlesUseCase(
        repo: ArticleRepository
    ): GetMostViewedArticlesUseCase = GetMostViewedArticlesUseCase(repo)

    @Provides
    @Singleton
    fun provideGetArticleByIdUseCase(
        repo: ArticleRepository
    ): GetArticleByIdUseCase = GetArticleByIdUseCase(repo)
}