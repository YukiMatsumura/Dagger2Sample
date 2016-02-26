package yuki.android.subcomponent.subcomponent;

import dagger.Subcomponent;
import yuki.android.subcomponent.MainActivity;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.scope.ChildScope;

@ChildScope
@Subcomponent(modules = ScreenModule.class)
public interface ChildComponent {
  /*
   * Scope
   *   SubComponentはParentComponentのScopeと異なるものを定義する必要がある.
   *   Scopeを管理するScopeProviderをParentComponent, ChildComponentで混同しない
   *   ためである.
   *
   *   Scopeはそれ自身が大小(包含)関係を明示するものではなく,
   *   ParentComponent, ChildComponentの関係性がScopeの範囲を決めるものである.
   *
   *   ChildComponentのScopeはParentComponentのScopeより短命となる.
   *   これはChildComponentがParentComponentにより生成されるためである.
   */

  /*
   * この宣言はMyAppクラスをApplicationScopeに属させる
   */
  void inject(MainActivity activity);
}
