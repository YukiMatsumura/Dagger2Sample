package yuki.m.android.dagger2sample.di;

import dagger.Module;
import dagger.Provides;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import yuki.m.android.dagger2sample.MyApp;
import yuki.m.android.dagger2sample.store.GitHubDatabase;
import yuki.m.android.dagger2sample.store.GitHubStore;
import yuki.m.android.dagger2sample.store.OrmaDatabase;
import yuki.m.android.dagger2sample.webapi.GitHubWebService.GitHubWebApi;

@Module
public class ApplicationModule {

  // Field injection vs. Constructor injection
  //   DaggerのDIコンテナはUnitTestで準備するのが手間である.
  //   それよりはS.U.T.の初期化時に依存コンポーネントを注入できる
  //   コンストラクタインジェクションの方がやりやすい.
  //   フィールドインジェクションも使えないわけではないが,
  //   インスタンスの初期化～構築完了までの間で不整合の発生する期間があり,
  //   オプショナルなインジェクションや遅延初期化を必要としないのであれば
  //   コンストラクタインジェクションの方が望ましい.
  //
  //   どうしてもフィールドインジェクションを選択せざるを得ない場合,
  //   @VisibleForTestingなコンストラクタをS.U.T.に用意することも可能.

  private final MyApp app;

  /*
   * モジュール自身のコンストラクタで引数を取り, 値を保持することもできる.
   */
  public ApplicationModule(MyApp app) {
    this.app = app;
  }

  /*
   * 3rdPartyライブラリのインスタンスをInjectするために必要な生成処理.
   * モジュール自身のコンストラクタで受け取ったApplicationインスタンスを
   * 使用することもできる.
   */
  @Provides
  @ApplicationScope
  public OrmaDatabase provideOrmaDatabase() {
    return OrmaDatabase.builder(app.getApplicationContext()).build();
  }

  /*
   * インターフェースの依存性を充足させるために必要な具象クラスの生成処理.
   * return new GitHubDatabase() とする場合は　GitHubDatabaseのフィールドインジェクション
   * は行われないため手動で依存性を充足させる必要がある.
   */
  @Provides
  @ApplicationScope
  public GitHubStore provideGitHubStore(GitHubDatabase store) {
    return store;
  }

  /*
   * インスタンス生成＋構築を伴う生成処理.
   */
  @Provides
  @ApplicationScope
  public Retrofit provideGitHubRetrofit() {
    return new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
  }

  /*
   * provideGitHubRetrofit()で生成されるインスタンスを引数にとる生成処理.
   */
  @Provides
  @ApplicationScope
  public GitHubWebApi provideGitHubService(Retrofit retrofit) {
    return retrofit.create(GitHubWebApi.class);
  }
}