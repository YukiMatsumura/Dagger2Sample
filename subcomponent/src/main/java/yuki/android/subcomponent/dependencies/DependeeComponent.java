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
   *   DependeeComponentはDependerComponentが要求する依存オブジェクトを
   *   Exportして依存性を充足される必要がある.
   *   dependenciesによる関連では依存コンポーネントの情報が現れず暗黙的な関連になる.
   *
   * SubComponent vs. dependencies
   *   ParentComponentクラスのコメントを参照.
   */

  ParentScopeClass parentScopeClass();

  MyApp myApp();

  void inject(MyApp myApp);
}
