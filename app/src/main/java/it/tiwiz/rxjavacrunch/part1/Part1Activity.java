package it.tiwiz.rxjavacrunch.part1;

import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part1);

        // set views
        ButterKnife.bind(this);

        // create the Observable from a function - Observable.create(observableAction)
        initObservable();
        mObservable = Observable.create(mObservableFunction);

        // invoke the subscribers/observers
        initSubscribers();

        // do the subscription
    }

    private void initObservable() {
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

        final Context context = this;
        mToastSubscriber = new Subscriber<String>() {
            @Override
            public void onNext(String s) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
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
