package it.tiwiz.rxjavacrunch.part1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import it.tiwiz.rxjavacrunch.R;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;

import java.util.ArrayList;
import java.util.List;

public class Part1Activity extends ActionBarActivity {

    @Bind(R.id.text_view_one)
    TextView mTextView;

    private Observable mObservable;

    /**
     * We can create an Observable from this function, which accepts the
     * `Observer<output type>` as its parameter and calls the Observer's
     * onNext, onError, onCompleted functions to emit outputs once the Observer subscribes.
     */
    private Observable.OnSubscribe mObservableFunction;

    /**
     * When this {@link rx.Subscriber} is invoked by the {@link rx.Observable}
     * it will change the value inside the {@link android.widget.TextView} in the UI,
     * using as value the parameter sent by the {@link rx.Observable}
     */
    private Subscriber<? super String> mTextViewSubscriber;

    /**
     * When this {@link rx.Subscriber} is invoked by the {@link rx.Observable}
     * it will create a {@link android.widget.Toast},
     * using as message the parameter sent by the {@link rx.Observable}
     */
    private Subscriber<? super String> mToastSubscriber;

    private List<String> mWordList = new ArrayList<String>() {{
        add("One");
        add("Two");
        add("Three");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);

        // set views
        ButterKnife.bind(this);

        // create the Observable from a function - Observable.create(observableAction)
//        initObservableFunction();
//        mObservable = Observable.create(mObservableFunction);
        final Observable<String> singleObservable = Observable.just("what up world?");

        // invoke the subscribers/observers
//        initSubscribers();
//        mObservable.subscribe(mTextViewSubscriber);
//        mObservable.subscribe(mToastSubscriber);

        // actions do not return anything
        Action1<Integer> textViewOnNextAction = s -> mTextView.setText("got str length: " + s);
        Action1<String> toastOnNextAction = s -> Toast.makeText(this, "upper cased: " + s, Toast.LENGTH_SHORT).show();
        // functions take input and return output
        Func1<String, String> toUpperCase = String::toUpperCase;
        Func1<String, Integer> toStringLength = String::length;

        // errors
        Action1<Throwable> onErrorCallback = err -> Toast.makeText(this, err.getLocalizedMessage(),
                Toast.LENGTH_SHORT).show();

        // Do the subscriptions
        // let observers take emissions on a particular thread (here, main)
        // subscribeOn would make the Observable *itself* operate on the particular thread as well
        singleObservable
                .observeOn(AndroidSchedulers.mainThread())
                .map(toStringLength)
                .subscribe(textViewOnNextAction, onErrorCallback);

        // from() emits each element in the iterable one at a time
        final Observable<String> oneByOneObservable = Observable.from(mWordList);
        oneByOneObservable
                .map(toUpperCase)
                .subscribe(toastOnNextAction, onErrorCallback);

        toastWordListSum();
    }

    /**
     * Sum all the strings
     */
    private void toastWordListSum() {
        // emit a full list in a single shot and then process each element into another observable
        // then merge all those into one string

        // [x1, x2, x3]
        // x1 -- x2 --x3
        // map to numbers
        // reduce to sum
        final Func1<List<String>, Observable<String>> getStringNums = Observable::from;
        final Func1<String, Integer> toNumber = Integer::parseInt;
        final Func2<Integer, Integer, Integer> sumList = (accum, currEl) -> accum + currEl;

        final Observable<Integer> sumObservable = Observable
                .just(mWordList)
                .observeOn(AndroidSchedulers.mainThread()) // put this right after the emission
                .flatMap(getStringNums)
                .map(toNumber)
                .reduce(sumList);

        sumObservable.subscribe(sum -> Toast.makeText(this, "total sum of word list: " + sum, Toast.LENGTH_SHORT).show());
    }

    private void initObservableFunction() {
        mObservableFunction = new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = 0; i < 4; i++) {
                            final String output = "Hello world! " + i;
                            subscriber.onNext(output);
                        }

                        subscriber.onCompleted();
                    } else {
                        Log.i("connie", "subscribe is NOT subscribed!");
                    }
                } catch (final Exception e) {
                    subscriber.onError(e);
                }
            }
        };
    }

    private void initSubscribers() {
        mTextViewSubscriber = new Subscriber<String>() {
            @Override
            public void onNext(final String s) {
                if (mTextView != null) {
                    mTextView.setText(s);
                }
            }

            @Override
            public void onCompleted() {
                Log.i("connie", "text ++ onCompleted!");
            }

            @Override
            public void onError(Throwable e) {

            }
        };

        mToastSubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(Part1Activity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCompleted() {
                Log.i("connie", "toast ++ onCompleted!");
            }

            @Override
            public void onError(Throwable e) {

            }
        };
    }

}
