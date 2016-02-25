package yuki.m.android.dagger2sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yuki.m.android.dagger2sample.di.ActivityComponent;
import yuki.m.android.dagger2sample.di.ActivityModule;
import yuki.m.android.dagger2sample.di.DaggerActivityComponent;
import yuki.m.android.dagger2sample.domain.GitHubUseCase;

public class RepositoryViewerActivity extends AppCompatActivity {

  private ActivityComponent activityComponent;

  @Inject
  GitHubUseCase useCase;

  @Bind(R.id.toolbar)
  Toolbar toolbar;

  @Bind(R.id.fab)
  FloatingActionButton fab;

  @Bind(R.id.text)
  TextView text;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
    setSupportActionBar(toolbar);

    activityComponent = DaggerActivityComponent.builder()
        .applicationComponent(((MyApp) getApplication()).getComponent())
        .activityModule(new ActivityModule())
        .build();
    activityComponent.inject(this);
  }

  @OnClick(R.id.fab)
  public void onFabClick(FloatingActionButton fab) {
    useCase.findRepository(
        find -> text.setText(find.toString()),
        error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show()
    );
  }
}
