package com.example.cscb07groupproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class EventCreation extends AppCompatActivity {
    private String eventName;
    private String eventDate;
    private String maxAttendees;
    private String eventLocation;
    private String eventDepartment;
    private String eventDescription;
    static int numTotal;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);
        Button btnCreateEvent = findViewById(R.id.btn_create_event);

        Button back = findViewById(R.id.back_btn);

        back.setOnClickListener(view -> {
            Intent intent = new Intent(EventCreation.this, AdminActivity.class);
            startActivity(intent);
        });

        Query totalNum = eventDatabase.orderByChild("Event Number");
        totalNum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                numTotal = (int) snapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventCreation.this,
                        "Failure in getting data.", Toast.LENGTH_SHORT).show();
            }
        });

        //get the id from the input events with info and assign them to variables on button click
        EditText inputEventName = findViewById(R.id.input_event_name);
        EditText inputEventDate = findViewById(R.id.input_event_date);
        EditText inputMaxAttendees = findViewById(R.id.input_max_attendees);
        EditText inputEventLocation = findViewById(R.id.input_event_location);
        EditText inputEventDepartment = findViewById(R.id.input_event_department);
        EditText inputEventDescription = findViewById(R.id.input_event_description);

        btnCreateEvent.setOnClickListener(view -> {
            eventName = inputEventName.getText().toString();
            eventDate = inputEventDate.getText().toString();
            maxAttendees = inputMaxAttendees.getText().toString();
            eventLocation = inputEventLocation.getText().toString();
            eventDepartment = inputEventDepartment.getText().toString();
            eventDescription = "N/A";

            if (inputEventDescription.length() != 0 && inputEventDescription.length() <= 100) {
                eventDescription = inputEventDescription.getText().toString();
            } else if (inputEventDescription.length() > 100) {
                Toast.makeText(EventCreation.this,
                        "Please write a maximum of 100 characters",
                        Toast.LENGTH_SHORT).show();
            }

            if (!verifyInput()) {
                Toast.makeText(EventCreation.this, "Please input valid data.",
                        Toast.LENGTH_SHORT).show();
            } else {
                numTotal += 1;
                addEventToFirebase();

                inputEventName.setText("");
                inputEventDate.setText("");
                inputMaxAttendees.setText("");
                inputEventLocation.setText("");
                inputEventDepartment.setText("");
                inputEventDescription.setText("");
            }
        });
    }
        //checks over necessary input values to make sure they're valid
    private boolean verifyInput(){
        if (eventName.equals("") || eventDate.equals("") ||
                maxAttendees.equals("") || eventLocation.equals("") ||
                eventDepartment.equals("") || maxAttendees.equals("0")) {
            return false;
        }
        if (!(eventDate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))) {
            return false;
        }
        return true;
    }

        //add event to firebase using input values
    private void addEventToFirebase () {
        String uniqueID = UUID.randomUUID().toString();
        Date currentDate = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy",
                Locale.ENGLISH);
        SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy",
                Locale.ENGLISH);
        Date date;

        try {
            date = format1.parse(eventDate);
            if (date.before(currentDate)) {
                Toast.makeText(EventCreation.this, "Input valid date",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        String formattedDate = format2.format(date);

        String dateCreated = DateFormat.getDateInstance().format(currentDate);
        Integer maxNumAttendees = Integer.parseInt(maxAttendees);
        String eventID = Integer.toString(numTotal);

        eventDatabase.child(eventID).child("Event Name").
                setValue(eventName);
        eventDatabase.child(eventID).child("Event Date").
                setValue(formattedDate);
        eventDatabase.child(eventID).child("Max Attendees").
                setValue(maxNumAttendees);
        eventDatabase.child(eventID).child("Event Location").
                setValue(eventLocation);
        eventDatabase.child(eventID).child("Event Department").
                setValue(eventDepartment);
        eventDatabase.child(eventID).child("Event Description").
                setValue(eventDescription);
        eventDatabase.child(eventID).child("Date Created").
                setValue(dateCreated);
        eventDatabase.child(eventID).child("Spots Taken").setValue(0);

        eventDatabase.child(eventID).child("Event Number").
                setValue(numTotal);
        Toast.makeText(EventCreation.this, "Event Added",
                Toast.LENGTH_SHORT).show();
    }
}

