package yuki.android.subcomponent.subcomponent;

import dagger.Component;
import yuki.android.subcomponent.MyApp;
import yuki.android.subcomponent.module.DatabaseModule;
import yuki.android.subcomponent.module.ScreenModule;
import yuki.android.subcomponent.scope.ParentScope;

@ParentScope
@Component(modules = {DatabaseModule.class})
public interface ParentComponent {
    /*
     * 親コンポーネント.
     *
     * Point
     *   ParentComponentはChildComponentへの依存(親子関係)を
     *   Abstract factory methodの定義をもって明示する.
     *
     *   このメソッドはSubComponentのインスタンス生成を伴う.
     *
     * SubComponent vs. dependencies
     *   dependenciesによる関連では, ParentComponentにChildComponentが依存している
     *   オブジェクトのAbstract factory methodを定義する必要がある.
     *   これはChildComponentの詳細に強く依存し, 関連も暗黙的なものとなる.
     *
     *   SubComponentによる関係性では依存オブジェクトではなく, Componentとの関係を
     *   明示するため, dependenciesの課題である暗黙的な関係は解消される.
     *   また, ChildComponentのカプセル化も促進され, ChildComponentの変更に強くなる.
     *
     * ChildComponent Constructor
     *   依存するChildComponentが引数有りのコンストラクタを持つ場合は,
     *   このAbstract factory methodの引数としてそれを指定する必要がある.
     *   この宣言から生成されるコードは下記.
     *     @Override
     *     public ChildComponent newChildComponent(ScreenModule screenModule) {  
     *         return new ChildComponentImpl(screenModule);
     *     }
     *
     *   ParentComponentがChildComponentの要求するModule(ScreenModule)を知っていることは
     *   Component間に余計な結合を生む. この辺を解消する@Component.BuilderがAPI 2.1から生えている.
     */
    ChildComponent newChildComponent(ScreenModule screenModule);

    /*
     * この宣言はMyAppクラスをApplicationScopeに属させる
     */
    void inject(MyApp myApp);
}
