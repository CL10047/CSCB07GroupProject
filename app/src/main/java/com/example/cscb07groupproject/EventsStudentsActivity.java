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

public class EventsStudentsActivity extends AppCompatActivity {

    static int currentPage = 1;
    static int numTotal;
    static int lastPage;
    final int pageSize = 5;
    Integer[] layoutID = {R.id.linear_layout1, R.id.linear_layout2, R.id.linear_layout3,
            R.id.linear_layout4, R.id.linear_layout5};
    Integer[] eventNames = {R.id.txt_name1, R.id.txt_name2, R.id.txt_name3, R.id.txt_name4,
            R.id.txt_name5};
    Integer[] eventInfo = {R.id.txt_info1, R.id.txt_info2, R.id.txt_info3, R.id.txt_info4,
            R.id.txt_info5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_students);
        getSupportActionBar().hide();
        // Setting layout for each event
        LinearLayout event1 = findViewById(layoutID[0]);
        LinearLayout event2 = findViewById(layoutID[1]);
        LinearLayout event3 = findViewById(layoutID[2]);
        LinearLayout event4 = findViewById(layoutID[3]);
        LinearLayout event5 = findViewById(layoutID[4]);

        // Setting click listeners for each event on the page
        event1.setOnClickListener(v -> getEventClicked(1));
        event2.setOnClickListener(v -> getEventClicked(2));
        event3.setOnClickListener(v -> getEventClicked(3));
        event4.setOnClickListener(v -> getEventClicked(4));
        event5.setOnClickListener(v -> getEventClicked(5));

        getEvents();

        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(view -> {
            Intent intent = new Intent(EventsStudentsActivity.this, StudentActivity.class);
            startActivity(intent);
        });
    }

    private void getEventClicked(int index) {
        ReviewEventActivity.getEventClicked(currentPage, index);
        // Navigate to rating page of event clicked
        Intent intent = new Intent(EventsStudentsActivity.this,
                ReviewEventActivity.class);
        startActivity(intent);
    }

    private void visibilityButton() {
        Button btnNext = findViewById(R.id.btn_next);
        Button btnPrevious = findViewById(R.id.btn_previous);
        if (currentPage == 1) {
            btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }
        if (numTotal <= pageSize) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
        if (numTotal % pageSize != 0 && numTotal > pageSize) {
            lastPage++;
        }
        if (currentPage == lastPage) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        btnNext.setOnClickListener(view -> switchPage(1));
        btnPrevious.setOnClickListener(view -> switchPage(-1));
    }

    private void clearEvents() {
        // Clearing event information from the screen
        for (int i = 0; i < pageSize; i++) {
            TextView txtEventName = findViewById(eventNames[i]);
            TextView txtEventInfo = findViewById(eventInfo[i]);

            txtEventName.setText("");
            txtEventInfo.setText("");
        }
    }

    private void checkFull() {
        // Checking if page is full and adjusting background
        if (numTotal < pageSize) {
            for (int i = numTotal; i < pageSize; i++) {
                findViewById(layoutID[i]).setBackground(null);
            }
        } else if (numTotal % pageSize != 0) {
            if (currentPage == lastPage) {
                for (int j = numTotal % pageSize; j < pageSize; j++) {
                    findViewById(layoutID[j]).setBackground(null);
                }
            } else {
                for (int j = numTotal % pageSize; j < pageSize; j++) {
                    findViewById(layoutID[j]).setBackgroundResource(R.drawable.gradient_blue);
                }
            }
        }
    }

    private void switchPage(int i) {
        // Switch between pages
        currentPage += i;
        getEvents();
    }

    private void getEvents() {
        // Retrieve events from firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
        // Query to fetch the total number of events
        Query totalNum = eventDatabase.orderByChild("Event Number");
        totalNum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numTotal = (int) snapshot.getChildrenCount();
                lastPage = numTotal / pageSize;
                clearEvents();
                visibilityButton();
                checkFull();

                // Query to fetch a page of event
                Query events = eventDatabase.limitToFirst(pageSize).orderByChild("Event Number")
                        .startAfter((currentPage - 1) * pageSize);
                events.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;

                        // Iterate through fetched events and update the UI
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            TextView txtEventName = findViewById(eventNames[count]);
                            TextView txtEventInfo = findViewById(eventInfo[count]);
                            String eventName = eventSnapshot.child("Event Name")
                                    .getValue(String.class);
                            String eventInfo = eventSnapshot.child("Event Location")
                                    .getValue(String.class) + " | "
                                    + eventSnapshot.child("Event Date").getValue(String.class);

                            txtEventName.setSingleLine(true);
                            txtEventInfo.setSingleLine(true);

                            txtEventName.setText(eventName);
                            txtEventInfo.setText(eventInfo);

                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(getApplicationContext(), "Failure in getting data.",
                                Toast.LENGTH_SHORT).show();
                    }
                });

                // Update the current page number on the UI
                TextView txtPageNumber = findViewById(R.id.txt_page_number);
                txtPageNumber.setText(Integer.toString(currentPage));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failure in getting data.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Static method to save the last viewed page
    public static void savePage(int lastViewedPage) {
        currentPage = lastViewedPage;
    }
}
