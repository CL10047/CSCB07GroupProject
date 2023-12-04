package com.example.cscb07groupproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class LoginView extends AppCompatActivity {
    TextInputEditText editTextUsername, editTextPassword;
    Button buttonLogin;
    android.widget.ProgressBar ProgressBar;
    TextView textView;
    LoginPresenter presenter;
    TextView warning;

    //Initialize variables
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        presenter = new LoginPresenter(this, new LoginModel());

        editTextUsername = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.login_button);
        ProgressBar = findViewById(R.id.ProgressBar);
        textView = findViewById(R.id.registerNow);
        warning = findViewById(R.id.warning);

        //TextView to go to Register page
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });

        //Button to check credentials and Log in
        buttonLogin.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                ProgressBar.setVisibility(View.VISIBLE);
                String username, password;
                username = editTextUsername.getText().toString();
                password = editTextPassword.getText().toString();


                presenter.checkDB(username,password);

            }
        });
    }

    //Shows toast with warning on Login status
    public void setToast(String resultText){
        Toast.makeText(getApplicationContext(), resultText, Toast.LENGTH_SHORT).show();
    }

    //Shows a text with warning on Login status
    public void setWarning(String resultText){
        warning.setVisibility(View.VISIBLE);
        warning.setText(resultText);
    }


    //Moves user to the admin page after successful login (if the user is an admin)
    public void adminLogin(){
        Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
        startActivity(intent);
        finish();
    }

    //Moves user to the main page after a successful login (if the user is not an admin)
    public void userLogin(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}