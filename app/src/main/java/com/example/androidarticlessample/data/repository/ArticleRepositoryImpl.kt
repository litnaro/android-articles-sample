package com.example.androidarticlessample.data.repository

import com.example.androidarticlessample.data.mapper.ArticleMapper
import com.example.androidarticlessample.data.source.ArticleRemoteDataSource
import com.example.androidarticlessample.domain.model.Article
import com.example.androidarticlessample.domain.repository.ArticleRepository
import com.example.androidarticlessample.domain.util.AppException
import com.example.androidarticlessample.domain.util.Container
import com.example.androidarticlessample.domain.util.Period
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val remote: ArticleRemoteDataSource,
    private val mapper: ArticleMapper
) : ArticleRepository {

    private var cachedArticles: List<Article> = emptyList()

    override suspend fun getMostViewedArticles(period: Period): Container<List<Article>> {
        return try {
            val response = remote.mostViewed(period.days)
            val list = mapper.fromResponse(response)
            cachedArticles = list
            if (list.isEmpty()) {
                Container.Error(AppException.NotFound("Empty list"))
            } else {
                Container.Success(list)
            }
        } catch (e: IOException) {
            Container.Error(AppException.Network(e.message, e))
        } catch (e: HttpException) {
            val code = e.code()
            val appException = if (code == 404) AppException.NotFound("Not found")
            else AppException.Server("HTTP $code", e)
            Container.Error(appException)
        } catch (e: Exception) {
            Container.Error(AppException.Unknown(e.message, e))
        }
    }

    override suspend fun getArticleById(id: Long): Container<Article> {
        val article = cachedArticles.firstOrNull { it.id == id }
        return if (article != null) Container.Success(article)
        else Container.Error(AppException.NotFound("Article not found"))
    }
}