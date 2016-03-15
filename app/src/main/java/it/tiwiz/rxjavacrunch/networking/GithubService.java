package it.tiwiz.rxjavacrunch.networking;

import it.tiwiz.rxjavacrunch.models.github.GitHubRepo;
import it.tiwiz.rxjavacrunch.models.github.GitHubUser;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

import java.util.List;

/**
 * Retrofit 2 service that returns Rx.Observables
 *
 * Created by connieli on 1/31/16.
 */
public interface GithubService {
	public static final String BASE_URL = "https://api.github.com";

	@GET("users/{user}")
	Observable<GitHubUser> getUserData(@Path("user") String user);

	@GET("users/{user}/repos")
	Observable<List<GitHubRepo>> getUserRepos(@Path("user") String user);
}
