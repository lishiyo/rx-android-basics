package it.tiwiz.rxjavacrunch.part4;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.models.github.GitHubUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by connieli on 1/31/16.
 */

public class GitHubUsersAdapter extends RecyclerView.Adapter<GitHubUsersAdapter.DataHolder> {

	private List<GitHubUser> users = new ArrayList<>();
	private final GithubUserClickListener listener;

	public GitHubUsersAdapter(final GithubUserClickListener listener) {
		this.listener = listener;
	}

	/**
	 * Add to list of users.
	 *
	 * @param user
	 */
	public void addUser(final GitHubUser user) {
		users.add(user);
		notifyItemInserted(users.size() - 1);
	}

	@Override
	public DataHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
		final View itemView = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.part4_element, viewGroup, false);

		return new DataHolder(itemView, listener);
	}

	@Override
	public void onBindViewHolder(final DataHolder viewHolder, int i) {
		viewHolder.bindTo(users.get(i));
	}

	@Override
	public int getItemCount() {
		return users.size();
	}

	public static class DataHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		private final ImageView userPicture;
		private final TextView userName;
		private final TextView userLogin;
		private final TextView userPage;
		private final GithubUserClickListener listener;

		public DataHolder(final View itemView, final GithubUserClickListener listener) {
			super(itemView);
			userPicture = (ImageView) itemView.findViewById(R.id.userPicture);
			userName = (TextView) itemView.findViewById(R.id.userName);
			userLogin = (TextView) itemView.findViewById(R.id.userLogin);
			userPage = (TextView) itemView.findViewById(R.id.userPage);
			this.listener = listener;

			// attach click listener to the item
			itemView.setOnClickListener(this);
		}

		/**
		 * Bind texts and avatar image.
		 *
		 * @param user
		 */
		public void bindTo(GitHubUser user) {
			userName.setText(user.getName());
			userLogin.setText(user.getLogin());
			userPage.setText(user.getReposUrl());

			Glide.with(userPicture.getContext())
					.load(user.getAvatarUrl())
					.into(userPicture);
		}

		@Override
		public void onClick(View v) {
			final String requestedUser = userLogin.getText().toString();

			listener.onItemClicked(requestedUser);
		}
	}
}