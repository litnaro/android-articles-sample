package com.example.androidarticlessample.core.di

import com.example.androidarticlessample.presentation.mapper.ArticlePreviewMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object PresentationModule {

    @Provides
    fun provideArticlePreviewMapper(): ArticlePreviewMapper = ArticlePreviewMapper()
}