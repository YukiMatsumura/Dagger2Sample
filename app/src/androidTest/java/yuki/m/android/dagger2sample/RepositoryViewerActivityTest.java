package yuki.m.android.dagger2sample;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import yuki.m.android.dagger2sample.di.ApplicationTestModule;
import yuki.m.android.dagger2sample.di.DaggerApplicationComponent;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RepositoryViewerActivityTest {

  @Rule
  public ActivityTestRule<RepositoryViewerActivity> activityTestRule
      = new ActivityTestRule<>(RepositoryViewerActivity.class);

  private RepositoryViewerActivity activity;

  @Before
  public void setUp() {
    activity = activityTestRule.getActivity();
  }

  @Test
  public void GitHubレポジトリ情報の取得_商用版() throws Exception {
    onView(ViewMatchers.withId(R.id.fab)).perform(click());
    Thread.sleep(3000);  // for Presentation
  }

  @Test
  public void GitHubレポジトリ情報の取得_テスト版() throws Exception {
    MyApp app = ((MyApp) activity.getApplication());
    app.setComponent(
        DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationTestModule(app))
            .build());
    onView(ViewMatchers.withId(R.id.fab)).perform(click());
    Thread.sleep(3000);  // for Presentation
  }
}