package com.example.cscb07groupproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class AnnouncementView extends AppCompatActivity {
    static int index = 0;
    static int numTotal;
    static int lastpage;
    static int currentpage = 1;
    final int pagesize = 5;
    Integer[] layoutID = {R.id.linear_layout1,R.id.linear_layout2, R.id.linear_layout3,
            R.id.linear_layout4, R.id.linear_layout5};
    Integer[] Titles = {R.id.txt_name1, R.id.txt_name2, R.id.txt_name3, R.id.txt_name4,
            R.id.txt_name5};
    Integer[] Announcements = {R.id.txt_info1, R.id.txt_info2, R.id.txt_info3, R.id.txt_info4,
            R.id.txt_info5};
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference AnnouncementDatabase = firebaseDatabase.getReference("Announcements");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.announcement_view);
        getAnnouncements();

        LinearLayout event1 = findViewById(layoutID[0]);
        LinearLayout event2 = findViewById(layoutID[1]);
        LinearLayout event3 = findViewById(layoutID[2]);
        LinearLayout event4 = findViewById(layoutID[3]);
        LinearLayout event5 = findViewById(layoutID[4]);

        event1.setOnClickListener(view -> getTitleClicked(1));
        event2.setOnClickListener(view -> getTitleClicked(2));
        event3.setOnClickListener(view -> getTitleClicked(3));
        event4.setOnClickListener(view -> getTitleClicked(4));
        event5.setOnClickListener(view -> getTitleClicked(5));

        count();



    }
    //Counting number of announcement in the database since user viewed at announcements
    private void count(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference AnnouncementDatabase = firebaseDatabase.getReference("Announcements");
        Query announcements = AnnouncementDatabase.orderByChild("Title");
        SharedPreferences sharepref = getSharedPreferences("Pref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharepref.edit();
        announcements.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counts = (int) snapshot.getChildrenCount();
                editor.putInt("Announcements count", counts);
                editor.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    private void getTitleClicked(int index) {
        SpecificAnnouncement.getEventClicked(currentpage, index);
        Intent intent = new Intent(AnnouncementView.this,
                SpecificAnnouncement.class);
        startActivity(intent);
    }
    private void visibilityButton() {
        Button btnNext = findViewById(R.id.btn_next);
        Button btnPrevious = findViewById(R.id.btn_previous);

        if (currentpage == 1) {
            btnPrevious.setVisibility(View.INVISIBLE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
        }
        if (numTotal < pagesize) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        if (numTotal % pagesize != 0) {
            lastpage++;
        }

        if (currentpage == lastpage) {
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }

        btnNext.setOnClickListener(view -> switchPage(1));
        btnPrevious.setOnClickListener(view -> switchPage(-1));
    }
    private void clearEvents() {
        for (int i = 0; i < pagesize; i++) {
            TextView txtEventName = findViewById(Titles[i]);
            TextView txtEventInfo = findViewById(Announcements[i]);
            txtEventName.setText("");
            txtEventInfo.setText("");
        }
    }
    private void checkFull() {
        if (numTotal < pagesize) {
            for (int i = 0; i < numTotal; i++) {
                findViewById(layoutID[i]).setBackgroundResource(R.drawable.gradient_blue);
            }
            for (int i = numTotal; i < pagesize; i++) {
                findViewById(layoutID[i]).setBackground(null);
            }
        } else if (numTotal % pagesize != 0) {
            if (currentpage == lastpage) {
                for (int j = numTotal % pagesize; j < pagesize; j++) {
                    findViewById(layoutID[j]).setBackground(null);
                }
            } else {
                for (int j = numTotal % pagesize; j < pagesize; j++) {
                    findViewById(layoutID[j]).setBackgroundResource(R.drawable.gradient_blue);
                }
            }
        }
    }
    private void switchPage(int i) {
        currentpage += i;
        getAnnouncements();
    }
    public static void savePage(int lastViewedPage) {
        currentpage = lastViewedPage;
    }
    private void getAnnouncements(){
        Query totalnum = AnnouncementDatabase.orderByChild("Announcement number");
        totalnum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numTotal = (int) snapshot.getChildrenCount();
                lastpage = numTotal / pagesize;
                clearEvents();
                visibilityButton();
                checkFull();
                Query events = AnnouncementDatabase.limitToFirst(pagesize)
                        .orderByChild("Announcement number")
                        .startAfter(((currentpage-1) * pagesize)-1);
                events.addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = 0;
                        for (DataSnapshot AnnouncementSnapshot : snapshot.getChildren()) {
                            if(count < pagesize) {
                                TextView txtTitle = findViewById(Titles[count]);
                                TextView txtAnnouncement = findViewById(Announcements[count]);
                                String Title = AnnouncementSnapshot.child("Title")
                                        .getValue(String.class);
                                String Announcement = AnnouncementSnapshot.child("Announcement")
                                        .getValue(String.class) + " | "
                                        + AnnouncementSnapshot.child("Date&time").getValue(String.class);
                                txtTitle.setSingleLine(true);
                                txtAnnouncement.setSingleLine(true);

                                txtTitle.setText(Title);
                                txtAnnouncement.setText(Announcement);
                            }

                            count++;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AnnouncementView.this,
                                "Failure in getting data.", Toast.LENGTH_SHORT).show();
                    }
                });
                TextView txtPageNumber = findViewById(R.id.txt_page_number);
                txtPageNumber.setText(Integer.toString(currentpage));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AnnouncementView.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}




