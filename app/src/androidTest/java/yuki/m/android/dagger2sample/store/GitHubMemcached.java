package yuki.m.android.dagger2sample.store;

import java.util.Collections;
import java.util.List;

import yuki.m.android.dagger2sample.domain.GitHubRepository;

public class GitHubMemcached implements GitHubStore {
  @Override public List<GitHubRepository> queryRepository() {
    GitHubRepository repository = new GitHubRepository();
    repository.setId("TEST_ID");
    repository.setName("**TEST** これはテストレポジトリ **TEST**");
    return Collections.singletonList(repository);
  }

  @Override public boolean updateRepository(List<GitHubRepository> repositories) {
    return true;
  }
}
