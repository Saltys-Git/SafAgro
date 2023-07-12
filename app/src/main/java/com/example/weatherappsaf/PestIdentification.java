package com.example.weatherappsaf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

public class PestIdentification extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pest_identification);
        ImageView imageView = findViewById(R.id.pi_camera);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PestIdentityCamera.class);
                startActivity(intent);
            }
        });
    }

    public void openPI(View view) {
        Intent intent = new Intent(getApplicationContext(), PIDetail.class);
        startActivity(intent);
    }
}