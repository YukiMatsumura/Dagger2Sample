package yuki.android.subcomponent;

import android.app.Application;
import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import yuki.android.subcomponent.module.DatabaseModule;
import yuki.android.subcomponent.subcomponent.DaggerParentComponent;
import yuki.android.subcomponent.subcomponent.ParentComponent;

public class MyApp extends Application {

  @Inject
  ParentScopeClass parentScopeClass;

  /*
   * ParentScopeに属するMyAppからでは, より広範囲のスコープを持つ
   * ChildScopeのオブジェクトを参照できない.
   */
  ChildScopeClass childScopeClass;

  private ParentComponent parentComponent;

  @Override public void onCreate() {
    super.onCreate();
    AndroidThreeTen.init(this);

    parentComponent = DaggerParentComponent.builder()
        .databaseModule(new DatabaseModule(this))
        .build();
    parentComponent.inject(this);
    Log.i("yuki", "ParentComponent injection. " + parentComponent);
    Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass);
    Log.i("yuki", "\tactivityScopeClass=" + childScopeClass);
  }

  public ParentComponent getParentComponent() {
    return parentComponent;
  }
}
