package com.example.cscb07groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {
    Button button;
    Button complaint;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        button = findViewById(R.id.log_out);
        textView = findViewById(R.id.user_details);
        complaint = findViewById(R.id.view_complaints);


        //Button to sign out and go back to Login page
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        });

        //Button to go to the Complaint page
        complaint.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ComplaintActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}