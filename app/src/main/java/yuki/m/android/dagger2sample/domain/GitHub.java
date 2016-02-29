package yuki.m.android.dagger2sample.domain;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import yuki.m.android.dagger2sample.di.ActivityScope;
import yuki.m.android.dagger2sample.store.GitHubStore;
import yuki.m.android.dagger2sample.webapi.GitHubWebService;

@ActivityScope
public class GitHub {

  private GitHubWebService webApi;

  private GitHubStore store;

  private Scheduler executionScheduler;

  private Scheduler postScheduler;

  @Inject
  public GitHub(GitHubWebService webApi, GitHubStore store,
                @Named("executeScheduler") Scheduler executionScheduler,
                @Named("postScheduler") Scheduler postScheduler) {
    this.webApi = webApi;
    this.store = store;
    this.executionScheduler = executionScheduler;
    this.postScheduler = postScheduler;
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
        .subscribeOn(executionScheduler)
        .observeOn(postScheduler)
        .subscribe(onNext, onError);
  }
}
