package com.nexus.calculator


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class UIInstrumentedTest {
    val list = arrayListOf(
        "4+3+4",
        "3-2",
        "-7.9+2.2",
        "3+2*3",
        "3*2+3",
        "3+(2*3)+((5/3)-1)"
    )

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)
    val numberAdapter: NumberAdapter get() = mActivityTestRule.activity.numberAdapter


    @Test
    fun checkNumberPosition() {
        val count = numberAdapter.itemCount - 1
        for (i in 0..count) {
            val item = numberAdapter.getItem(i)
            onView(
                allOf(
                    withId(R.id.button),
                    withText(if (item.isBack) "<<" else item.value.toString()),
                    childAtPosition(
                        childAtPosition(withId(R.id.recyclerView), i),
                        0
                    ),
                    isDisplayed()
                )
            )
        }
    }

    @Test
    fun allNumberTest() {
        val count = numberAdapter.itemCount - 1
        for (i in 0..count) {
            val item = numberAdapter.getItem(i)
            if (item.isBack || item.isClean)
                continue
            click(i)
        }
        showResult()
    }

    fun checkText(text: String) {
        val textView = onView(
            allOf(
                withId(R.id.info),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(android.view.ViewGroup::class.java),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText(text)))
    }

    @Test
    fun evaluateTest() {
        list.forEach { expression ->
            enter(expression = expression)
            showResult()
            checkText(NexusCalculator.evaluate(expression).toString())
            clean()
            checkText("")
        }
    }

    fun enter(expression: String) {
        expression.toCharArray().forEach { char ->
            val posotion = numberAdapter.getPosition(char)
            if (posotion in 0..(numberAdapter.itemCount - 1)) {
                click(posotion)
            }
        }
        checkText(expression)
    }

    fun click(position: Int) {
        val appCompatButton = onView(
            allOf(
                childAtPosition(childAtPosition(withId(R.id.recyclerView), position), 0),
                isDisplayed()
            )
        )
        appCompatButton.perform(click())
    }

    fun showResult() {
        val view = onView(
            allOf(
                withId(R.id.button), withText("="),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        19
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        view.perform(click())
    }

    fun clean() {
        val view = onView(
            allOf(
                withId(R.id.button), withText("C"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        12
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        view.perform(click())
    }

    fun backPress() {
        val view = onView(
            allOf(
                withId(R.id.button), withText("<<"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.recyclerView),
                        16
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        view.perform(click())
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
