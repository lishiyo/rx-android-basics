package it.tiwiz.rxjavacrunch.part7;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import it.tiwiz.rxjavacrunch.App;
import it.tiwiz.rxjavacrunch.R;
import it.tiwiz.rxjavacrunch.models.weather.OpenWeatherResponse;
import it.tiwiz.rxjavacrunch.networking.OpenWeatherService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.SafeSubscriber;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * https://medium.com/@ahmedrizwan/rxandroid-and-retrofit-2-0-66dc52725fff#.345da2ave
 *
 *
 * @author connieli
 */
public class WeatherActivity extends RxAppCompatActivity implements OnToggleListener {

	@Bind(R.id.edit_input)
	EditText mSearchInput;

	@Bind(R.id.name)
	TextView mName;

	@Bind(R.id.temperature)
	TextView mTemp;

	@Bind(R.id.description)
	TextView mDescription;

	/**
	 * The raw subscription.
	 */
	private Subscriber<OpenWeatherResponse> mCitySubscriber;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		ButterKnife.bind(this);

		// https://medium.com/@diolor/improving-ux-with-rxjava-4440a13b157f#.e0bs5965o
		// TODO: add retry logic
		RxTextView.textChanges(mSearchInput)
				.compose(bindToLifecycle())
				.skip(1) // avoid first
				.debounce(400, TimeUnit.MILLISECONDS) // default debounce Scheduler is Computation
				// use switchMap rather than flatMap to cancel previous request
				.switchMap(s -> App.getWeatherService().getCityWeather(OpenWeatherService.APPID, s.toString()))
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new SafeSubscriber<>(getResultsSubscriber()));

		setupTogglesList();
	}

	/**
	 * Lazy create the networking subscriber to update the response.
	 *
	 * @return the subscription to OpenWeatherResponse
	 */
	private Subscriber<OpenWeatherResponse> getResultsSubscriber() {
		if (mCitySubscriber != null) {
			return mCitySubscriber;
		}

		mCitySubscriber = new Subscriber<OpenWeatherResponse>() {
			@Override
			public void onCompleted() {
				Toast.makeText(WeatherActivity.this,
						"completed!",
						Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onError(final Throwable e) {
				mDescription.setText(e.getLocalizedMessage());
			}

			@Override
			public void onNext(final OpenWeatherResponse response) {
				if (response != null) {
					bindResultViews(response);
				}
			}
		};

		return mCitySubscriber;
	}

	/**
	 * Bind data for one result to views.
	 *
	 * @param response
	 *      the response object
	 */
	private void bindResultViews(@NonNull final OpenWeatherResponse response) {
		final String name = response.getName() != null ? response.getName() : "";
		final Double temp = response.getMain() != null ? response.getMain().getTempFahrenheit() : 0;
		final String description = !response.getWeather().isEmpty()
				? response.getWeather().get(0).getDescription()
				: "";

		mName.setText("Name: " + name);
		mTemp.setText("Current temp: " + String.valueOf(temp));
		mDescription.setText("Current description: " + description);
	}

	/**
	 * =====================================================================
	 * Rx Toggles
	 * =====================================================================
	 */
	public final List<String> mDummyItems = Arrays.asList("one", "two", "three", "four", "five", "six", "seven",
			"eight", "nine", "ten");

	private RxTogglesAdapter mTogglesAdapter;

	@Bind(R.id.toggles_list)
	RecyclerView mTogglesList;

	@Bind(R.id.toggles_results)
	TextView mTogglesResults;

	/**
	 * Set up rx recycler view for toggles.
	 */
	private void setupTogglesList() {
		// init recycler view
		final LinearLayoutManager llm = new LinearLayoutManager(this);
		llm.setOrientation(LinearLayoutManager.VERTICAL);
		mTogglesList.setLayoutManager(llm);

		// init and set adapter
		mTogglesAdapter = new RxTogglesAdapter(this);
		mTogglesList.setAdapter(mTogglesAdapter);

		// set mock items
		mTogglesAdapter.setItems(mDummyItems);
	}

	@Override
	public void onToggle(final String toggleItem, boolean isChecked) {
		// TODO: rx-ify?
		final String res = "toggled! " + toggleItem + " is checked?" + isChecked;
		Toast.makeText(this, res, Toast.LENGTH_LONG).show();

	}
}
