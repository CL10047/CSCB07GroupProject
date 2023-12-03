package com.example.cscb07groupproject;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginModel extends AppCompatActivity {

    DatabaseReference databaseReference;

    public LoginModel(){
        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cscb07--project-db-default-rtdb.firebaseio.com/");
    }

    public void queryDB(LoginPresenter presenter, String username, String password){

        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           presenter.checkCredentials(snapshot, username, password);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
                }
        });
    }

}
