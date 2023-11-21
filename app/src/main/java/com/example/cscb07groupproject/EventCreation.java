package com.example.cscb07groupproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class EventCreation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_creation);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference eventDatabase = firebaseDatabase.getReference("events");
        Button btnCreateEvent = (Button) findViewById(R.id.create_event);
        //get the id from the input events with info and assign them to variables on button click
        EditText inputEventName = (EditText) findViewById(R.id.input_event_name);
        EditText inputEventDate = (EditText) findViewById(R.id.input_event_date);
        EditText inputMaxAttendees = (EditText) findViewById(R.id.input_max_attendees);
        EditText inputEventLocation = (EditText) findViewById(R.id.input_event_location);
        EditText inputEventDepartment = (EditText) findViewById(R.id.input_event_department);
        EditText inputEventDescription = (EditText) findViewById(R.id.input_event_description);

        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eventName = inputEventName.getText().toString();
                String eventDate = inputEventDate.getText().toString();
                String maxAttendees = inputMaxAttendees.getText().toString();
                String eventLocation = inputEventLocation.getText().toString();
                String eventDepartment = inputEventDepartment.getText().toString();
                String eventDescription = "N/A";

                if (inputEventDescription.length() != 0)
                    eventDate = inputEventDescription.getText().toString();

                try {if (!verifyInput(eventName, eventDate, maxAttendees, eventLocation,
                        eventDepartment)) {
                        Toast.makeText(EventCreation.this, "Please input valid data.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        addEventToFirebase(eventName, eventDate, maxAttendees, eventLocation,
                                eventDepartment, eventDescription);

                        inputEventName.setText("");
                        inputEventDate.setText("");
                        inputMaxAttendees.setText("");
                        inputEventLocation.setText("");
                        inputEventDepartment.setText("");
                        inputEventDescription.setText("");
                    }
                } catch (ParseException e) {
                throw new RuntimeException(e);
                }
            }

            //checks over necessary input values to make sure they're valid
            private boolean verifyInput(String eventName, String eventDate, String maxAttendees,
                                        String eventLocation,
                                        String eventDepartment) throws ParseException {
                Date currentDate = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",
                        Locale.ENGLISH);
                Date parsedDate = dateFormat.parse(eventDate);

                if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventDate) ||
                        TextUtils.isEmpty(maxAttendees) || TextUtils.isEmpty(eventLocation) ||
                        TextUtils.isEmpty(eventDepartment)) {
                    return false;
                }
                if (Integer.parseInt(maxAttendees) == 0) {
                    return false;
                }
                if (!(eventDate.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})")) ||
                        parsedDate.before(currentDate)) {
                    return false;
                }
                return true;
            }

            //add event to firebase using input values
            private void addEventToFirebase(String eventName, String eventDate,
                                            String maxAttendees, String eventLocation,
                                            String eventDepartment, String eventDescription) {
                String uniqueID = UUID.randomUUID().toString();
                Date currentDate = new Date();
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy",
                        Locale.ENGLISH);
                SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy",
                        Locale.ENGLISH);
                Date date;

                try {date = format1.parse(eventDate);
                } catch (ParseException e) {
                throw new RuntimeException(e);
                }
                String formattedDate = format2.format(date);

                String dateCreated = DateFormat.getDateInstance().format(currentDate);
                Integer maxNumAttendees = Integer.parseInt(maxAttendees);
                eventDatabase.addValueEventListener(new ValueEventListener() {
                    //use randomly generated unique ID to act as names for each event to get over
                    //firebase issues for adding without replacing data
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventDatabase.child(uniqueID).child("Event Name").
                                setValue(eventName);
                        eventDatabase.child(uniqueID).child("Event Date").
                                setValue(formattedDate);
                        eventDatabase.child(uniqueID).child("Max Attendees").
                                setValue(maxNumAttendees);
                        eventDatabase.child(uniqueID).child("Event Location").
                                setValue(eventLocation);
                        eventDatabase.child(uniqueID).child("Event Department").
                                setValue(eventDepartment);
                        eventDatabase.child(uniqueID).child("Event Description").
                                setValue(eventDescription);
                        eventDatabase.child(uniqueID).child("Date Created").
                                setValue(dateCreated);

                        Toast.makeText(EventCreation.this, "Event Added",
                                Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EventCreation.this, "Failed to add event",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
