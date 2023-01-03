package com.example.fitnesstraining;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    MaterialButton btnStart, btnStart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStart = findViewById(R.id.btnStart);
        btnStart1 = findViewById(R.id.btnStart1);

        View.OnClickListener onClickListener = view -> startActivity(new Intent(this, TrainingActivity.class));

        btnStart1.setOnClickListener(onClickListener);
        btnStart.setOnClickListener(onClickListener);
    }
}