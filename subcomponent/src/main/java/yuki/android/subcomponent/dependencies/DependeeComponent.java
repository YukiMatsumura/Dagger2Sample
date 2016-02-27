package yuki.android.subcomponent.dependencies;

import dagger.Component;
import yuki.android.subcomponent.MyApp;
import yuki.android.subcomponent.ParentScopeClass;
import yuki.android.subcomponent.module.DatabaseModule;
import yuki.android.subcomponent.scope.ParentScope;

@ParentScope
@Component(modules = {DatabaseModule.class})
public interface DependeeComponent {
    /*
     * 依存される(dependenciesで指定される)コンポーネント.
     *
     * Point
     *   ChildScopeのDependeeComponentが異なるスコープであるParentScopeの
     *   オブジェクトを要求する時, DependerComponentはその依存性を充足されるために
     *   当該オブジェクトをExportする必要がある. 
     *   ChildScopeはexportされたメソッドを経由して異なるScopeのオブジェクトを取得する.
     *   
     *   dependenciesによる関係性は対象Componentの情報がコード上現れず暗黙的な関連になる.
     *   DependerComponentが別のParentScopeオブジェクトを求める時, DependeeComponent
     *   は不足した依存性を充足させる変更が必要になる. 
     *
     * SubComponent vs. dependencies
     *   ParentComponentクラスのコメントを参照.
     */

    // DependerComponentが要求するParentScopeオブジェクトをexportする
    ParentScopeClass parentScopeClass();

    // DependerComponentが要求するParentScopeオブジェクトをexportする
    MyApp myApp();

    void inject(MyApp myApp);
}
