package yuki.android.subcomponent.module;

import dagger.Module;
import dagger.Provides;
import yuki.android.subcomponent.MyApp;
import yuki.android.subcomponent.scope.ParentScope;

@Module
public class DatabaseModule {

  MyApp app;

  public DatabaseModule(MyApp app) {
    this.app = app;
    // 省略...
  }

  @Provides
  @ParentScope
  public MyApp app() {
    return app;
  }
}