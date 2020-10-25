package com.fawzy.mapsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fawzy.mapsapplication.DB.DB;
import com.fawzy.mapsapplication.DB.places;

public class AddActivity extends AppCompatActivity {

    private EditText latitude , longitude , tittle ;
    private Button save ;
    private DB db ;
    private Bundle extras ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        tittle = findViewById(R.id.tittle);
        save = findViewById(R.id.saveButton);

          db = new DB(this);
          extras = getIntent().getExtras();

          if (extras != null){
              latitude.setText(String.valueOf(extras.getDouble("latitiude")));
              longitude.setText(String.valueOf(extras.getDouble("longitude")));
          }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               db.addplace(new places(latitude.getText().toString(),longitude.getText().toString(),tittle.getText().toString()));
                startActivity(new Intent(AddActivity.this,MapsActivity.class));
                finish();
            }
        });


    }








}