package yuki.m.android.dagger2sample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;
import javax.inject.Provider;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.Lazy;
import yuki.m.android.dagger2sample.di.ActivityComponent;
import yuki.m.android.dagger2sample.di.ActivityModule;
import yuki.m.android.dagger2sample.domain.GitHub;

public class RepositoryViewerActivity extends AppCompatActivity {

    private ActivityComponent activityComponent;

    @Inject
    Lazy<GitHub> github;

    @Inject
    Provider<LocalTime> localTimeProvider;

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

        activityComponent = ((MyApp) getApplication()).getComponent()
                .newActivityComponent(new ActivityModule());
        activityComponent.inject(this);
    }

    @OnClick(R.id.fab)
    public void onFabClick(FloatingActionButton fab) {
        github.get().findRepository(
                find -> text.setText(localTimeProvider.get() + "\n" + find.toString()),
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show()
        );
    }
}
