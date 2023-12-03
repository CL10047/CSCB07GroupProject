package com.example.cscb07groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ViewSpecificEventStudent extends AppCompatActivity {
 static int currentPage;
 static int index;
 final int pageSize = 5;
 public Integer spotsTaken;
 public Integer numSpots;

 public DatabaseReference rsvpAttendees;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_view_specific_event_student);

  FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
  DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
  TextView txtEventName = findViewById(R.id.txt_event_name);
  TextView txtCreatedDate = findViewById(R.id.txt_created_date);
  TextView txtEventInfo = findViewById(R.id.txt_event_info);
  TextView txtEventDescription = findViewById(R.id.txt_description);
  TextView txtSpotsLeft = findViewById(R.id.txt_spots_left);

  Button btnBack = findViewById(R.id.btn_back_to_list);
  btnBack.setOnClickListener(view -> returnToList());


  Query query = eventDatabase.limitToFirst(1).orderByChild("Event Number")
          .startAfter((currentPage - 1) * pageSize + (index - 1));

  query.addValueEventListener(new ValueEventListener() {
   @Override
   public void onDataChange(@NonNull DataSnapshot snapshot) {
    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
     /*Loops through all the "children" of the event the user clicked
      * to save all the information in variables
      */
     String eventName = eventSnapshot.child("Event Name").getValue(String.class);
     String eventLocation = eventSnapshot.child("Event Location").getValue(String.class);
     String dateCreated = "Created: " + eventSnapshot.child("Date Created").getValue(String.class);
     String eventDate = eventSnapshot.child("Event Date").getValue(String.class);
     String eventDepartment = eventSnapshot.child("Event Department").getValue(String.class);
     String eventDescription = eventSnapshot.child("Event Description").getValue(String.class);
     Integer maxAttendees = eventSnapshot.child("Max Attendees").getValue(Integer.class);
     Integer currAttendees = eventSnapshot.child("Spots Taken").getValue(Integer.class);
     rsvpAttendees = eventSnapshot.child("Spots Taken").getRef();
     spotsTaken = currAttendees;//had to make public variables for rsvp button
     numSpots = maxAttendees;
     String spotsLeft =  maxAttendees - currAttendees + "/" + maxAttendees + " spots left";
     String eventInfo = "Location: " + eventLocation + " | Event Date: " + eventDate
             + " | Max Attendees: " + maxAttendees + " Department: " + eventDepartment;

     if (eventDescription.equals("N/A")) {
      eventDescription = "No Event Description";
     }
     //set text view to variables in previous --> displays event info
     txtEventName.setText(eventName);
     txtCreatedDate.setText(dateCreated);
     txtEventInfo.setText(eventInfo);
     txtEventDescription.setText(eventDescription);
     txtSpotsLeft.setText(spotsLeft);

    }
    Button btnRsvp = findViewById(R.id.btn_rsvp);
    btnRsvp.setOnClickListener(view -> addAttendee(rsvpAttendees, spotsTaken, numSpots));
   }


   @Override
   public void onCancelled(@NonNull DatabaseError error) {
    Toast.makeText(ViewSpecificEventStudent.this, "Failure in getting data.",
            Toast.LENGTH_SHORT).show();

   }
  });
 }

 public static void getEventClicked(int pageOn, int eventClicked) {
  currentPage = pageOn;
  index = eventClicked;
 }

 private void returnToList() {
  ViewEventStudent.savePage(currentPage);
  Intent intent = new Intent(ViewSpecificEventStudent.this, ViewEventStudent.class);
  startActivity(intent);
 }

 private void addAttendee(DatabaseReference spotsTaken, Integer numAttendees,Integer maxAttendees) {
  /*
  * Input: reference to the event, Current number of attendees,Max number of attendees
  * Output: void
  * Adds a new attendee if there is spots left,
  * i.e if numAttendees < maxAttendees
  * */
  Integer test = numAttendees+1;
  if (numAttendees < maxAttendees) {
   HashMap Attendee = new HashMap<>();
   Attendee.put("Spots Taken",numAttendees+1);

   spotsTaken.setValue(test).addOnCompleteListener(new OnCompleteListener<Void>() {
    @Override
    public void onComplete(@NonNull Task<Void> task) {
     if (task.isSuccessful()) {
      Toast.makeText(ViewSpecificEventStudent.this, "Successfully added Attendee",
              Toast.LENGTH_SHORT).show();
     } else {
      Toast.makeText(ViewSpecificEventStudent.this, "Could not add Attendee",
              Toast.LENGTH_SHORT).show();
     }
    }
   });
  } else {
   Toast.makeText(ViewSpecificEventStudent.this, "No more spots left in the event",
           Toast.LENGTH_SHORT).show();
  }
 }
}

