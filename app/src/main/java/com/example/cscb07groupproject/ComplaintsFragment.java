package com.example.cscb07groupproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;
import java.util.Date;
import java.text.SimpleDateFormat;


public class ComplaintsFragment extends Fragment {

    private String editTextComplaint;
    private String complaintTopic;
    private EditText editTextComplaintView;
    private EditText editTextComplaintTopicView;

    //fragment initialization parameters
    private static final String ARG_COMPLAINT_TEXT = "complaint_text";
    private static final String ARG_COMPLAINT_TOPIC = "complaint_topic";


    // TODO: Rename and change types of parameters

    public ComplaintsFragment() {
        // Required empty public constructor
    }

    public static ComplaintsFragment newInstance(String complaintText, String complaintTopic) {
        ComplaintsFragment fragment = new ComplaintsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COMPLAINT_TEXT, complaintText);
        args.putString(ARG_COMPLAINT_TOPIC, complaintTopic);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            editTextComplaint = getArguments().getString(ARG_COMPLAINT_TEXT);
            complaintTopic = getArguments().getString(ARG_COMPLAINT_TOPIC);
        }
    }
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);

        editTextComplaintView = view.findViewById(R.id.editTextComplaint);
        editTextComplaintTopicView = view.findViewById(R.id.editTextComplaintTopic);
        Button buttonSubmitComplaint = view.findViewById(R.id.buttonSubmitComplaint);

        if (getArguments() != null) {
            editTextComplaint = getArguments().getString(ARG_COMPLAINT_TEXT);
            complaintTopic = getArguments().getString(ARG_COMPLAINT_TOPIC);
            editTextComplaintView.setText(editTextComplaint);
            editTextComplaintTopicView.setText(complaintTopic);
        }
        buttonSubmitComplaint.setOnClickListener(view1 -> {
            String complaintText = editTextComplaintView.getText().toString();
            String complaintTopicText = editTextComplaintTopicView.getText().toString();

            if (!TextUtils.isEmpty(complaintText) && !TextUtils.isEmpty(complaintTopicText)) {
                submitComplaint(complaintText, complaintTopicText);
                Toast.makeText(requireContext(), "Complaint submitted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Please enter your complaint and its topic", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void submitComplaint(String complaint, String topic) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference complaintReference = firebaseDatabase.getReference("complaints");
        // Generate unique ID
        String complaintId = complaintReference.push().getKey();

        // Create a Complaint object
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String dateTime = sdf.format(new Date());

        DatabaseReference newComplaintRef = complaintReference.child(complaintId);
        newComplaintRef.child("Description").setValue(complaint);
        newComplaintRef.child("Title").setValue(topic);
        newComplaintRef.child("Date").setValue(dateTime);

        Toast.makeText(getContext(), "Complaint submitted successfully!", Toast.LENGTH_SHORT).show();
    }
}