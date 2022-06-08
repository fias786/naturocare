package com.natrocare.naturocare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

    ImageButton welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPrefs.saveSharedSetting(this,"sharedPrefs","false");
        welcome = findViewById(R.id.WelcomeButton);
        welcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Welcome.this, MainActivity.class));
                finish();
            }
        });
    }
}