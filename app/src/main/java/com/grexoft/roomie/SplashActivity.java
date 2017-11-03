package com.grexoft.roomie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grexoft.roomie.utils.SharedPrefsUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread background = new Thread() {
            public void run() {

                try {
                    sleep(3000);
                    if (SharedPrefsUtil.hasPreference(SplashActivity.this, SharedPrefsUtil.SOCIAL_LOGIN_ID) ) {
                        Intent in = new Intent(getBaseContext(), HomeActivity.class);
//                        in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(in);
                 /*   }
                    else if (SharedPrefsUtil.hasPreference(SplashActivity.this, SharedPrefsUtil.SOCIAL_LOGIN_ID)) {
                        Intent in = new Intent(getBaseContext(), SignUpActivity.class);
                        startActivity(in);*/
                    } else {
                        Intent in = new Intent(getBaseContext(), LoginActivity.class);
                        startActivity(in);
                    }
//                    sleep(2000);
//                    System.out.println("Finish");
//                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        background.start();
    }
}
