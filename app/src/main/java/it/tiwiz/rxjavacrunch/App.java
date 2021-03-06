package it.tiwiz.rxjavacrunch;

import android.support.annotation.Nullable;
import it.tiwiz.rxjavacrunch.networking.GithubService;
import it.tiwiz.rxjavacrunch.networking.OpenWeatherService;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton app instance.
 *
 * Created by connieli on 1/31/16.
 */
public class App {
	public static final String TAG = "connie";
	@Nullable
	private static App mInstance = null;
	@Nullable
	private static GithubService mGithubService;
	@Nullable
	private static OpenWeatherService mWeatherService;

	private App() {

	}

	public static App getInstance() {
		if (mInstance == null) {
			// for thread safety, lock creation of App.class
			final Class clazz = App.class;
			synchronized (clazz) {
				mInstance = new App();
			} // lock released here
		}

		return mInstance;
	}

	/**
	 * @return singleton instance of app's {@link GithubService}
	 */
	public static GithubService getGithubService() {
		if (mGithubService == null) {
			final Class clazz = GithubService.class;
			synchronized (clazz) {
				final Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(GithubService.BASE_URL)
						.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
						.addConverterFactory(GsonConverterFactory.create())
						.build();

				mGithubService = retrofit.create(GithubService.class);
			}
		}

		return mGithubService;
	}

	/**
	 * @return singleton instance of open weather service
	 */
	public static OpenWeatherService getWeatherService() {
		if (mWeatherService == null) {
			// Create the weather service
			final Retrofit retrofit = new Retrofit.Builder()
					.addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // turn to observable!
					.addConverterFactory(GsonConverterFactory.create())
					.baseUrl(OpenWeatherService.BASE_URL)
					.build();

			mWeatherService = retrofit.create(OpenWeatherService.class);
		}

		return mWeatherService;
	}
}
