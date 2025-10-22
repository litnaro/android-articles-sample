package com.example.androidarticlessample

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class ArticleDetailsScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun navigateToDetails_detailsScreenAndContentAreVisible() {
        composeRule.onRoot(useUnmergedTree = true).printToLog("SEMANTICS")

        composeRule.waitUntilAtLeastOneExists(hasTestTag("ArticleItem"))

        composeRule.onNodeWithTag("ArticlesList")
            .performScrollToIndex(0)
        composeRule.onAllNodes(hasTestTag("ArticleItem"), useUnmergedTree = true)
            .onFirst()
            .assertIsDisplayed()
            .performClick()

        composeRule.waitUntilAtLeastOneExists(hasTestTag("DetailsScreen"))

        composeRule.onNode(hasTestTag("DetailsContent"), useUnmergedTree = true)
            .assertExists()
            .assertIsDisplayed()
    }
}