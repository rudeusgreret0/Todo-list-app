package com.example

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Aura Tasks", appName)
    }

    @Test
    fun `toggle between premium color palettes by clicking theme name pill`() {
        composeTestRule.onNodeWithTag("header_title_text").assertIsDisplayed()

        // Verify initial theme label and cycle through palettes by clicking the theme name pill
        composeTestRule
            .onNodeWithTag("active_theme_label", useUnmergedTree = true)
            .assertTextEquals("Royal Indigo")

        composeTestRule.onNodeWithTag("cycle_theme_button").performClick()
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("active_theme_label", useUnmergedTree = true)
            .assertTextEquals("Obsidian Gold")

        composeTestRule.onNodeWithTag("cycle_theme_button").performClick()
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithTag("active_theme_label", useUnmergedTree = true)
            .assertTextEquals("Emerald Silk")
    }
}
