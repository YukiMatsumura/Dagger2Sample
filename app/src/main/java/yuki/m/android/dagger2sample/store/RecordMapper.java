package yuki.m.android.dagger2sample.store;

import java.util.ArrayList;
import java.util.List;

import yuki.m.android.dagger2sample.domain.GitHubRepository;

public class RecordMapper {
    public List<GitHubRepository> transform(List<GitHubRepositoryRecord> response) {
        if (response == null) {
            return null;
        }

        ArrayList<GitHubRepository> repositories = new ArrayList<>(response.size());
        for (GitHubRepositoryRecord repository : response) {
            GitHubRepository entity = new GitHubRepository();
            entity.setId(String.valueOf(repository.id));
            entity.setName(repository.name);
            repositories.add(entity);
        }

        return repositories;
    }
}
