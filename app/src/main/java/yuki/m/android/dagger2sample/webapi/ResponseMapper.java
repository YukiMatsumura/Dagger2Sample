package yuki.m.android.dagger2sample.webapi;

import java.util.ArrayList;
import java.util.List;

import yuki.m.android.dagger2sample.domain.GitHubRepository;

public class ResponseMapper {
    public List<GitHubRepository> transform(List<GitHubApiResponse.Repository> response) {
        if (response == null) {
            return null;
        }

        ArrayList<GitHubRepository> repositories = new ArrayList<>(response.size());
        for (GitHubApiResponse.Repository repository : response) {
            GitHubRepository entity = new GitHubRepository();
            entity.setId(repository.getId());
            entity.setName(repository.getName());
            repositories.add(entity);
        }

        return repositories;
    }
}
