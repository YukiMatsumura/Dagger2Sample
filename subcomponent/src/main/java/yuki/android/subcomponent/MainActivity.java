package yuki.android.subcomponent;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yuki.android.subcomponent.dependencies.DaggerDependeeComponent;
import yuki.android.subcomponent.dependencies.DaggerDependerComponent;
import yuki.android.subcomponent.dependencies.DependeeComponent;
import yuki.android.subcomponent.dependencies.DependerComponent;
import yuki.android.subcomponent.module.DatabaseModule;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.subcomponent.ChildComponent;

public class MainActivity extends AppCompatActivity {
  /*
   * ChildScopeに属するMyActivityからは, 包含するParentScopeに属する
   * ParentScopeClassを参照できる.
   */
  @Inject
  ParentScopeClass parentScopeClass;

  @Inject
  ChildScopeClass childScopeClass;

  @Inject
  LocalTime localTime;

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.fab)
  FloatingActionButton fab;

  private ChildComponent childComponent;

  private DependerComponent dependerComponent;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    Log.i("yuki", "ChildComponent build ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
    MyApp app = ((MyApp) getApplication());
    childComponent = ((MyApp) getApplication()).getParentComponent()
        .newChildComponent(new ScreenModule(this));
    childComponent.inject(this);

    Log.i("yuki", "ChildComponent build " + childComponent.hashCode());
    Log.i("yuki", "ChildComponent injection result:");
    Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass.hashCode());
    Log.i("yuki", "\tactivityScopeClass=" + childScopeClass.hashCode());
    Log.i("yuki", "\tlocalTime=" + localTime.hashCode());
    Log.i("yuki", "ChildComponent build ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

    parentScopeClass = null;
    childScopeClass = null;
    localTime = null;

    Log.i("yuki", "DependerComponent build ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
    dependerComponent = DaggerDependerComponent.builder()
        .dependeeComponent(app.getDependeeComponent())
        .screenModule(new ScreenModule(this))
        .build();
    dependerComponent.inject(this);

    Log.i("yuki", "DependerComponent build " + dependerComponent.hashCode());
    Log.i("yuki", "DependerComponent injection result:");
    Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass.hashCode());
    Log.i("yuki", "\tactivityScopeClass=" + childScopeClass.hashCode());
    Log.i("yuki", "\tlocalTime=" + localTime.hashCode());
    Log.i("yuki", "DependerComponent build ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
  }

  @OnClick(R.id.fab)
  public void onFabClick(FloatingActionButton fab) {

  }

  public MainActivity() {
    Log.i("yuki", "new MainActivity() " + this.hashCode());
  }
}
