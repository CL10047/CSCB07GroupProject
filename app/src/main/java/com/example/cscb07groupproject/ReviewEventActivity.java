package com.example.cscb07groupproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ReviewEventActivity extends AppCompatActivity {


    // Page handling parameters
    static int currentPage;
    static int index;
    final int pageSize = 5;

    // UI elements
    private RatingBar ratingBar;
    private EditText editTextReviewView;
    Integer eventNumber;
    DatabaseReference reviewReference;

    private static final String ARG_REVIEW_TEXT = "review_text";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_event);
        getDetails();
        getSupportActionBar().hide();

        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(ReviewEventActivity.this, EventsStudentsActivity.class);
            startActivity(intent);
        });

        editTextReviewView = findViewById(R.id.review_events);
        ratingBar = findViewById(R.id.ratingBar);
        Button buttonSubmitReview = findViewById(R.id.buttonSubmitReview);

        // Check whether there is a review input
        if (getIntent() != null && getIntent().getExtras() != null) {
            String editTextReview = getIntent().getStringExtra(ARG_REVIEW_TEXT);
            editTextReviewView.setText(editTextReview);
        }

        buttonSubmitReview.setOnClickListener(view -> {
            float rating = ratingBar.getRating();
            String reviewText = editTextReviewView.getText().toString();

            if (!TextUtils.isEmpty(reviewText)) {
                if(!hasUserAlreadySubmittedReview()){
                    // Submit review and rating if there is input review
                    // and user has not already submitted a review for this event
                    submitRatingAndReview(rating, reviewText);
                    Toast.makeText(this, "Review submitted successfully!",
                            Toast.LENGTH_SHORT).show();
                    buttonSubmitReview.setEnabled(false);
                }
                else{
                    Toast.makeText(this, "You have already submitted" +
                            " a review for this event", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "Please enter your comment",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void returnToList() {
        EventsStudentsActivity.savePage(currentPage);
        Intent intent = new Intent(ReviewEventActivity.this,
                EventsStudentsActivity.class);
        startActivity(intent);
    }

    public static void getEventClicked(int pageOn, int eventClicked) {
        currentPage = pageOn;
        index = eventClicked;
    }

    private void submitRatingAndReview(Float rating, String review) {

        reviewReference = FirebaseDatabase.getInstance().getReference()
                .child("events")
                .child(Integer.toString(eventNumber))
                .child("Reviews");
        String reviewId = reviewReference.push().getKey();

        reviewReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long newReviewCount = snapshot.getChildrenCount() + 1;

                // Use incremented count as the review number
                int reviewNumber = (int) newReviewCount;

                // Reference for the new review
                DatabaseReference newReviewRef = reviewReference.push();
                newReviewRef.child("Rating").setValue(rating);
                newReviewRef.child("Review").setValue(review);
                newReviewRef.child("ReviewNumber").setValue(reviewNumber);
                clearReview();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewEventActivity.this, "Failure to get data",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearReview() {
        TextView txtComplaintTitle = findViewById(R.id.review_events);

        txtComplaintTitle.setText("");
    }

    private void getDetails() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
        TextView txtEventName = findViewById(R.id.txt_event_name);
        TextView txtCreatedDate = findViewById(R.id.txt_created_date);
        TextView txtEventInfo = findViewById(R.id.txt_event_info);
        TextView txtEventDescription = findViewById(R.id.txt_description);

        Button btnBack = findViewById(R.id.btn_back_to_list);
        btnBack.setOnClickListener(view -> returnToList());

        Query events = eventDatabase.limitToFirst(1).orderByChild("Event Number")
                .startAfter((currentPage - 1) * pageSize + (index - 1));
        events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    // Get event details from the database
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
                    eventNumber = eventSnapshot.child("Event Number")
                            .getValue(Integer.class);

                    String eventInfo = "Location: " + eventLocation + " | Event Date: " + eventDate
                            + " | Max Attendees: " + maxAttendees + " | Department: "
                            + eventDepartment;

                    assert eventDescription != null;
                    if (eventDescription.equals("N/A")) {
                        eventDescription = "No Event Description";
                    }

                    // Display event details on the UI
                    txtEventName.setText(eventName);
                    txtCreatedDate.setText(dateCreated);
                    txtEventInfo.setText(eventInfo);
                    txtEventDescription.setText(eventDescription);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReviewEventActivity.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean hasUserAlreadySubmittedReview() {
        // Get the current Firebase user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Reference to the user's review for the current event
            DatabaseReference userReviewReference = reviewReference.child(userId);

            // Check if there are any reviews for the current event under the user's ID
            return userReviewReference.getKey() != null;
        } else {
            // User is not authenticated
            return false;
        }
    }
}
