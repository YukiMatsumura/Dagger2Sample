package yuki.m.android.dagger2sample.store;

import java.util.List;

import yuki.m.android.dagger2sample.domain.GitHubRepository;

public interface GitHubStore {

    List<GitHubRepository> queryRepository();

    boolean updateRepository(List<GitHubRepository> repositories);
}
