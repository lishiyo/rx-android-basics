package it.tiwiz.rxjavacrunch.part3;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.Utils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Part3Activity extends AppCompatActivity {

	@Bind(R.id.text_view_one)
	TextView txtPart1;

	@Bind(R.id.rootView)
	View rootView;

	@Bind(R.id.btn)
	Button mButton;

	final String[] manyWords = {"Hello", "to", "everyone", "from", "RxAndroid",
			"something", "that", "is", "really", "nice"};

	final List<String> manyWordList = Arrays.asList(manyWords); // turn array to list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part1);
		ButterKnife.bind(this);

		mButton.setVisibility(View.VISIBLE);

		/**
		 * Creates an {@link Observable} that will be emitted only once
		 */
		Observable.just("Hello, World!")
				.observeOn(AndroidSchedulers.mainThread())
				.map(String::toUpperCase)
				.subscribe(txtPart1::setText);

		/**
		 * Emits one item at the time, taking them from any {@link java.util.Collection}
		 */
		Observable.from(manyWords)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(message ->
						Toast.makeText(Part3Activity.this, message, Toast.LENGTH_SHORT).show());

		/**
		 * ------[x, x, x]----- // Observable.just
		 * ------[x...][x...][x...]------ // Observable.from(list)
		 * -------x--x--x------- // flatMap
		 * -------------xxx----- // reduce
		 */
		Observable.just(manyWordList)
				.observeOn(AndroidSchedulers.mainThread())
				.flatMap(Observable::from)
				.reduce((accumStr, currWord) -> accumStr + " :" + currWord)
				.subscribe(message -> Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show());


		createAsyncOperation();
	}

	/**
	 * Async long-running operation: http://www.captechconsulting.com/blogs/getting-started-with-rxjava-and-android
	 * When subscribing to a Single, there is only an onSuccess Action and an onError action.
	 */
	private void createAsyncOperation() {
		final Observable<String> operationObs = Observable.create(new Observable.OnSubscribe<String>() {
			@Override
			public void call(final Subscriber<? super String> subscriber) {
				final String value = Utils.mockLongRunningOperation();
				subscriber.onNext(value);

				subscriber.onCompleted();
			}
		})
		.subscribeOn(Schedulers.io()) // do the work async
		.map(s -> s + " button one!")
		.repeatWhen(completedObservable -> completedObservable.delay(1, TimeUnit.SECONDS))
		.observeOn(AndroidSchedulers.mainThread());

		mButton.setOnClickListener(v -> {
			mButton.setEnabled(false);
			operationObs.subscribe(
					mButton::setText,
					error -> Log.e("connie", error.getLocalizedMessage()),
					() -> {
						mButton.setEnabled(true);
						mButton.setText("onComplete!");
					});
		});
	}

}