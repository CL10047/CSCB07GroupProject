package com.example.cscb07groupproject;

import android.content.Intent;
import android.provider.ContactsContract;
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

    FirebaseDatabase db;

    // Initialize database
    public LoginModel(){
        db = FirebaseDatabase.getInstance("https://cscb07--project-db-default-rtdb.firebaseio.com/");
    }

    //Checks if credentials (username and password) appear in database
    public void queryDB(LoginPresenter presenter, String username, String password){
        DatabaseReference ref = db.getReference();
        DatabaseReference query = ref.child("users");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
           presenter.checkCredentials(snapshot, username, password);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
                }
        });
    }

    //Checks if the snapshot has a child with the text name
    public boolean checkChild(DataSnapshot snapshot, String text){
        return snapshot.hasChild(text);
    }

    //Gets password from database snapshot for user username
    public String getPassword(DataSnapshot snapshot, String username){
       return snapshot.child(username).child("password").getValue(String.class);
    }

    //Checks if username is an admin
    public boolean isAdmin(DataSnapshot snapshot, String username){
        return snapshot.child(username).child("admin").getValue(Boolean.class);
    }

}
