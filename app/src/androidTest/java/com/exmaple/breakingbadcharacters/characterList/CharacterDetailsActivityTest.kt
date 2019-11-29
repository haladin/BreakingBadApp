package com.exmaple.breakingbadcharacters.characterList


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.exmaple.breakingbadcharacters.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CharacterDetailsActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(CharacterListActivity::class.java)

    @Test
    fun characterDetailsActivityTest() {
        Thread.sleep(2000)
        val linearLayout = onView(
            allOf(
                childAtPosition(
                    allOf(
                        withId(R.id.rv_characters_list),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            0
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        linearLayout.perform(click())

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        Thread.sleep(2000)

        val textView = onView(
            allOf(
                withId(R.id.tvStatusColor), withText("Presumed dead"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                        2
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Presumed dead")))

        val textView2 = onView(
            allOf(
                withId(R.id.tvSeasons), withText("Appears in season 1, 2, 3, 4, 5"),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                        2
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Appears in season 1, 2, 3, 4, 5")))
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
