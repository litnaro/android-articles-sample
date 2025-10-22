package com.example.androidarticlessample.data.mapper

import com.example.androidarticlessample.data.dto.MostViewedResponse
import com.example.androidarticlessample.testutil.DtoFactory
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ArticleMapperTest {

    @Test
    fun `fromResponse() with valid dto returns mapped domain list`() {
        val dto =
            MostViewedResponse(results = listOf(DtoFactory.articleDto(id = 42L, title = "Title")))
        val list = ArticleMapper().fromResponse(dto)

        assertEquals(1, list.size)
        assertEquals(42L, list.first().id)
        assertEquals("Title", list.first().title)
        assertEquals("https://img", list.first().media.first().url)
    }
}