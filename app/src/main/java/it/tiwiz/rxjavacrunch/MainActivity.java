package it.tiwiz.rxjavacrunch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import it.tiwiz.rxjavacrunch.part1.Part1Activity;
import it.tiwiz.rxjavacrunch.part3.Part3Activity;
import it.tiwiz.rxjavacrunch.part4.Part4Activity;
import it.tiwiz.rxjavacrunch.part5.Part5Activity;
import it.tiwiz.rxjavacrunch.part6.Part6Activity;
import it.tiwiz.rxjavacrunch.part7.Part7Activity;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wireUi();
    }

    private void wireUi() {
        setTapListener(R.id.btnPart1);
        setTapListener(R.id.btnPart3);
        setTapListener(R.id.btnPart4);
        setTapListener(R.id.btnPart5);
        setTapListener(R.id.btnPart6);
    }

    private void setTapListener(int viewId) {
        findViewById(viewId).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(getLaunchIntentFor(v.getId()));
    }

    private Intent getLaunchIntentFor(int viewId) {
        Intent launchIntent = null;

        switch (viewId) {
        case R.id.btnPart1:
            launchIntent = new Intent(this, Part1Activity.class);
            break;
        case R.id.btnPart3:
            launchIntent = new Intent(this, Part3Activity.class);
            break;
        case R.id.btnPart4:
            launchIntent = new Intent(this, Part4Activity.class);
            break;
        case R.id.btnPart5:
            launchIntent = new Intent(this, Part5Activity.class);
            break;
        case R.id.btnPart6:
            launchIntent = new Intent(this, Part6Activity.class);
            break;
        case R.id.btnPart7:
            launchIntent = new Intent(this, Part7Activity.class);
            break;
        }

        return launchIntent;
    }
}
