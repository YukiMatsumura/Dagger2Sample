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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    childComponent = ((MyApp) getApplication()).getParentComponent()
        .newChildComponent(new ScreenModule(this));
    childComponent.inject(this);
    Log.i("yuki", "ChildComponent injection:");
    Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass);
    Log.i("yuki", "\tactivityScopeClass=" + childScopeClass);
    Log.i("yuki", "\tlocalTime=" + localTime);
  }

  @OnClick(R.id.fab)
  public void onFabClick(FloatingActionButton fab) {

  }
}
