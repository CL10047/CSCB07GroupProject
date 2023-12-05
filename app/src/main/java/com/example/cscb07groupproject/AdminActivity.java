package com.example.cscb07groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdminActivity extends AppCompatActivity {
    Button logOut;
    LinearLayout complaint;
    LinearLayout feedback;
    LinearLayout createEvents;
    LinearLayout anouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        logOut = findViewById(R.id.log_out_btn);
        complaint = findViewById(R.id.complaints_btn);
        feedback = findViewById(R.id.feedback_btn);
        createEvents = findViewById(R.id.events_btn);
        anouncement = findViewById(R.id.anouncement_btn);

//        createEvents.setOnClickListener(new View.OnClickListener() {
//
//        @Override
//             public void onClick(View view) {
//                 Intent intent = new Intent(AdminActivity.this, ViewEventStudent.class);
//                 startActivity(intent);
//             }
//         });

//        feedback.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//             public void onClick(View view) {
//                 Intent intent = new Intent(AdminActivity.this, ViewEventStudent.class);
//                 startActivity(intent);
//             }
//         });

        //        anouncement.setOnClickListener(new View.OnClickListener() {
//            @Override
//             public void onClick(View view) {
//                 Intent intent = new Intent(AdminActivity.this, ViewEventStudent.class);
//                 startActivity(intent);
//             }
//         });
        //Button to sign out and go back to Login page
        logOut.setOnClickListener(new View.OnClickListener() {

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