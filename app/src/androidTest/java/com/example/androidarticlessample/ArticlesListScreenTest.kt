package com.example.androidarticlessample

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

class ArticlesListScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun articlesList_tagIsVisible_onLaunch() {
        composeRule.onRoot(useUnmergedTree = true).printToLog("SEMANTICS")

        composeRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag("ArticlesList"),
            timeoutMillis = 10_000L
        )

        composeRule.onNode(
            hasTestTag("ArticlesList"),
            useUnmergedTree = true
        ).assertIsDisplayed()
    }
}