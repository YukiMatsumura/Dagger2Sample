package yuki.m.android.dagger2sample.domain;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yuki.m.android.dagger2sample.di.ActivityScope;
import yuki.m.android.dagger2sample.store.GitHubStore;
import yuki.m.android.dagger2sample.webapi.GitHubWebService;

@ActivityScope
public class GitHubUseCase {

  @Inject
  GitHubWebService webApi;

  @Inject
  GitHubStore store;

  @Inject
  public GitHubUseCase() {
  }

  public void findRepository(Action1<List<GitHubRepository>> onNext, Action1<Throwable> onError) {
    Observable.<List<GitHubRepository>>create(subscriber -> {
      // まずはキャッシュを通知
      subscriber.onNext(store.queryRepository());

      // 最新の情報を取得
      List<GitHubRepository> newer = webApi.requestGitHubRepository("YukiMatsumura");
      if (newer == null || newer.isEmpty()) {
        subscriber.onCompleted();
        return;
      }

      // キャッシュを更新
      boolean updated = store.updateRepository(newer);
      if (!updated) {
        subscriber.onCompleted();
        return;
      }

      // 更新したキャッシュで再通知
      subscriber.onNext(store.queryRepository());
      subscriber.onCompleted();
    })
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError);
  }
}
