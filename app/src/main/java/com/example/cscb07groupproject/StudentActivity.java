package com.example.cscb07groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class StudentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        LinearLayout rsvp = findViewById(R.id.rsvp_btn);
        LinearLayout announcement = findViewById(R.id.anouncement_btn);
        LinearLayout post = findViewById(R.id.post_btn);
        LinearLayout reviews = findViewById(R.id.review_btn);
        LinearLayout complaints = findViewById(R.id.complaints_btn);
        Button logOut = findViewById(R.id.log_out_btn);
        TextVisible();
        getSupportActionBar().hide();
        rsvp.setOnClickListener(view -> {
            Intent intent = new Intent(StudentActivity.this, ViewEventStudent.class);
            startActivity(intent);
        });

        announcement.setOnClickListener(view -> {
            Intent intent = new Intent(StudentActivity.this, StudentAnnouncementView.class);
            startActivity(intent);
        });
         post.setOnClickListener(view -> {
             Intent intent = new Intent(StudentActivity.this, PostQualifications.class);
             startActivity(intent);
         });

         reviews.setOnClickListener(view -> {
             Intent intent = new Intent(StudentActivity.this, EventsStudentsActivity.class);
             startActivity(intent);
         });

         complaints.setOnClickListener(view -> {
             Intent intent = new Intent(StudentActivity.this, StudentComplaintActivity.class);
             startActivity(intent);
         });

        logOut.setOnClickListener(view -> {
            Intent intent = new Intent(StudentActivity.this, LoginView.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        TextVisible();
        super.onResume();
    }
    private void TextVisible(){
        TextView announcementNotification = findViewById(R.id.txt_announcement);
        TextView eventNotification = findViewById(R.id.txt_events);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference AnnouncementDatabase = firebaseDatabase.getReference("Announcements");
        DatabaseReference EventDB = firebaseDatabase.getReference("events");
        announcementNotification.setVisibility(View.INVISIBLE);
        eventNotification.setVisibility(View.INVISIBLE);
        SharedPreferences sharepref = getSharedPreferences("Pref", Context.MODE_PRIVATE);

        Query announcements = AnnouncementDatabase.orderByChild("Title");
        //Counts the number of announcements in the database and compared to
        //number of announcements user checked last time he opened AnnouncementView
        announcements.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int account = (int) snapshot.getChildrenCount();
                int announcementCount = sharepref.getInt("Announcements count", 0);
                if(account > announcementCount) {
                    announcementNotification.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query events = EventDB.orderByChild("Event number");

        events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int b = (int) snapshot.getChildrenCount();
                int eventcount = sharepref.getInt("Events count", 0);

                if(b > eventcount){
                    eventNotification.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}