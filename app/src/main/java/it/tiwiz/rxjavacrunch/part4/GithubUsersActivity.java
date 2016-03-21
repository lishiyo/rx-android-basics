package it.tiwiz.rxjavacrunch.part4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.tiwiz.rxjavacrunch.App;
import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.Utils;
import it.tiwiz.rxjavacrunch.models.github.GitHubUser;
import it.tiwiz.rxjavacrunch.networking.GithubService;
import rx.Observable;
import rx.Subscriber;

import java.util.Arrays;
import java.util.List;

/**
 * Created by connieli on 1/31/16.
 */
public class GithubUsersActivity extends AppCompatActivity implements GithubUserClickListener {
	public final List<String> mUserNames = Arrays.asList("lishiyo", "tiwiz", "staltz", "dlew");
	private final GithubService mGithubService = App.getGithubService();
	private GitHubUsersAdapter mAdapter;

	@Bind(R.id.usersList)
	RecyclerView mUsersList;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part4);
		ButterKnife.bind(this);

		// init recycler view
		LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mUsersList.setLayoutManager(llm);

		// init and set adapter
		mAdapter = new GitHubUsersAdapter(this);
		mUsersList.setAdapter(mAdapter);

		getAllUsers();
	}

	/**
	 * Populate adapter with users.
	 */
	private void getAllUsers() {
		Observable.from(mUserNames)
				.flatMap(mGithubService::getUserData) // username => GitHubUser
				.compose(Utils.applySchedulers())
				.subscribe(new Subscriber<GitHubUser>() {
					@Override
					public void onCompleted() {
						Log.i(App.TAG, "getAllUsers completed!");
					}

					@Override
					public void onError(final Throwable e) {
						Log.i(App.TAG, "getAllUsers onError! " + e.getMessage());
					}

					@Override
					public void onNext(GitHubUser gitHubUser) {
						Log.i(App.TAG, "getAllUsers onNext! " + gitHubUser.getName());
						mAdapter.addUser(gitHubUser);
					}
				});
	}

	@Override
	public void onItemClicked(String username) {
		Log.i(App.TAG, "item clicked! username: " + username);
	}
}
