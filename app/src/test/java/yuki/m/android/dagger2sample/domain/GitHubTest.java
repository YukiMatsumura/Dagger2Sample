package yuki.m.android.dagger2sample.domain;

import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.schedulers.Schedulers;
import yuki.m.android.dagger2sample.store.GitHubStore;
import yuki.m.android.dagger2sample.webapi.GitHubWebService;

@SmallTest
public class GitHubTest {

    @Test
    public void GitHubレポジトリ情報の取得() throws Exception {
        GitHub github = new GitHub(
                // GitHubWebApiのレスポンスをモック
                new GitHubWebService(null) {
                    @Override
                    public List<GitHubRepository> requestGitHubRepository(String userName) {
                        GitHubRepository newRepository = new GitHubRepository();
                        newRepository.setId("* TEST_ID_2");
                        newRepository.setName("* TEST_REPOSITORY_NAME_2");
                        return Collections.singletonList(newRepository);
                    }
                },
                // GitHubStoreをメモリキャッシュ＆固定レスポンス
                new GitHubStore() {
                    private List<GitHubRepository> cached = new ArrayList<>();

                    {
                        GitHubRepository repository = new GitHubRepository();
                        repository.setId("TEST_ID_1");
                        repository.setName("TEST_REPOSITORY_NAME_1");
                        cached.add(repository);
                    }

                    @Override
                    public List<GitHubRepository> queryRepository() {
                        return cached;
                    }

                    @Override
                    public boolean updateRepository(List<GitHubRepository> repositories) {
                        cached = repositories;
                        return true;
                    }
                },
                Schedulers.immediate(),
                Schedulers.immediate()
        );
        github.findRepository(
                gitHubRepositories -> System.out.println("onNext: " + gitHubRepositories),
                throwable -> System.out.println("onError:" + throwable)
        );

        // verify省略
    }
}
