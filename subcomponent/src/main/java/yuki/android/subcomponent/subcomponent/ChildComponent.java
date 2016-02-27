package yuki.android.subcomponent.subcomponent;

import dagger.Subcomponent;
import yuki.android.subcomponent.MainActivity;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.scope.ChildScope;

@ChildScope
@Subcomponent(modules = ScreenModule.class)
public interface ChildComponent {
    /* 
     * 子コンポーネント
     * 
     * Scope
     *   SubComponentはParentComponentのScopeと異なる必要がある.
     *   Scopeを管理するScopeProviderが依存オブジェクトをどのScopeに割り当てるべきか
     *   明確にする必要がある.
     *
     *   Scope自身が大小(包含)関係を明示する術はなく,
     *   ParentComponent, ChildComponentの関係性がScopeの階層(範囲)を決める.
     *
     *   ChildComponentのScopeはParentComponentのScopeより短命となる.
     *   これはChildComponentがParentComponentにより生成されるためである.
     */

    /*
     * この宣言はMyAppクラスをApplicationScopeに属させる
     */
    void inject(MainActivity activity);
}
