package yuki.m.android.dagger2sample.di;

import dagger.Module;
import yuki.m.android.dagger2sample.MyApp;
import yuki.m.android.dagger2sample.store.GitHubDatabase;
import yuki.m.android.dagger2sample.store.GitHubMemcached;
import yuki.m.android.dagger2sample.store.GitHubStore;

@Module
public class ApplicationTestModule extends ApplicationModule {

  public ApplicationTestModule(MyApp app) {
    super(app);
  }

  @Override public GitHubStore provideGitHubStore(GitHubDatabase store) {
    return new GitHubMemcached();
  }
}
