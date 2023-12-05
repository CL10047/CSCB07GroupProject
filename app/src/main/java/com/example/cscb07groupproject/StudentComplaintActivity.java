package com.example.cscb07groupproject;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StudentComplaintActivity extends AppCompatActivity {

    private String editTextComplaint;
    private String complaintTopic;
    private EditText editTextComplaintView;
    private EditText editTextComplaintTopicView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint);

        editTextComplaintView = findViewById(R.id.editTextComplaint);
        editTextComplaintTopicView = findViewById(R.id.editTextComplaintTopic);
        Button buttonSubmitComplaint = findViewById(R.id.buttonSubmitComplaint);

        // Setting values of arguments
        if (getIntent().getExtras() != null) {
            editTextComplaint = getIntent().getExtras().getString("complaint_text");
            complaintTopic = getIntent().getExtras().getString("complaint_topic");
            editTextComplaintView.setText(editTextComplaint);
            editTextComplaintTopicView.setText(complaintTopic);
        }
        // On click listener for submit button
        buttonSubmitComplaint.setOnClickListener(view -> {
            String complaintText = editTextComplaintView.getText().toString();
            String complaintTopicText = editTextComplaintTopicView.getText().toString();

            // Submit if fields are complete, show warning message if at least one field empty
            if (!TextUtils.isEmpty(complaintText) && !TextUtils.isEmpty(complaintTopicText)) {
                submitComplaint(complaintText, complaintTopicText);
                Toast.makeText(this, "Complaint submitted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please enter your complaint and its topic", Toast.LENGTH_SHORT).show();
            }
        });
        /*
        previousButton.setOnClickListener(new View.OnClickListener() { @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentComplaintActivity.this, StudentActivity.class);
                startActivity(intent);
            }
        });
         */
    }

    private void submitComplaint(String complaint, String topic) {
        // Initialize firebase references
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference complaintReference = firebaseDatabase.getReference("complaints");
        // Generate unique ID
        String complaintId = complaintReference.push().getKey();

        // Setting date formatting
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTime = sdf.format(new Date());

        // Creating complaint child node with description, title and date
        DatabaseReference newComplaintRef = complaintReference.child(complaintId);
        newComplaintRef.child("Description").setValue(complaint);
        newComplaintRef.child("Title").setValue(topic);
        newComplaintRef.child("Date").setValue(dateTime);
    }
}
