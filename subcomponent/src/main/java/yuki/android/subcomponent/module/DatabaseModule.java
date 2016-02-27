package yuki.android.subcomponent.module;

import android.util.Log;

import dagger.Module;
import dagger.Provides;
import yuki.android.subcomponent.MyApp;
import yuki.android.subcomponent.scope.ParentScope;

@Module
public class DatabaseModule {

  MyApp myApp;

  public DatabaseModule(MyApp myApp) {
    Log.i("yuki", "new DatabaseModule(myApp) arg.myApp=" + myApp.hashCode());
    this.myApp = myApp;
    // 省略...
  }

  @Provides
  @ParentScope
  public MyApp myApp() {
    Log.i("yuki", "provide myApp " + myApp.hashCode());
    return myApp;
  }
}