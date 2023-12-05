package com.example.cscb07groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SpecificAnnouncement extends AppCompatActivity {
    static int currentPage;
    static int index;
    final int pageSize = 5;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specific_announcement);

        getDetails();
    }
    private void returnToList() {
        AnnouncementView.savePage(currentPage);
        Intent intent = new Intent(SpecificAnnouncement.this,
                AnnouncementView.class);
        startActivity(intent);
    }
    public static void getEventClicked(int page, int eventClicked) {
        currentPage = page;
        index = eventClicked;
    }
    private void getDetails(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventDatabase = firebaseDatabase.getReference("Announcements");
        TextView txtTitle = findViewById(R.id.txt_event_name);
        TextView txtCreatedDate = findViewById(R.id.txt_created_date);
        TextView txtAnnouncement = findViewById(R.id.txt_event_info);

        Button btnBack = findViewById(R.id.btn_back_to_list);
        btnBack.setOnClickListener(view -> returnToList());

        Query events = eventDatabase.limitToFirst(1).orderByChild("Announcement number")
                .startAfter(((currentPage - 1) * pageSize +(index - 1))-1);
        events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String Title = eventSnapshot.child("Title")
                            .getValue(String.class);
                    String dateCreated = "Created: " + eventSnapshot.child("Date&time")
                            .getValue(String.class);
                    String Announcements = eventSnapshot.child("Announcement")
                            .getValue(String.class);


                    txtTitle.setText(Title);
                    txtCreatedDate.setText(dateCreated);
                    txtAnnouncement.setText(Announcements);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SpecificAnnouncement.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}