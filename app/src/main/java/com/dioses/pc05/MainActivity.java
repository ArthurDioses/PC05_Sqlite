package com.dioses.pc05;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonOptionRegister = findViewById(R.id.btn_option_register_new);
        Button buttonOptionSearch = findViewById(R.id.btn_option_search);

        buttonOptionRegister.setOnClickListener(view -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
        buttonOptionSearch.setOnClickListener(view -> {
            Intent intent = new Intent(this, SearchEditActivity.class);
            startActivity(intent);
        });
    }

}