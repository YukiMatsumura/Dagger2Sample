package yuki.android.subcomponent;

import android.app.Application;
import android.util.Log;

import com.jakewharton.threetenabp.AndroidThreeTen;

import javax.inject.Inject;

import yuki.android.subcomponent.dependencies.DaggerDependeeComponent;
import yuki.android.subcomponent.dependencies.DependeeComponent;
import yuki.android.subcomponent.module.DatabaseModule;
import yuki.android.subcomponent.subcomponent.DaggerParentComponent;
import yuki.android.subcomponent.subcomponent.ParentComponent;

public class MyApp extends Application {

    @Inject
    ParentScopeClass parentScopeClass;

    // ParentScopeに属するMyAppから, より広いスコープを持つChildScopeのオブジェクトを参照できない.
    ChildScopeClass childScopeClass;

    private ParentComponent parentComponent;

    private DependeeComponent dependeeComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);

        Log.i("yuki", "ParentComponent build ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");
        // ParentComponentのbuildにより, 新たなParentComponent/ParentScopeが生成される.
        parentComponent = DaggerParentComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
        parentComponent.inject(this);
        Log.i("yuki", "ParentComponent build " + parentComponent.hashCode());
        Log.i("yuki", "ParentComponent injection result:");
        Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass.hashCode());
        Log.i("yuki", "\tactivityScopeClass=" + childScopeClass);
        Log.i("yuki", "ParentComponent build ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");

        parentScopeClass = null;
        childScopeClass = null;

        Log.i("yuki", "DependeeComponent build ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓");

        // DependeeComponentのbuildにより, 新たなDependeeComponent/ParentScopeが生成される.
        dependeeComponent = DaggerDependeeComponent.builder()
                .databaseModule(new DatabaseModule(this))
                .build();
        dependeeComponent.inject(this);
        Log.i("yuki", "DependeeComponent build " + dependeeComponent.hashCode());
        Log.i("yuki", "DependeeComponent injection result:");
        Log.i("yuki", "\tapplicationScopeClass=" + parentScopeClass.hashCode());
        Log.i("yuki", "\tactivityScopeClass=" + childScopeClass);
        Log.i("yuki", "DependeeComponent build ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑");
    }

    public ParentComponent getParentComponent() {
        return parentComponent;
    }

    public DependeeComponent getDependeeComponent() {
        return dependeeComponent;
    }

    // for debug
    public MyApp() {
        Log.i("yuki", "new MyApp() " + this.hashCode());
    }
}
