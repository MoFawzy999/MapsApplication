package com.fawzy.mapsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fawzy.mapsapplication.DB.DB;
import com.fawzy.mapsapplication.DB.places;

public class EditActivity extends AppCompatActivity {

    private EditText editText ;
    private Button edit ;
    private DB db ;
    private Bundle extras ;
    private String latitude ="" ;
    private String longitude ="" ;
    private int id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editText = findViewById(R.id.edt_txt);
        edit = findViewById(R.id.editplace);
        extras = getIntent().getExtras();
        db = new DB(this);

        if (extras != null){
            editText.setText(extras.getString("title"));
            id = extras.getInt("id");
            latitude = extras.getString("latitude");
            longitude = extras.getString("longitude");
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (id != 0 && !latitude.isEmpty() && !longitude.isEmpty()){
                    db.editplace(new places(id,latitude,longitude,editText.getText().toString()));
                    startActivity(new Intent(EditActivity.this, MapsActivity.class));
                    finish();
                }

            }
        });
    }




}