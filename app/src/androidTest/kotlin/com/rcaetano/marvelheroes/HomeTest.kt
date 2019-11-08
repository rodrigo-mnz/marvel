package com.rcaetano.marvelheroes

import android.content.Intent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import br.com.concretesolutions.requestmatcher.InstrumentedTestRequestMatcherRule
import br.com.concretesolutions.requestmatcher.model.HttpMethod
import com.rcaetano.marvelheroes.di.appModule
import com.rcaetano.marvelheroes.feature.common.ItemHolder
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule
    val server = InstrumentedTestRequestMatcherRule()
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    @Before
    fun before() {
        val testModules = listOf(
            appModule,
            createMockNetworkModule(server.url("/").toString())
        )
        startKoin {
            androidLogger()
            modules(testModules)
        }
    }

    @After
    fun after() {
        stopKoin()
    }

    @Test
    fun whenHomeStarted_shouldLoadCharactersAndMakeCorrectRequest() {
        server.addFixture("character-list.json")
            .ifRequestMatches()
            .methodIs(HttpMethod.GET)
            .pathIs("/v1/public/characters")
            .queriesContain("offset", "0")

        activityRule.launchActivity(Intent())
        waitOneSecond()

        onView(withText("3-D Man"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenScrollDown_shouldLoadMoreCharactersAndMakeCorrectRequest() {
        server.addFixture("character-list.json")
            .ifRequestMatches()
            .methodIs(HttpMethod.GET)
            .pathIs("/v1/public/characters")
            .queriesContain("offset", "0")
        activityRule.launchActivity(Intent())
        waitOneSecond()

        server.addFixture("character-list.json")
            .ifRequestMatches()
            .methodIs(HttpMethod.GET)
            .pathIs("/v1/public/characters")
            .queriesContain("offset", "20")

        onView(withId(R.id.recycler_view_home))
            .check(matches(isDisplayed()))
            .perform(scrollToPosition<ItemHolder.CharacterItem>(20))
        waitOneSecond()
    }
}
