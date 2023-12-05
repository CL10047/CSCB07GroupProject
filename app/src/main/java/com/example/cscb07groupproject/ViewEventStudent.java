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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewEventStudent extends AppCompatActivity {

    static int currentPage = 1;
    final int pageSize = 5;
    static int numTotal;
    static int lastPage;

    Integer[] layoutID = {R.id.linear_layout1, R.id.linear_layout2, R.id.linear_layout3,
            R.id.linear_layout4, R.id.linear_layout5};
    Integer[] eventNames = {R.id.txt_name1, R.id.txt_name2, R.id.txt_name3, R.id.txt_name4,
            R.id.txt_name5};
    Integer[] eventInfo = {R.id.txt_info1, R.id.txt_info2, R.id.txt_info3, R.id.txt_info4,
            R.id.txt_info5};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event_student);

        TextView txtPageNumber = findViewById(R.id.txt_page_number);
        txtPageNumber.setText(Integer.toString(currentPage));
        getEvents();
        getSupportActionBar().hide();
        count();

        LinearLayout event1 = findViewById(layoutID[0]);
        LinearLayout event2 = findViewById(layoutID[1]);
        LinearLayout event3 = findViewById(layoutID[2]);
        LinearLayout event4 = findViewById(layoutID[3]);
        LinearLayout event5 = findViewById(layoutID[4]);

        event1.setOnClickListener(view -> getEventClicked(currentPage, 1));
        event2.setOnClickListener(view -> getEventClicked(currentPage, 2));
        event3.setOnClickListener(view -> getEventClicked(currentPage, 3));
        event4.setOnClickListener(view -> getEventClicked(currentPage, 4));
        event5.setOnClickListener(view -> getEventClicked(currentPage, 5));
        //button back to homepage
        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewEventStudent.this, StudentActivity.class);
                startActivity(intent);
            }
        });
    }
    private void count(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference EventDB = firebaseDatabase.getReference("events");
        SharedPreferences sharepref = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();

        Query events = EventDB.orderByChild("Event number");
        events.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counts = (int) snapshot.getChildrenCount();
                editor.putInt("Events count", counts);
                editor.apply();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void getEventClicked(int currentPage, int index) {
        ViewSpecificEventStudent.getEventClicked(currentPage, index);
        Intent intent = new Intent(ViewEventStudent.this,
                ViewSpecificEventStudent.class);
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
        if (numTotal < pageSize) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        if (numTotal % pageSize != 0) {
            lastPage++;
        }

        if (currentPage == lastPage) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

        btnNext.setOnClickListener(view -> switchPage(1));
        btnPrevious.setOnClickListener(view -> switchPage(-1));
    }

    private void clearEvents() {
        for (int i = 0; i < pageSize; i++) {
            TextView txtEventName = findViewById(eventNames[i]);
            TextView txtEventInfo = findViewById(eventInfo[i]);

            txtEventName.setText("");
            txtEventInfo.setText("");
        }
    }
    private void checkFull() {
        if (numTotal % pageSize != 0) {
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
    public void getEvents() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
        TextView txtPageNumber = findViewById(R.id.txt_page_number);
        clearEvents();
        visibilityButton();
        checkFull();

        Query query = eventDatabase.limitToFirst(pageSize).orderByChild("Event Number").startAfter((currentPage - 1) * pageSize);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count = 0;
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    TextView txtEventName = findViewById(eventNames[count]);
                    TextView txtEventInfo = findViewById(eventInfo[count]);
                    String eventName = eventSnapshot.child("Event Name").getValue(String.class);
                    String eventInfo = eventSnapshot.child("Event Location").getValue(String.class)
                            + " | " + eventSnapshot.child("Event Date").getValue(String.class);

                    txtEventName.setSingleLine(true);
                    txtEventInfo.setSingleLine(true);

                    txtEventName.setText(eventName);
                    txtEventInfo.setText(eventInfo);

                    count++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewEventStudent.this, "Failure in getting data.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        Query totalNum = eventDatabase.orderByChild("Event Number");
        totalNum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numTotal = (int) snapshot.getChildrenCount();
                lastPage = numTotal / pageSize;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewEventStudent.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });


        txtPageNumber.setText(Integer.toString(currentPage));
    }

    public static void savePage(int lastViewedPage) {
        currentPage = lastViewedPage;
    }

    private void switchPage(int i) {
        currentPage += i;
        getEvents();
    }
}