package com.example.appsdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.appsdata.constants.Constants;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getPreferences(0);
        initFragment();


    }

    private void initFragment() {
        Fragment fragment;
        if (pref.getBoolean(Constants.IS_LOGIN_IN,false)){
            fragment = new MainMenuFragment();

        }else {
            fragment = new LoginFragment();
        }
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_frame,fragment).commit();

    }
}
