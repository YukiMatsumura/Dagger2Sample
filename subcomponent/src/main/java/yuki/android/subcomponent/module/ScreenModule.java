package yuki.android.subcomponent.module;

import android.app.Activity;
import android.util.Log;

import org.threeten.bp.LocalTime;

import dagger.Module;
import dagger.Provides;
import yuki.android.subcomponent.MyApp;
import yuki.android.subcomponent.scope.ChildScope;

@Module
public class ScreenModule {

  private Activity activity;

  public ScreenModule(Activity activity) {
    Log.i("yuki", "new ScreenModule(activity) arg.activity=" + activity.hashCode());
    this.activity = activity;
  }

  /*
   * ChildScopeにあたるコンポーネントがParentScopeにあたるオブジェクトを
   * 要求する場合を想定.
   */
  @Provides
  @ChildScope
  public LocalTime localTime(MyApp myApp) {
    Log.i("yuki", "provide localTime arg.myApp=" + myApp.hashCode());
    return LocalTime.now();
  }
}