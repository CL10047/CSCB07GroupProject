package com.example.cscb07groupproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cscb07--project-db-default-rtdb.firebaseio.com/");
    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar ProgressBar;
    TextView textView;
    Switch admin;

    Boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.Register_button);
        ProgressBar = findViewById(R.id.ProgressBar);
        textView = findViewById(R.id.loginNow);
        admin = findViewById(R.id.admin);
        isAdmin = false;

        //button back to login
        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, LoginView.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                startActivity(intent);
                finish();
            }
        });

        //Lever to choose if user will register as admin or not
        admin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    isAdmin = true;
                }
                else{
                    isAdmin = false;
                }
            }
        });

        //Button to upload credentials on database, while also giving warnings to user
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressBar.setVisibility(View.VISIBLE);
                String username, password;
                username = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                //warns user that username is empty
                if (TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this,"Enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                //warns user that password is empty
                else if (TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //Warn user that username already exists
                            if (snapshot.hasChild(username)) {
                                Toast.makeText(Register.this, "Username already registered.", Toast.LENGTH_SHORT).show();
                            }
                            //Upload username and password to database, and send user back to Login
                            else {
                                databaseReference.child("users").child(username).child("username").setValue(username);
                                databaseReference.child("users").child(username).child("password").setValue(password);
                                databaseReference.child("users").child(username).child("admin").setValue(isAdmin);

                                Toast.makeText(Register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginView.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}