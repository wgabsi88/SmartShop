package com.beuth.ebp.smartshop;

/**
 * Created by waelgabsi on 01.02.16.
 */
import java.util.List;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface GithubService {

    public static final String ENDPOINT = "https://api.github.com";

    @GET("/users/{user}/repos")
    List<Repo> listRepos(@Path("user") String user);

    @GET("/search/repositories")
    List<Repo> searchRepos(@Query("q") String query);
}