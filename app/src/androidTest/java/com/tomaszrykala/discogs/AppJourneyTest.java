package com.tomaszrykala.discogs;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;

import com.tomaszrykala.discogs.dagger.component.AppComponent;
import com.tomaszrykala.discogs.ui.detail.DetailActivity;
import com.tomaszrykala.discogs.ui.list.ListActivity;
import com.tomaszrykala.discogs.util.ListItem;

import junit.framework.Assert;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AppJourneyTest {

    @Rule public ActivityTestRule<ListActivity> activityRule = new ActivityTestRule<>(ListActivity.class, true, false);
    private ListActivity mActivity;

    @Before
    public void setUp() {

        final Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        final Context context = instrumentation.getTargetContext().getApplicationContext();
        final DiscogsApp discogsApp = (DiscogsApp) context;
        final AppComponent component = discogsApp.getComponent();
        final MockDiscogsApp.MockAppComponent mockAppComponent = (MockDiscogsApp.MockAppComponent) component;
        mockAppComponent.inject(this);

        activityRule.launchActivity(new Intent());
        mActivity = activityRule.getActivity();
    }

    @Test
    public void listItemShowsCorrectData() {
        final List<ListItem.ChartListItem> items = mActivity.getItems();
        for (int i = 0; i < 5; i++) {
            final ListItem.ChartListItem item = items.get(i);
            onView(withId(R.id.recycler_view)).check(matches(atPosition(i, hasDescendant(withText(item.artist)))));
            onView(withId(R.id.recycler_view)).check(matches(atPosition(i, hasDescendant(withText(item.title)))));
        }
    }

    @Test
    public void listItemOpensDetailWithCorrectInfo() {
        Instrumentation.ActivityMonitor activityMonitor =
                getInstrumentation().addMonitor(DetailActivity.class.getName(), null, false);

        final List<ListItem.ChartListItem> items = mActivity.getItems();
        final ListItem.ChartListItem item = items.get(0);
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.detail_artist)).check(matches(withText(item.artist)));

        final int monitorHits = activityMonitor.getHits();
        Assert.assertEquals(1, monitorHits);
        final Activity activity = activityMonitor.getLastActivity();
        Assert.assertTrue(activity instanceof DetailActivity);
        final DetailActivity detailActivity = (DetailActivity) activity;
        final CharSequence title = detailActivity.getSupportActionBar().getTitle();
        Assert.assertTrue(title.toString().equals(item.title.toString()));
    }

    @Test
    public void listItemIndexIsRetainedWhenGoingBackFromDetail() {
        final Matcher<View> matcher = withId(R.id.recycler_view);
        final List<ListItem.ChartListItem> items = mActivity.getItems();
        final int position = items.size() - 3;
        onView(matcher).perform(RecyclerViewActions.scrollToPosition(position));
        final ListItem.ChartListItem item = items.get(position);
        onView(matcher).check(matches(atPosition(position, hasDescendant(withText(item.artist)))));
        onView(matcher).check(matches(atPosition(position, hasDescendant(withText(item.title)))));

        onView(matcher).perform(RecyclerViewActions.actionOnItemAtPosition(position, click()));
        pressBack();

        onView(matcher).check(matches(atPosition(position, hasDescendant(withText(item.artist)))));
        onView(matcher).check(matches(atPosition(position, hasDescendant(withText(item.title)))));
    }


    /**
     * Credit: http://stackoverflow.com/a/34795431/561498
     */
    public static Matcher<View> atPosition(final int position, @NonNull final Matcher itemMatcher) {
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }
}