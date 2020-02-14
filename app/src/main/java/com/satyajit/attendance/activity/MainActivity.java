package com.satyajit.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;

import com.satyajit.attendance.fragments.AboutFragment;
import com.satyajit.attendance.fragments.HomeFragment;
import com.satyajit.attendance.fragments.ScanFragment;

import es.dmoral.toasty.Toasty;
import me.ibrahimsn.lib.NiceBottomBar;

public class MainActivity extends AppCompatActivity {


    Toolbar toolbar ;
    boolean doubleBackToExitPressedOnce = false;
    NiceBottomBar navigation;
    Fragment fragment = null;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        navigation.setActiveItem(1);



        setSupportActionBar(toolbar);

        //Set the main fragment
         fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, HomeFragment.newInstance()); //Set fragment as default
        fragmentTransaction.commit();

        setListeners();



    }



    void initUI() {

        navigation = findViewById(R.id.bottomBar);
        toolbar = findViewById(R.id.toolbar);


        Typeface Cav = Typeface.createFromAsset(getAssets(), "font/cav.ttf");

        Toasty.Config.getInstance().setToastTypeface(Cav).apply();


    }


    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toasty.info(this,"Hit back again to exit.",Toasty.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

void setListeners(){

        
    navigation.setBottomBarCallback(new NiceBottomBar.BottomBarCallback() {
        @Override
        public void onItemSelect(int i) {

            switch (i) {

                case 0:
                    fragment = ScanFragment.newInstance();

                    
                    break;
                case 1:
                    fragment = HomeFragment.newInstance();

                    break;
                case 2:
                    fragment = AboutFragment.newInstance();

                    break;

            }


            if (fragment != null) {

                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame_layout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        }

        @Override
        public void onItemReselect(int i) {

        }

        @Override
        public void onItemLongClick(int i) {

        }

    });



    }



}


