package com.exmaple.breakingbadcharacters.characterList


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.exmaple.breakingbadcharacters.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CharacterListActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CharacterListActivity::class.java)

    @Test
    fun characterListActivityTest() {
        Thread.sleep(2000)
        val textView = onView(
            allOf(
                withId(R.id.tvCharacterName), withText("Walter White"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.cvCharacter),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Walter White")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
