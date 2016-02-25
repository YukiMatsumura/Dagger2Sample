package yuki.m.android.dagger2sample.di;

import dagger.Component;
import yuki.m.android.dagger2sample.store.GitHubStore;
import yuki.m.android.dagger2sample.webapi.GitHubWebService;

@ApplicationScope
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

  // exported for child
  GitHubStore githubStore();

  GitHubWebService githubWebApi();
}
