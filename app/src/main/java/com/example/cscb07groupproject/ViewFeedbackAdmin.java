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


public class ViewFeedbackAdmin extends AppCompatActivity {
    static int eventNumber;
    static int feedbackNumber;
    Integer[] infoID = {R.id.txt_event_name_f, R.id.txt_created_date_f, R.id.txt_event_info_f,
            R.id.txt_description_f, R.id.txt_event_rating_f, R.id.txt_event_review_f};
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
    DatabaseReference feedbackDatabase = eventDatabase.child(Integer.toString(eventNumber)).child("Reviews");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_feedback_admin);

        //calls function to retrieve event info
        getDetails();
    }

    public static void getEventClicked(int eventNum, int feedbackNum) {
        //function that gets called in ViewEventsAdmin to get which event was clicked
        eventNumber = eventNum;
        feedbackNumber = feedbackNum;
    }
    private void returnToList() {
        //function to return to list of all events, to the last page you were on
        int currentPage = feedbackNumber / 4;
        if (feedbackNumber % 4 != 0) {
            currentPage++;
        }

        ViewSpecificEventAdmin.savePage(currentPage);
        Intent intent = new Intent(ViewFeedbackAdmin.this,
                ViewSpecificEventAdmin.class);
        startActivity(intent);
    }

    private void getDetails() {
        //gets detail from the events database
        TextView txtEventName = findViewById(infoID[0]);
        TextView txtCreatedDate = findViewById(infoID[1]);
        TextView txtEventInfo = findViewById(infoID[2]);
        TextView txtEventDescription = findViewById(infoID[3]);
        TextView feedbackRating = findViewById(infoID[4]);
        TextView feedbackReview = findViewById(infoID[5]);

        Button btnBack = findViewById(R.id.btn_back_to_list_f);
        btnBack.setOnClickListener(view -> returnToList());

        //query to get info from the event that was clicked
        Query events = eventDatabase.limitToFirst(1).orderByChild("Event Number")
                .equalTo(eventNumber);

        events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.child("Event Name")
                            .getValue(String.class);
                    String eventLocation = eventSnapshot.child("Event Location")
                            .getValue(String.class);
                    String dateCreated = "Created: " + eventSnapshot.child("Date Created")
                            .getValue(String.class);
                    String eventDate = eventSnapshot.child("Event Date")
                            .getValue(String.class);
                    String eventDepartment = eventSnapshot.child("Event Department")
                            .getValue(String.class);
                    String eventDescription = eventSnapshot.child("Event Description")
                            .getValue(String.class);
                    Integer maxAttendees = eventSnapshot.child("Max Attendees")
                            .getValue(Integer.class);
                    Integer spotsTaken = eventSnapshot.child("Spots Taken")
                            .getValue(Integer.class);

                    String eventInfo = "Location: " + eventLocation + " | Event Date: " + eventDate
                            + " | Max Attendees: " + maxAttendees + " | Spots Taken: " + spotsTaken
                            + " | Department: " + eventDepartment;

                    if (eventDescription.equals("N/A")) {
                        eventDescription = "No Event Description";
                    }

                    txtEventName.setText(eventName);
                    txtCreatedDate.setText(dateCreated);
                    txtEventInfo.setText(eventInfo);
                    txtEventDescription.setText(eventDescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFeedbackAdmin.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });

        Query feedbacks = feedbackDatabase.limitToFirst(1).orderByChild("ReviewNumber")
                .equalTo(feedbackNumber);
        feedbacks.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot feedback) {
                for (DataSnapshot feedbackSnapshot : feedback.getChildren()) {
                    Double rating = feedbackSnapshot.child("Rating").getValue(Double.class);
                    String review = feedbackSnapshot.child("Review").getValue(String.class);

                    String ratingTxt = "Rating: " + rating;
                    String reviewTxt = "Review: " + review;
                    feedbackRating.setText(ratingTxt);
                    feedbackReview.setText(reviewTxt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewFeedbackAdmin.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
