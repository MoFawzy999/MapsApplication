 package com.fawzy.mapsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fawzy.mapsapplication.DB.DB;
import com.fawzy.mapsapplication.DB.places;

 public class ShowActivity extends AppCompatActivity {

     private TextView latitude , longitude , tittle ;
     private Button delete , edit ;
     private DB db ;
     private Bundle extras ;
     int num ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        tittle =  findViewById(R.id.name);
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);

        db = new DB(this);
        extras = getIntent().getExtras();

        if (extras != null){
            latitude.setText(String.valueOf(extras.getDouble("latitiude")));
            longitude.setText(String.valueOf(extras.getDouble("longitude")));
            tittle.setText(extras.getString("title"));
            Toast.makeText(this, String.valueOf((int) extras.getFloat("id")), Toast.LENGTH_SHORT).show();
            num = Math.round( extras.getFloat("id"));
        }


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num != 0) {
                    db.removeplace(new places(num,latitude.getText().toString(), longitude.getText().toString()
                            , tittle.getText().toString()));
                    startActivity(new Intent(ShowActivity.this, MapsActivity.class));
                    finish();
                }

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowActivity.this , EditActivity.class);
                intent.putExtra("id",num);
                intent.putExtra("title" , tittle.getText().toString());
                intent.putExtra("latitude" , latitude.getText().toString());
                intent.putExtra("longitude" , longitude.getText().toString());
                startActivity(intent);
            }
        });

    }
}