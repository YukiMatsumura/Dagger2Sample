package yuki.m.android.dagger2sample.webapi;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface GitHubWebApi {
    @GET("/users/{user}/repos")
    Call<List<GitHubApiResponse.Repository>> getRepositories(@Path("user") String userName);
}
