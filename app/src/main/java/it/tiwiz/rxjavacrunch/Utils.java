package it.tiwiz.rxjavacrunch;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Rx Utils.
 *
 * Created by connieli on 1/31/16.
 */
public class Utils {

	/**
	 * Do work on worker, output on main.
	 */
	final static Observable.Transformer<Observable, Observable> schedulersTransformer =
			observable -> observable.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread());

	/**
	 * Do the work on a worker thread, output back to main thread.
	 *
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Observable.Transformer<T, T> applySchedulers() {
		return (Observable.Transformer<T, T>) schedulersTransformer;
	}

	/**
	 * @return a fake long operation
	 */
	public static String mockLongRunningOperation() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// error
		}

		return "Complete!";
	}

}
