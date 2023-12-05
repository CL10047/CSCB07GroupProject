package com.example.cscb07groupproject;



import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



import androidx.annotation.NonNull;

import androidx.navigation.Navigation;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.util.UUID;

import java.util.Date;

public class AnnouncementCreation extends AppCompatActivity {

    static int num;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_creation);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference AnnouncementDatabase = firebaseDatabase.getReference("Announcements");
        Button btnPostAnnouncement = (Button) findViewById(R.id.button2);
        EditText InputTitle = (EditText) findViewById(R.id.Titleinput);
        EditText InputAnnouncement = (EditText) findViewById(R.id.Announcementinput);
        Button previous = (Button) findViewById(R.id.button_second);

        //Button for going back to home page

        //Method for reading post button
        btnPostAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Announcement = InputAnnouncement.getText().toString();
                String Title = InputTitle.getText().toString();
                if (!Announcement.equals("") && !Title.equals("")) {
                    AddAnnouncementFirebase(Announcement, Title);
                    InputAnnouncement.setText("");
                    InputTitle.setText("");
                } else {
                    Toast.makeText(AnnouncementCreation.this, "String is empty",
                            Toast.LENGTH_SHORT).show();
                }
            }
            //Method for adding new announcement to firebase
            private void AddAnnouncementFirebase(String announcement, String title) {
                Query announcements = AnnouncementDatabase.orderByChild("Date&time");
                announcements.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        num = (int) snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                String UniqueId = UUID.randomUUID().toString();
                String currentDateTime = java.text.DateFormat.getDateTimeInstance()
                        .format(new Date());
                AnnouncementDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        num = (int) snapshot.getChildrenCount();
                        AnnouncementDatabase.child(UniqueId).child("Announcement number")
                                .setValue(num);
                        AnnouncementDatabase.child(UniqueId).
                                child("Title").setValue(title);
                        AnnouncementDatabase.child(UniqueId).child("Announcement")
                                .setValue(announcement);
                        AnnouncementDatabase.child(UniqueId).child("Date&time").
                                setValue(currentDateTime);


                        Toast.makeText(AnnouncementCreation.this, "Announcement posted",
                                Toast.LENGTH_SHORT).show();

                    }
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AnnouncementCreation.this,
                                "Failed to post announcement",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}