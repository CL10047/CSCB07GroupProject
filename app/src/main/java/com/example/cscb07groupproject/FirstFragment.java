package com.example.cscb07groupproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cscb07groupproject.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        Button event_button = getView().findViewById(R.id.events_button); // Get the button reference

        event_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the activity context
                Activity activity = getActivity();

                // Create an intent to start the new activity
                Intent intent = new Intent(activity, ViewEventStudent.class);

                // Start the new activity
                activity.startActivity(intent);
            }
        });

        Button post_button = getView().findViewById(R.id.button_post); // Get the button reference
       post_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the activity context
                Activity activity = getActivity();

                // Create an intent to start the new activity
                Intent intent = new Intent(activity, PostQualifications.class);

                // Start the new activity
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}