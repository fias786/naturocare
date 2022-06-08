package com.natrocare.naturocare;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean check = Boolean.valueOf(SharedPrefs.readSharedSetting(this,"sharedPrefs","true"));
        if(check){
            startActivity(new Intent(this, Login.class));
            finish();
        }
        else {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }
}