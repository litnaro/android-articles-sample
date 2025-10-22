package com.example.androidarticlessample.data.repository

import com.example.androidarticlessample.data.mapper.ArticleMapper
import com.example.androidarticlessample.data.source.ArticleRemoteDataSource
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import com.example.androidarticlessample.testutil.DtoFactory
import com.example.androidarticlessample.testutil.FakeNyTimesApi
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ArticleRepositoryImplTest {

    @Test
    fun `getMostViewed() with non-empty remote returns Success and caches items`() = kotlinx.coroutines.test.runTest {
        val api = FakeNyTimesApi(results = listOf(DtoFactory.articleDto(id = 7L)))
        val remoteDataSource = ArticleRemoteDataSource(api = api, apiKey = "key")
        val repositoryImpl = ArticleRepositoryImpl(remoteDataSource, ArticleMapper())

        val result = repositoryImpl.getMostViewedArticles(Period.SEVEN_DAYS)
        assertTrue(result is Container.Success)

        val byId = repositoryImpl.getArticleById(7L)
        assertTrue(byId is Container.Success)
    }

    @Test
    fun `getArticleById() without cache returns Error NotFound`() = kotlinx.coroutines.test.runTest {
        val api = FakeNyTimesApi(results = emptyList())
        val remote = ArticleRemoteDataSource(api = api, apiKey = "key")
        val repositoryImpl = ArticleRepositoryImpl(remote, ArticleMapper())

        val byId = repositoryImpl.getArticleById(1L)
        assertTrue(byId is Container.Error)
    }
}