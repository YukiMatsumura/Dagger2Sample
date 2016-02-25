package yuki.m.android.dagger2sample.di;

import dagger.Component;
import yuki.m.android.dagger2sample.RepositoryViewerActivity;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

  /*
   * フィールドインジェクションの発火元.
   * Activityのライフサイクルはシステム制御されるため明示的にインジェクションが必要.
   * インジェクション対象のクラスは具象クラスを引数にとる必要がある.
   */
  void inject(RepositoryViewerActivity activity);
}
