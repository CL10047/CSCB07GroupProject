package com.example.cscb07groupproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.appcompat.app.AppCompatActivity;


import com.example.cscb07groupproject.databinding.FragmentSecondBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;


public class AnnouncementCreation extends AppCompatActivity {

    private FragmentSecondBinding binding;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_second);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference AnnouncementDatabase = firebaseDatabase.getReference("Announcements");
        Button btnPostAnnouncement = (Button) findViewById(R.id.button2);
        EditText InputAnnouncement = (EditText) findViewById(R.id.textInputEditText2);

        btnPostAnnouncement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Announcement = InputAnnouncement.getText().toString();
                if (!Announcement.equals("")) {
                    AddAnnouncementFirebase(Announcement);
                    InputAnnouncement.setText("");
                } else {
                    Toast.makeText(AnnouncementCreation.this, "String is empty",
                            Toast.LENGTH_SHORT).show();
                }
            }

            private void AddAnnouncementFirebase(String announcement) {
                String UniqueId = UUID.randomUUID().toString();
                AnnouncementDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        AnnouncementDatabase.child(UniqueId).child("Announcement")
                                .setValue(announcement);

                        Toast.makeText(AnnouncementCreation.this, "Announcement posted",
                                Toast.LENGTH_SHORT).show();

                    }

                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AnnouncementCreation.this, "Failed to post announcement",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}