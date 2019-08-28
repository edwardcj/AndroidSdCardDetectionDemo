package com.edwardchen.sdcarddetectiondemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;

public class MainActivity extends AppCompatActivity {

    private Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sw = (Switch) findViewById(R.id.switch_sdcard);

        final Handler handler = new Handler();
        new Thread(new CheckSdCardPresence(this, sw, handler)).start();

    }

    private static class CheckSdCardPresence implements Runnable {

        Context context;
        Switch sw;
        Handler handler;
        public CheckSdCardPresence(Context context, Switch sw, Handler handler) {
            this.context = context;
            this.sw = sw;
            this.handler = handler;
        }

        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            boolean SdCardPresent = hasRealRemovableSdCard();
                            if (SdCardPresent) {
                                sw.setChecked(true);
                            } else
                                sw.setChecked(false);

                            Log.d("CheckSdCardPresence", !SdCardPresent ? "not inserted" : "inserted");
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        boolean hasRealRemovableSdCard() {
            return ContextCompat.getExternalFilesDirs(context, null).length >= 2;
        }
    }
}
