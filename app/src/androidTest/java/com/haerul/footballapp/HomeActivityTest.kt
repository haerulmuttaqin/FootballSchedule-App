package com.haerul.footballapp

import android.support.design.widget.TabLayout
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.pressBack
import android.support.test.espresso.PerformException
import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.RecyclerView
import android.view.View
import com.haerul.footballapp.R.id.*
import com.haerul.footballapp.ui.main.HomeActivity
import org.hamcrest.BaseMatcher
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.Thread.sleep


@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule
    @JvmField var activityRule = ActivityTestRule(HomeActivity::class.java)

    @Test
    fun testAppBehaviour() {
        sleep(1000)

        onView(withId(tab_layout_match)).check(matches(isDisplayed()))
        onView(withId(view_pager_match)).check(matches(isDisplayed()))
        sleep(1000)

        onView(withId(tab_layout_match)).perform(selectTabAtPosition(0))
        sleep(1000)

        onView(result(withId(spinner), 0)).check(matches(isDisplayed()))
        onView(result(withId(spinner), 0)).perform(click())
        onView(result(withText("Spanish La Liga"), 0)).perform(click())

        onView(result(withId(recycler_view), 0)).check(matches(isDisplayed()))
        onView(result(withId(recycler_view), 0))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))

        sleep(1000)
        pressBack()

        onView(withId(tab_layout_match)).perform(selectTabAtPosition(1))
        sleep(1000)

        onView(result(withId(recycler_view), 1)).check(matches(isDisplayed()))
        onView(result(withId(recycler_view), 1)).perform(scrollToPosition<RecyclerView.ViewHolder>(10))
        onView(result(withId(recycler_view), 1))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(7, click()))

        sleep(1000)

        onView(withId(nested_scroll)).check(matches(isDisplayed()))
        onView(withId(android.R.id.content)).perform(swipeUp())
        onView(withId(nested_scroll)).perform(swipeDown())

        pressBack()

        onView(withId(bottom_navigation)).check(matches(isDisplayed()))
        onView(withId(teams)).perform(click())

        sleep(1000)

        onView(withContentDescription(R.string.teams_desc)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.teams_desc))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(3, click()))

        onView(withId(tab_layout)).perform(selectTabAtPosition(0))
        onView(withId(view_pager)).check(matches(isDisplayed()))

        onView(withId(android.R.id.content)).perform(swipeUp())
        onView(withId(android.R.id.content)).perform(swipeDown())

        onView(withId(tab_layout)).perform(selectTabAtPosition(1))

        sleep(1000)

        onView(withContentDescription(R.string.player_desc)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.player_desc))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        sleep(1000)
        pressBack()
        pressBack()

        onView(withId(bottom_navigation)).check(matches(isDisplayed()))
        onView(withId(favorites)).perform(click())

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(0))

        onView(withContentDescription(R.string.favorites_match)).check(matches(isDisplayed()))

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(1))

        sleep(1000)

        onView(withContentDescription(R.string.favorites_team)).check(matches(isDisplayed()))

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(0))

        sleep(1000)
    }


    @Test
    fun testAddRemoveFavorites() {

        onView(withId(tab_layout_match)).check(matches(isDisplayed()))
        onView(withId(view_pager_match)).check(matches(isDisplayed()))
        sleep(1000)

        onView(withId(tab_layout_match)).perform(selectTabAtPosition(0))
        sleep(1000)

        onView(result(withId(recycler_view), 0)).check(matches(isDisplayed()))
        onView(result(withId(recycler_view), 0))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(5, click()))

        onView(withId(add_to_favorite)).perform(click())
        try {
            onView(withText("Added to favorite")).check(matches(isDisplayed()))
        } catch (e: Exception) {
            onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        }

        sleep(1000)
        pressBack()

        onView(withId(bottom_navigation)).check(matches(isDisplayed()))
        onView(withId(teams)).perform(click())

        sleep(1000)

        onView(withContentDescription(R.string.teams_desc)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.teams_desc))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(tab_layout)).perform(selectTabAtPosition(0))
        onView(withId(view_pager)).check(matches(isDisplayed()))

        onView(withId(android.R.id.content)).perform(swipeUp())
        onView(withId(android.R.id.content)).perform(swipeDown())

        onView(withId(tab_layout)).perform(selectTabAtPosition(1))

        sleep(1000)

        onView(withContentDescription(R.string.player_desc)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.player_desc))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        sleep(1000)
        pressBack()

        onView(withId(add_to_favorite)).perform(click())
        try {
            onView(withText("Added to favorite")).check(matches(isDisplayed()))
        } catch (e: Exception) {
            onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        }

        pressBack()

        onView(withId(bottom_navigation)).check(matches(isDisplayed()))
        onView(withId(favorites)).perform(click())

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(0))

        onView(withContentDescription(R.string.favorites_match)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.favorites_match))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        try {
            onView(withText("Added to favorite")).check(matches(isDisplayed()))
        } catch (e: Exception) {
            onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        }

        sleep(1000)
        pressBack()

        onView(withId(tab_layout_favorite)).perform(selectTabAtPosition(1))

        sleep(1000)

        onView(withContentDescription(R.string.favorites_team)).check(matches(isDisplayed()))
        onView(withContentDescription(R.string.favorites_team))
                .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(withId(add_to_favorite)).perform(click())
        try {
            onView(withText("Added to favorite")).check(matches(isDisplayed()))
        } catch (e: Exception) {
            onView(withText("Removed to favorite")).check(matches(isDisplayed()))
        }

        sleep(1000)
    }

    @Test
    fun testSearch() {

        onView(withId(action_search)).perform(click())
        sleep(1000)
        onView(withHint(R.string.search_match_hint)).perform(typeText("Barcelona"))
        sleep(1000)

        closeSoftKeyboard()
        pressBack()
        pressBack()
        sleep(1000)
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                        ?: throw PerformException.Builder()
                                .withCause(Throwable("No tab at index $tabIndex"))
                                .build()

                tabAtIndex.select()
            }
        }
    }

    private fun <T> result(matcher: Matcher<T>, i: Int): Matcher<T> {
        return object : BaseMatcher<T>() {
            override fun describeTo(description: org.hamcrest.Description?) {}
            private var resultIndex = -1
            override fun matches(item: Any): Boolean {
                if (matcher.matches(item)) {
                    resultIndex++
                    return resultIndex == i
                }
                return false
            }
        }
    }
}

