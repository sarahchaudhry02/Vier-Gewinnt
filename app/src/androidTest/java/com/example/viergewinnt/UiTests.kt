package com.example.viergewinnt

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test

class UiTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startScreen_showsConnectionButtons() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Verbinden")
            .assertIsDisplayed()
    }

    @Test
    fun clickStartConnection_navigatesToConnectionView() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .performClick()

        composeTestRule
            .onNodeWithText("Start")
            .assertIsDisplayed()
    }

    @Test
    fun boardView_showsGameStatusAfterStart() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .performClick()

        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Gast-Spieler verbunden")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Start", useUnmergedTree = true)
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Start", useUnmergedTree = true)
            .performClick()

        composeTestRule
            .onNodeWithText("Du bist dran")
            .assertIsDisplayed()
    }

    @Test
    fun boardView_showsSixBySevenBoard() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .performClick()

        composeTestRule
            .onNodeWithText("Start")
            .performClick()

        composeTestRule
            .onNodeWithTag("game_board")
            .assertIsDisplayed()

        for (row in 0 until 6) {
            for (column in 0 until 7) {
                composeTestRule
                    .onNodeWithTag("cell_${row}_${column}")
                    .assertIsDisplayed()
            }
        }
    }

    @Test
    fun statusText_changesAfterColumnClick() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .performClick()

        composeTestRule
            .onNodeWithText("Start")
            .performClick()

        composeTestRule
            .onNodeWithText("Du bist dran")
            .assertIsDisplayed()
    }

    @Test
    fun cancelButton_isDisplayedDuringGame() {
        composeTestRule
            .onNodeWithText("Verbindung starten")
            .performClick()

        composeTestRule
            .onNodeWithText("Start")
            .performClick()

        composeTestRule
            .onNodeWithText("Spiel verlassen")
            .assertIsDisplayed()
    }
}