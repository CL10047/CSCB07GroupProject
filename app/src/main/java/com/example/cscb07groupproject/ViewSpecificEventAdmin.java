package com.example.cscb07groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import java.text.DecimalFormat;

public class ViewSpecificEventAdmin extends AppCompatActivity {
    static int currentPage;
    static int index;
    final int pageSize = 5;
    final int feedbackPageSize = 4;
    static int currentFeedbackPage = 1;
    static int numTotal;
    static int lastFeedbackPage;
    Integer eventNumber;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference eventDatabase = firebaseDatabase.getReference("events");

    Integer[] infoID = {R.id.txt_event_name, R.id.txt_created_date, R.id.txt_event_info,
            R.id.txt_description};
    Integer[] layoutID = {R.id.linear_layout1, R.id.linear_layout2, R.id.linear_layout3,
            R.id.linear_layout4, R.id.linear_layout5};
    Integer[] feedbackInfo = {R.id.txt_feedback1, R.id.txt_feedback2, R.id.txt_feedback3, R.id.txt_feedback4};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_speciific_event_admin);
        //calls function to retrieve event info
        getDetails();

        LinearLayout event1 = findViewById(layoutID[0]);
        LinearLayout event2 = findViewById(layoutID[1]);
        LinearLayout event3 = findViewById(layoutID[2]);
        LinearLayout event4 = findViewById(layoutID[3]);

        event1.setOnClickListener(view -> showFeedbackDetails(eventNumber, (currentFeedbackPage - 1) * feedbackPageSize + 1));
        event2.setOnClickListener(view -> showFeedbackDetails(eventNumber, (currentFeedbackPage - 1) * feedbackPageSize + 2));
        event3.setOnClickListener(view -> showFeedbackDetails(eventNumber, (currentFeedbackPage - 1) * feedbackPageSize + 3));
        event4.setOnClickListener(view -> showFeedbackDetails(eventNumber, (currentFeedbackPage - 1) * feedbackPageSize + 4));
    }
    private void showFeedbackDetails(int eventNumber, int feedbackNumber) {
        ViewFeedbackAdmin.getEventClicked(eventNumber, feedbackNumber);
        Intent intent = new Intent(ViewSpecificEventAdmin.this,
                ViewFeedbackAdmin.class);
        startActivity(intent);
    }
    private void returnToList() {
        //function to return to list of all events, to the last page you were on
        ViewEventsAdmin.savePage(currentPage);
        Intent intent = new Intent(ViewSpecificEventAdmin.this,
                ViewEventsAdmin.class);
        startActivity(intent);
    }
    private void clearEvents() {
        for (int i = 0; i < feedbackPageSize; i++) {
            TextView txtFeedbackInfo = findViewById(feedbackInfo[i]);
            txtFeedbackInfo.setText("");
        }
    }
    public static void getEventClicked(int pageOn, int eventClicked) {
        //function that gets called in ViewEventsAdmin to get which event was clicked
        currentPage = pageOn;
        index = eventClicked;
    }
    public static void savePage(int lastViewedPage) {
        currentFeedbackPage = lastViewedPage;
    }
    private void visibilityButton() {
        Button btnNext = findViewById(R.id.btn_next);
        Button btnPrevious = findViewById(R.id.btn_previous);

        if (currentFeedbackPage == 1) {
            btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }
        if (numTotal < feedbackPageSize) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        if (numTotal % feedbackPageSize != 0) {
            lastFeedbackPage++;
        }

        if (currentFeedbackPage == lastFeedbackPage) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

        btnNext.setOnClickListener(view -> switchPage(1));
        btnPrevious.setOnClickListener(view -> switchPage(-1));
    }
    private void checkFull() {
        if (numTotal <= feedbackPageSize) {
            for (int i = 0; i < numTotal; i++) {
                findViewById(layoutID[i]).setBackgroundResource(R.drawable.gradient_blue);
            }
            for (int i = numTotal; i < feedbackPageSize; i++) {
                findViewById(layoutID[i]).setBackground(null);
            }
        } else {
            if (currentFeedbackPage == lastFeedbackPage) {
                if (numTotal % feedbackPageSize == 0) {
                    for (int i = 0; i < feedbackPageSize; i++) {
                        findViewById(layoutID[i]).setBackgroundResource(R.drawable.gradient_blue);
                    }
                } else {
                    for (int i = 0; i < feedbackPageSize; i++) {
                        findViewById(layoutID[i]).setBackgroundResource(R.drawable.gradient_blue);
                    }
                    for (int i = numTotal % feedbackPageSize; i < feedbackPageSize; i++) {
                        findViewById(layoutID[i]).setBackground(null);
                    }
                }
            } else {
                for (int i = 0; i < feedbackPageSize; i++) {
                    findViewById(layoutID[i]).setBackgroundResource(R.drawable.gradient_blue);
                }
            }
        }
    }
    private void getDetails() {
        TextView txtPageNumber = findViewById(R.id.txt_page_number);
        txtPageNumber.setText(Integer.toString(currentFeedbackPage));

        TextView txtEventName = findViewById(infoID[0]);
        TextView txtCreatedDate = findViewById(infoID[1]);
        TextView txtEventInfo = findViewById(infoID[2]);
        TextView txtEventDescription = findViewById(infoID[3]);


        Button btnBack = findViewById(R.id.btn_back_to_list);
        btnBack.setOnClickListener(view -> returnToList());



        //query to get info from the event that was clicked
        Query events = eventDatabase.limitToFirst(1).orderByChild("Event Number")
                .startAfter((currentPage - 1) * pageSize + (index - 1));
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
                    eventNumber = eventSnapshot.child("Event Number")
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

                    DatabaseReference feedbackDatabase = eventDatabase.child(Integer.toString(eventNumber)).child("Reviews");
                    Query feedbackInfoData = feedbackDatabase.orderByChild("ReviewNumber");
                    feedbackInfoData.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot feedback) {
                            numTotal = (int) feedback.getChildrenCount();
                            lastFeedbackPage = numTotal / feedbackPageSize;
                            clearEvents();
                            visibilityButton();
                            checkFull();
                            if (numTotal != 0) {
                                int numSum = 0;
                                double avgNum;
                                for (DataSnapshot feedbacks : feedback.getChildren()) {
                                    Integer rating = feedbacks.child("Rating")
                                            .getValue(Integer.class);

                                    numSum += rating;
                                }
                                avgNum = ((double) numSum) / numTotal;
                                TextView ratingCount = findViewById(R.id.txtRatingCount);
                                TextView avgRating = findViewById(R.id.txtRatingAvg);
                                DecimalFormat decimalFormat = new DecimalFormat("#.#");

                                if (numTotal != 0) {
                                    String txtAvgRating = "Rating: " + decimalFormat.format(avgNum) + " / 5";
                                    avgRating.setText(txtAvgRating);
                                }
                                String txtRatingCount = numTotal + " Review(s)";
                                ratingCount.setText(txtRatingCount);
                            } else {
                                for (int i = 0; i < 4; i++) {
                                    findViewById(layoutID[i]).setBackground(null);
                                }
                                TextView txtFeedback = findViewById(R.id.txtFeedbackTitle);
                                txtFeedback.setText("No Feedbacks");
                                findViewById(R.id.btn_next).setVisibility(View.INVISIBLE);
                                findViewById(R.id.btn_previous).setVisibility(View.INVISIBLE);
                                findViewById(R.id.txt_page_number).setVisibility(View.INVISIBLE);
                            }

                            Query feedbackData = feedbackDatabase.limitToFirst(feedbackPageSize).orderByChild("ReviewNumber").startAfter((currentFeedbackPage - 1) * feedbackPageSize);
                            feedbackData.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot feedback) {
                                    int count = 0;
                                    for (DataSnapshot feedbacks : feedback.getChildren()) {
                                        TextView txtFeedbackInfo = findViewById(feedbackInfo[count]);
                                        Integer rating = feedbacks.child("Rating")
                                                .getValue(Integer.class);
                                        String review = feedbacks.child("Review")
                                                .getValue(String.class);

                                        String feedbackText = rating + " | " + review;
                                        txtFeedbackInfo.setText(feedbackText);
                                        count++;
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(ViewSpecificEventAdmin.this,
                                            "Failure in getting data.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(ViewSpecificEventAdmin.this,
                                    "Failure in getting data.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewSpecificEventAdmin.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchPage(int i) {
        currentFeedbackPage += i;
        getDetails();
    }
}
