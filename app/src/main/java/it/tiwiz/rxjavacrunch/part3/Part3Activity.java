package it.tiwiz.rxjavacrunch.part3;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import it.tiwiz.rxjavacrunch.R;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

import java.util.Arrays;
import java.util.List;

public class Part3Activity extends AppCompatActivity {

	TextView txtPart1;
	View rootView;

	final String[] manyWords = {"Hello", "to", "everyone", "from", "RxAndroid",
			"something", "that", "is", "really", "nice"};

	final List<String> manyWordList = Arrays.asList(manyWords); // turn array to list

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_part1);

		txtPart1 = (TextView) findViewById(R.id.text_view_one);
		rootView = findViewById(R.id.rootView);

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
				.reduce((accumStr, currWord) -> String.format("%s %s", accumStr, currWord))
				.subscribe(message ->
						Snackbar.make(rootView, message, Snackbar.LENGTH_LONG).show());

	}

}