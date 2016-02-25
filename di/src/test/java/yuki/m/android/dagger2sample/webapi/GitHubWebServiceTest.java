package yuki.m.android.dagger2sample.webapi;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import yuki.m.android.dagger2sample.domain.GitHubRepository;
import yuki.m.android.dagger2sample.webapi.GitHubWebService.GitHubWebApi;

public class GitHubWebServiceTest {

  @Before
  public void setup() {
  }

  @Test
  public void 商用サーバを使ってGitHubレポジトリを取得() throws Exception {
    GitHubWebApi webApi = new Retrofit.Builder()
        .baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubWebApi.class);

    GitHubWebService service = new GitHubWebService(webApi);
    List<GitHubRepository> repositories = service.requestGitHubRepository("YukiMatsumura");
    for (GitHubRepository repository : repositories) {
      System.out.println(repository);
    }
  }

  @Test
  public void 検証サーバを使ってGitHubレポジトリを取得() throws Exception {
    final MockWebServer mockWebServer = new MockWebServer();
    MockResponse mockResponse = new MockResponse()
        .setResponseCode(200)
        .setHeader("Content-Type", "application/json")
        .setBody("[{\"id\":123456789, \"name\":\"Hello-World\"}]");
    mockWebServer.enqueue(mockResponse);
    mockWebServer.start();

    GitHubWebApi webApi = new Retrofit.Builder()
        .baseUrl(mockWebServer.url("/").toString())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GitHubWebApi.class);

    GitHubWebService service = new GitHubWebService(webApi);
    List<GitHubRepository> repositories = service.requestGitHubRepository("YukiMatsumura");
    for (GitHubRepository repository : repositories) {
      System.out.println(repository);
    }
    mockWebServer.shutdown();
  }
}