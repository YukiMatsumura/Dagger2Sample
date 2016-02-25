package yuki.m.android.dagger2sample.webapi;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import yuki.m.android.dagger2sample.di.ApplicationScope;
import yuki.m.android.dagger2sample.domain.GitHubRepository;

@ApplicationScope
public class GitHubWebService {

  private GitHubWebApi webApi;

  private ResponseMapper responseMapper = new ResponseMapper();

  public interface GitHubWebApi {
    @GET("/users/{user}/repos")
    Call<List<RepositoryWebApiResponse>> getRepositories(@Path("user") String userName);
  }

  @Inject
  public GitHubWebService(GitHubWebApi webApi) {
    this.webApi = webApi;
  }

  public List<GitHubRepository> requestGitHubRepository(String userName) {
    try {
      List<RepositoryWebApiResponse> response
          = webApi.getRepositories(userName).execute().body();
      return responseMapper.transform(response);

    } catch (IOException error) {
      throw new RuntimeException(error);
    }
  }
}
