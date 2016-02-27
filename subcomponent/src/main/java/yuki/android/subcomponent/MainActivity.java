package yuki.android.subcomponent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import yuki.android.subcomponent.dependencies.DaggerDependerComponent;
import yuki.android.subcomponent.dependencies.DependerComponent;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.subcomponent.ChildComponent;

public class MainActivity extends AppCompatActivity {
    // ParentScopeに属するオブジェクト
    @Inject
    ParentScopeClass parentScopeClass;

    // ChildScopeに属するオブジェクト
    @Inject
    ChildScopeClass childScopeClass;

    // ChildScopeに属するオブジェクト
    @Inject
    LocalTime localTime;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private ChildComponent childComponent;

    private DependerComponent dependerComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        MyApp app = ((MyApp) getApplication());

        Log.i("yuki", "ChildComponent build ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        // newChildComponentにより, 新たなChildComponent/ChildScopeのgraphが生成される.
        // ただし, 親にあたるParentScopeのオブジェクト群は引き継がれる.
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
        // DependerComponentのbuildにより, 新たなDependerComponent/ChildScopeのgraphが生成される.
        // 親にあたるParentScopeは既存のものを使用するためParentScopeのオブジェクト群は引き継がれる.
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

    // for debug
    public MainActivity() {
        Log.i("yuki", "new MainActivity() " + this.hashCode());
    }
}
