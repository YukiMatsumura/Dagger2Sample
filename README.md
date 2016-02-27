# Dagger2Sample


## Dagger2 Subcomponent vs. dependencies

### はじめに

Dagger2(google/dagger)でComponentの関連性を指定する[@Subcomponent](http://google.github.io/dagger/api/2.0/dagger/Subcomponent.html)と[component dependencies](http://google.github.io/dagger/api/2.0/dagger/Component.html#component-dependencies)についてまとめる. 

Dagger2では依存オブジェクト群を"Component"と呼ばれる単位で管理する. 
このComponentには他Componentと従属関係を築く方法と, 他Componentと使用関係を築く方法の2種類が用意されている. 
さらにDagger2では"Scope"の概念も加わり, このあたりの仕様理解を難しくしている. 

Subcomponentやcomponent dependenciesを使わなくてもDagger2はDI Frameworkとして十分役に立つ. 
ただ, Subcomponentとcomponent dependenciesの理解はDagger2の依存性充足の仕組みを理解するのに大いに役立つため, 知っておくことをお勧めする. 

> **NOTE:**
> 本稿はDagger2 Ver.2.0.2をベースに作成している. 
> Dagger2 Ver.2.1からはややこしい Subcomponent周りにも改良が加えられる様子(@Component.Builder新設etc.)がある. 
> そのため, 本稿ではSubcomponentとcomponent dependenciesの基本に焦点を絞り, その他Tipsなどは非推奨化が見えているのでここでは触れないことにする. 

### 基本的な構成

本稿における用語定義も兼ねてDagger2の基本をおさらいする. 

依存性の充足  
: ClassAがClassBに依存する場合, Dagger2はClassAにClassBのオブジェクトを注入(DI)する. 本稿ではこういった依存性を解決することを依存性の充足(あるいは単に充足)と表現する.  

依存性の要求  
:   Dagger2では[@Inject](https://docs.oracle.com/javaee/6/api/javax/inject/Inject.html)アノテーションで依存性を宣言し, これにより依存オブジェクトをDagger2へ要求することができる. あるいはコンストラクタに@InjectをつけることでそのオブジェクトをDagger2に管理・生成させることができる. 

Component graph  
: Dagger2は依存オブジェクト群をComponent単位で管理する. この時に出来上がる依存オブジェクトのコレクションを本稿ではComponent graph(あるいは単にgraph)と表現する. Dagger1(square/dagger)ではObject graphと呼ばれていたものに相当する. 

依存オブジェクト  
: Dagger2に管理される依存関係の対象となるオブジェクト. 他オブジェクトに必要とされているオブジェクトであったり, 他オブジェクトを要求するオブジェクトであったりする. 

[Module](http://google.github.io/dagger/api/2.0/dagger/Module.html)  
: 依存オブジェクトのファクトリにあたるクラス. 依存性の要求ができないライブラリやシステムにライフサイクル制御されているクラス(Activity等), 生成に構築作業が必要なオブジェクトの場合はModuleでファクトリメソッドを記述する. 

Scope  
: Dagger2では依存オブジェクトとComponent graphのライフサイクルを決めるアノテーションとしてScopeの概念を持つ. Dagger2では依存性を充足させる度に依存オブジェクトを生成するのか, あるいはComponent graph内で一度生成したインスタンスを使い回すのかをScopeで制御できる仕組みを持つ. 

[Component](http://google.github.io/dagger/api/2.0/dagger/Component.html)    
: Component graphの単位. また, Componentは依存オブジェクトとDagger2, あるいは他Componentとの橋渡しの役割を持つ. 

### ComponentとScope

Dagger2の依存性の充足を理解する上で重要な要素はComponentだ.  
Dagger2はPluggable Annotation Processing(JSR 269/APT)の仕組みを使ってDIコンテナにあたるファクトリ群のコードを自動生成する. 
このコード生成に大きく関わってくるのがComponentだ. つまり, Componentの書き方がDagger2の挙動に深く関わってくる. 

細かな点は除いて, Component graphの構造を下記のようにイメージすると理解が早い. 

![Component graphの簡易イメージ](https://2.bp.blogspot.com/-vOnesFIZxG0/VtGeTwG3y1I/AAAAAAAANa4/K9GWkDHHBqs/s1600/component%25E6%25A7%258B%25E6%2588%2590.png)

私たちが定義するComponentはDagger~から始まるComponentの具象クラスを生成する設計図になる. 
Dagger~インスタンスこそがComponent graphの正体であり, 依存オブジェクトを保持するインスタンスである. 
私たちは, このインスタンスから依存オブジェクトを取得したり, @Injectの依存性の要求を充足させたりするのに使うわけだ. 

Component graphはScopeを持ち, Scopeは依存オブジェクトを持つ. 
Dagger2ではComponentがScopeを持つとScopedProviderというクラスによって, Scope毎のファクトリが作られる. 
難しいことはさておき, 依存オブジェクト群はScopeによって管理されているということだ. 

ただ, これだけを見るとScopeの価値がわかり辛い. Scopeをやめて直接Componentが依存オブジェクトを管理しても良いように思える. 
そこでSubcomponentとcomponent dependenciesの登場だ. 

### Subcomponent

ComponentやScopeは階層化できる. 
例えば, 親のComponentを作成し, それの子にあたるComponentを作ることも可能だ. 
子にあたるComponentには'@Subcomponent`のアノテーションをつけて宣言する. 

```java
@Subcomponent(...)
public interface ChildComponent {
``` 

親にあたるComponentは子Componentを作成するAbstract factory methodを親側のComponentで宣言する. 

```java
@Component(...)
public interface ParentComponent {
    ChildComponent newChildComponent(HogeModule hoge);
```

親Componentは子Componentを明示的に宣言し, 子Componentは自身が子であることを宣言する. 
この構造は面白い. 通常の継承関係は子が親を指定するのに対してDagger2では親が子を指定する. 

上記のComponentからどのようなコードが生成されるか見てみよう. 
実は, SubComponentにあたるComponentのクラスファイル(.java)は生成されない. 
その理由はSubComponentが親Componentの内部クラスとして宣言されるからだ. 

```java
// クラス名はそのままComponent間の関係性(Parent-Child)を表している
public final class DaggerParentComponent implements ParentComponent {
    ...
    @Override
    public ChildComponent newChildComponent(HogeModule hoge) {  
        return new ChildComponentImpl(hoge);
    }
    ...

    private final class ChildComponentImpl implements ChildComponent {
        ...
    }
}
```

つまり, 親Componentと子Componentの関係は, 親Componentが子Componentのエンクロージングインスタンスにあたる関係に等しい. 
そして, 子Componentは非staticな内部クラスで宣言されていることから, エンクロージングインスタンスにあたる"親"への参照を保持しており, またそのライフサイクルも親のものより短くなる. 

親, 子それぞれのComponentが持つ依存オブジェクトへの参照範囲について.  
Dagger2のComponentはComponent graphの単位. つまりファクトリ群の単位である. 
親Component, 子Componentを使うクライアント側のコードを見てみよう. 

```java
// 親ComponentのComponent graphを生成
parentComponent = DaggerParentComponent.builder()
        ...
        .build();
parentComponent.inject(this);
```

上記は親Componentを使って依存性を充足させる例である. 
これによって充足される依存性は親Componentに属している依存オブジェクトのものに限られる. 
これは, 生成されたコードからもわかる通り, 親Componentが子Componentへの参照を保持していないからだ. 

次に子Componentの例を見てみる. 

```java
// 親ComponentのComponent graphから子Componentのgraphを生成
childComponent = ((MyApp) getApplication()).getParentComponent()
    .newChildComponent(new ScreenModule(this));
childComponent.inject(this);
```

これによって親Componentと子Componentに属している依存オブジェクト双方から依存性が充足される. 
子Componentはエンクロージングインスタンスである親Componentへの参照を持っているからだ. 

SubComponentによる親, 子それぞれの関係を整理する. 

| -            | 親Component     | 子Component   |
|--------------|-----------------|--------------|
| ライフサイクル | 長い             | 短い          |
| 参照範囲      | 狭い(子を含まない) | 広い(親を含む) |

![SubComponentの関係性](https://4.bp.blogspot.com/-UqKgX7nAJ6c/VtGeT7x-clI/AAAAAAAANa4/AZgx1YO0PKw/s1600/subcomponent%25E6%25A7%258B%25E6%2588%2590.png)

### Scopeの階層化

SubComponent化する際のルールとしてScopeは異なるものでないといけない.  
親と子で同じScopeを持っていては, どちらのComponentに属する依存オブジェクトであるか区別がつかないからだ. 

"Scope"という単語からは, 参照範囲を決定する力を連想するが, 実際にそれを決めるのは前述の通り親Componentと子Componentによるエンクロージングの関係による. 
SubComponent化された状況において, Scopeは"どちらのComponentに属するものか"を問うているにすぎない. 

```java
// 親Component. スコープにはParentScopeを指定.
@ParentScope @Component(...)
public interface ParentComponent { ... }

// 子Component. スコープにはChildScopeを指定.
@ChildScope @Subcomponent(...)
public interface ChildComponent { ... }

// この状況で, @ParentScopeなオブジェクトはParentComponentに属する.
@ParentScope
public class ParentScopeClass {
  @Inject
  public ParentScopeClass() {...}
}

// この状況で, @ChildScopeなオブジェクトはChildComponentに属する.
@ChildScope
public class ChildScopeClass {
  @Inject
  public ChildScopeClass() {...}
}
```

### SubComponentまとめ

 - SubComponentは親Componentと子Componentに強い結合をもたらす. 
 - 親は子Componentを生成するAbstract factory methodを宣言する. 
 - 子は@Subcomponentでアノテートする. 
 - 親Componentが子Componentのエンクロージングインスタンスとして関係を持つ. 
 - 親は子Componentの依存オブジェクトを参照した充足ができない. 
 - 子は親Componentの依存オブジェクトを参照した充足ができる. 
 - Scopeは"どちらのComponentに属するか"を決定する要素になる. 

例えば, アプリのライフサイクルに合わせて生存するDatabaseのような依存オブジェクトは親Componentに割り当てる. 
Activityのような複数個/複数回生成されるクラスに紐づく依存オブジェクトは子Componentとして定義するような分け方もできる. 
あるいはユーザログイン/ログアウトといった特定の区間だけ生存する依存オブジェクトもこれで実現できる. 
子Componentのライフサイクルを開始したければ親Componentが宣言したAbstract factory methodを呼べばよい. 子Componentのライフサイクルを終了したければ子Componentのインスタンス(SubComponent graph)を破棄すればよい. 


### dependencies

SubComponentをおさえたところで, Dagger2にはこれを混乱させる要素がもう1つある. dependenciesパラメータだ. 

Componentアノテーションはパラメータにdependenciesを指定することができる. 
これにより, Componentは他Componentに依存することができる. 
この概念はSubComponentの概念と似通っているものの内部構造は大きく異なる. 

```java
// @Subcomponentアノテーションではない点に注意
@Component(dependencies = DependeeComponent.class, ...)
public interface DependerComponent {
```

dependenciesによる依存の要点は下記である. 

 1. dependenciesで指定されたComponentを使って依存性を充足させることができる
 2. ただし, その場合はdependenciesで指定された側が依存性をexportする必要がある

dependenciesを宣言して依存する側(dependerComponent)と, dependenciesの宣言で指定された依存される側(dependeeComponent)について. 
dependerComponentはdependeeComponentの依存オブジェクトを使って自身が持つComponent graphの依存性を充足させることができる. 

```java
// 依存する側
@Component(dependencies = DependeeComponent.class, ...)
public interface DependerComponent { ... }

// 依存される側
@Component(modules = {HogeModule.class})
public interface DependeeComponent { ... }
```

これによってDependerComponentはDependeeComponentを使って依存性を充足させる. 
SubComponentの例をみると, DependerComponentがChildComponent, DependeeComponentがParentComponentのように見えるが, 関係性を指定する方向が逆転しているのがわかる. 

生成されるコードに目をやると, SubComponentの時とは違って, DependerComponentのクラスファイル(.java)が生成され, DaggerDependerComponentクラスが出来上がる. 

その中身は単純で, DependeeComponentに直接アクセスしてDependeeComponentがもつ依存オブジェクトを取得することで依存性充足を実現していることがわかる. 

```java
public final class DaggerDependerComponent implements DependerComponent {
  // DependeeComponentが持つ依存オブジェクト
  private Provider<ParentScopeClass> parentScopeClassProvider;
  ...
  private void initialize(final Builder builder) {  
    this.parentScopeClassProvider = new Factory<ParentScopeClass>() {
      private final DependeeComponent dependeeComponent = builder.dependeeComponent;
      @Override public ParentScopeClass get() {
        // DependeeComponentの@Provideメソッド経由で依存オブジェクト取得
        ParentScopeClass provided = dependeeComponent.parentScopeClass();
        ...
      }
    ...
  }
  ...
```

上記コードからわかるように, DependerComponentがDependeeComponentの依存オブジェクトを参照できるようにするためには, DependeeComponent側に依存オブジェクトを参照できるExportメソッドを定義する必要がある. 
(上記の例では`parentScopeClass`メソッドがそれにあたる) 


```java
// 依存する側
@Component(dependencies = DependeeComponent.class, ...)
public interface DependerComponent { ... }

// 依存される側
@Component(modules = {HogeModule.class})
public interface DependeeComponent {
  // DependerComponentが要求するParentScopeオブジェクトをexportする必要がある
  ParentScopeClass parentScopeClass();
}
```

これらのComponentを使うクライアント側のコードを見てみよう. 

```java
// dependeeComponentのComponent graphを生成
dependeeComponent = DaggerDependeeComponent.builder()
        .databaseModule(new DatabaseModule(this))
        .build();
dependeeComponent.inject(this);

// dependerComponentのComponent graphを生成
dependerComponent = DaggerDependerComponent.builder()
        .dependeeComponent(((MyApp) getApplication()).getDependeeComponent())
        .screenModule(new ScreenModule(this))
        .build();
dependerComponent.inject(this);
```

dependenciesはSubComponentとは異なり, Compnentが別のComponentを生成するということはない. 
depencenciesを宣言する側, つまり依存を要求する側のdependerComponentが必要な依存性を持ったdependeeComponentを自身のComponent graph生成時に組み込むといった形で充足を実現するわけだ. 

### dependenciesとScope

dependenciesによるComponent間の依存関係においてもScopeは異なるものにしなければならない. 
その理由はSubComponentの場合と同じである. 

### SubComponent vs. dependencies

本題のSubComponentかdependencies, どちらを選択するべきかについて. 

SubComponentは親Componentが子Componentを指定・生成し, 子Componentは@Subcomonentで明示的に宣言される. 
一方で, dependenciesはSubComponentとは関係性の指定方向が逆転しており, 依存する側のComponentがdependenciesパラメータで依存先を宣言する. 依存先のComponentは必要な依存オブジェクトをExportするメソッドを宣言する. 

dependenciesは依存される側のComponentで定義されるexportメソッドにより暗黙的な結合が生まれている. 
一方でこれは関係するComponentを直接指定するSubComponentに比べて緩い結合である. 
dependenciesは他Componentの依存性充足を手軽に拝借できるところが利点ともいえる. 
ただ, "静的なファクトリさ"を持つDagger2の特性からみても, この"機敏さ"にはさほど魅力を感じられない. 

ファクトリコードは複雑化しやすい. そのため, 多少の面倒さはあっても将来ファクトリが複雑化する可能性があるのであれば, Componentの関係性を明確に定義するSubComponentを使いたい. 


### 補足

Dagger2ではScopeの定義が必須ではない. Scopeでアノテートしない場合, インスタンスはScopedProviderではなくModuleのファクトリで管理される. 
本稿ではSubComponentとcomponent dependenciesに焦点を絞り, 説明をわかりやすくするためにあえてScopeを宣言しないケースについては除外した. 
SubComponentやdependenciesでScopeを宣言しないProviderを作成した場合, 通常通りそのComponent/Moduleにファクトリがつく. 

以上. 
