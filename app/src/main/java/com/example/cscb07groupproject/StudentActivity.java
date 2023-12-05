package com.example.cscb07groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StudentActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        LinearLayout event = findViewById(R.id.events_btn);
        LinearLayout anouncement = findViewById(R.id.anouncement_btn);
        LinearLayout post = findViewById(R.id.post_btn);
        LinearLayout reviews = findViewById(R.id.review_btn);
        LinearLayout complaints = findViewById(R.id.complaints_btn);
        Button logOut = findViewById(R.id.log_out_btn);

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this, ViewEventStudent.class);
                startActivity(intent);
            }
        });

//        anouncement.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(StudentActivity.this, StudentActivity.class);
//                startActivity(intent);
//            }
//        });
         post.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(StudentActivity.this, PostQualifications.class);
                 startActivity(intent);
             }
         });

//         reviews.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 Intent intent = new Intent(StudentActivity.this, ViewEventStudent.class);
//                 startActivity(intent);
//             }
//         });
//
//         complaints.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view) {
//                 Intent intent = new Intent(StudentActivity.this, ViewEventStudent.class);
//                 startActivity(intent);
//             }
//         });
            logOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(StudentActivity.this, LoginView.class);
                    startActivity(intent);
                    finish();
                }
            });
    }


}