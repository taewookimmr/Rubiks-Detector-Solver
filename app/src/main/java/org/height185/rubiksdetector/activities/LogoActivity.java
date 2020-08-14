package org.height185.rubiksdetector.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import org.height185.rubiksdetector.R;


// AppCompatActivity 대신 Activity를 extends
public class LogoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Handler handler = new Handler();
        handler.postDelayed(new delayHandler(), 2000); // 2초뒤에 다음 엑티비티로
    }

    class delayHandler implements Runnable {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
