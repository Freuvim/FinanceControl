package io.github.freuvim.financecontrol.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.github.freuvim.financecontrol.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Bundle b = new Bundle();
                    b.putBoolean("intro", true);
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
