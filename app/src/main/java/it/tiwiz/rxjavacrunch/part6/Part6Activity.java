package it.tiwiz.rxjavacrunch.part6;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.support.design.widget.RxSnackbar;
import com.jakewharton.rxbinding.support.v7.widget.RxToolbar;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import it.tiwiz.rxjavacrunch.App;
import it.tiwiz.rxjavacrunch.R;

/**
 * Created by connieli on 1/31/16.
 */
public class Part6Activity extends RxAppCompatActivity {
	@Bind(R.id.response)
	TextView mResponseTextView;

	@Bind(R.id.toolbar)
	Toolbar mToolbar;

	@Bind(R.id.fab)
	FloatingActionButton mFab;

	@Bind(R.id.editTextUsualApproach)
	EditText mUsualEditText;

	@Bind(R.id.editTextReactiveApproach)
	EditText mReactiveEditText;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part6);
		ButterKnife.bind(this);

		manageToolbar();
		manageFloatingActionButton();
		manageEditTexts();
	}

	/**
	 * Create toasts on toolbar events - item and up nav clicks.
	 **/
	private void manageToolbar() {
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true); // go up one level instead of front page

		RxToolbar.itemClicks(mToolbar).subscribe(this::onToolbarItemClicked);
		RxToolbar.navigationClicks(mToolbar).subscribe(aVoid -> onToolbarUpNavClicked());
	}

	/**
	 * Clicking the FAB opens and closes the snackbar.
	 */
	private void manageFloatingActionButton() {
		RxView.clicks(mFab)
				.compose(bindToLifecycle())
				.subscribe(aVoid -> onFabClicked());
	}

	/**
	 * Edit text subscription.
	 */
	private void manageEditTexts() {
		RxTextView.textChanges(mReactiveEditText).subscribe(this::onNewTextChanged);
	}

	/**
	 * ========== EVENT HANDLERS =========
	 */

	private void onToolbarItemClicked(final MenuItem menuItem) {
		final String res = "toolbar item clicked!";
		Log.i(App.TAG, res);
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	private void onToolbarUpNavClicked() {
		final String res = "onToolbarUpNavClicked!";
		Log.i(App.TAG, res);
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	private void onFabClicked() {
		Log.i(App.TAG, "onFabClicked!");

		// show then dismiss the snackbar
		final View contentView = findViewById(android.R.id.content);
		final Snackbar snackbar = Snackbar.make(contentView, "snackbar clicked!", Snackbar.LENGTH_SHORT);
		snackbar.show();

		RxSnackbar.dismisses(snackbar).subscribe(this::onSnackbarDismissed);
	}

	private void onSnackbarDismissed(final Integer code) {
		final String res = "onSnackbarDismissed! passed: " + code;
		Log.i(App.TAG, res);
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Add new text to results view.
	 *
	 * @param text
	 */
	private void onNewTextChanged(final CharSequence text) {
		final String res = "onNewTextChanged! passed: " + text;
		Log.i(App.TAG, res);

		mResponseTextView.setText(text);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.part6, menu);
		return true;
	}
}
