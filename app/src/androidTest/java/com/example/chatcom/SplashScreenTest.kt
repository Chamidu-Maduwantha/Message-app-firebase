package com.example.chatcom


import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SplashScreenTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(SplashScreen::class.java)

    @Test
    fun splashScreenTest() {
    }
}
