package it.tiwiz.rxjavacrunch.part5;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import it.tiwiz.rxjavacrunch.App;
import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.Utils;
import rx.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Created by connieli on 1/31/16.
 */
public class Part5Activity extends RxAppCompatActivity {
	private static final String TAG = App.TAG;

	@Bind(R.id.mainEmittedNumber)
	TextView textEmittedNumber;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part5);
		ButterKnife.bind(this);

		/**
		 * bindToLifecycle() calls onComplete when activity finishes
		 */
		Observable.interval(1, TimeUnit.SECONDS)
				.compose(Utils.applySchedulers())
				.compose(bindToLifecycle())
				.filter(this::transformInterval)
				.map(s -> "in evens: " + s)
				.subscribe(this::logOnNext, this::logOnError, this::logOnCompleted);
	}

	/**
	 * Takes in the input => outputs whether to pass it on.
	 *
	 * @param time
	 *      the input
	 * @return true to pass on the time
	 */
	private boolean transformInterval(final Long time) {
		return time % 2 == 0;
	}

	private void logOnNext(final String string) {
		textEmittedNumber.setText(string);
		Log.d(TAG, "Nothing bad happened for " + string + " seconds");
	}

	private void logOnError(Throwable throwable) {
		Log.e(TAG, "Something worse than White Walkers is approaching!\t" + throwable.getMessage());
	}

	private void logOnCompleted() {
		Log.d(TAG, "The day has come, may my watch end!");
	}

}
