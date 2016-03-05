package yuki.m.android.dagger2sample.store;

import com.github.gfx.android.orma.TransactionTask;

import java.util.List;

import javax.inject.Inject;

import yuki.m.android.dagger2sample.di.ApplicationScope;
import yuki.m.android.dagger2sample.domain.GitHubRepository;

@ApplicationScope
public class GitHubDatabase implements GitHubStore {

    private OrmaDatabase orma;

    private RecordMapper recordMapper = new RecordMapper();

    @Inject
    public GitHubDatabase(OrmaDatabase orma) {
        this.orma = orma;
    }

    public List<GitHubRepository> queryRepository() {
        return recordMapper.transform(
                orma.selectFromGitHubRepositoryRecord().toList());
    }

    public boolean updateRepository(List<GitHubRepository> repositories) {
        // TODO: Impl. If-Modified-Since and Update Check
        orma.transactionSync(new TransactionTask() {
            @Override
            public void execute() throws Exception {
                orma.deleteFromGitHubRepositoryRecord();
                for (GitHubRepository repository : repositories) {
                    orma.insertIntoGitHubRepositoryRecord(
                            new GitHubRepositoryRecord(Long.valueOf(repository.getId()), repository.getName()));
                }
            }
        });
        return true;
    }
}
