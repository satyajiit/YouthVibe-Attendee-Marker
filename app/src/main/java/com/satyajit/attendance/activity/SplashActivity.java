package com.satyajit.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class SplashActivity extends AppCompatActivity {

    FirebaseUser user;
    private Typeface Cav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

         user = mAuth.getCurrentUser();

        Cav = Typeface.createFromAsset(getAssets(),"font/cav.ttf");

        Toasty.Config.getInstance().setToastTypeface(Cav).apply();



        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Main-Activity. */

                //  if (isLoggedUser())
                // Intent mainIntent;
                if (user != null){

                    Toasty.success(SplashActivity.this, "Welcome back" + " " + user.getDisplayName(), Toast.LENGTH_LONG, true).show();
                    finish();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                }
                else
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));


                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        }, 900);


    }
}
