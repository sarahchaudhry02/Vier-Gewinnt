package com.example.viergewinnt

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class GameE2ETest {
    @get:Rule

    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test

    fun hostStartetSpielUndFuehrtSpielzugAus() {

        composeTestRule

            .onNodeWithText("Verbindung starten")

            .assertIsDisplayed()

            .performClick()

        composeTestRule.waitUntil(timeoutMillis = 5_000) {

            composeTestRule

                .onAllNodesWithText("Start")

                .fetchSemanticsNodes()

                .isNotEmpty()

        }

        composeTestRule

            .onNodeWithText("Start")

            .assertIsDisplayed()

            .performClick()

        composeTestRule

            .onNodeWithTag("game_board")

            .assertIsDisplayed()

        composeTestRule

            .onNodeWithText("Du bist dran")

            .assertIsDisplayed()

        composeTestRule

            .onNodeWithTag("cell_5_0")

            .performClick()

        composeTestRule

            .onNodeWithText("Auf anderen Spieler warten")

            .assertIsDisplayed()

    }


}