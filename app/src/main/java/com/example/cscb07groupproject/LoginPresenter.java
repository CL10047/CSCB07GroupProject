package com.example.cscb07groupproject;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;

public class LoginPresenter extends AppCompatActivity {

    LoginModel model;
    LoginView view;

    public LoginPresenter(){
    }

    public LoginPresenter(LoginView view, LoginModel model) {
        this.model = model;
        this.view = view;
    }

    public void checkDB(String username, String password) {
        if (TextUtils.isEmpty(username)) {
            view.setWarning("Enter username");
            view.setToast("Enter username");
            return;
        } else if (TextUtils.isEmpty(password)) {
            view.setWarning("Enter password");
            view.setToast("Enter password");
            return;
        } else {
            model.queryDB(this, username, password);
        }
    }

    public void checkCredentials(DataSnapshot snapshot, String username, String password) {

        if (snapshot.hasChild(username)) {
            String getPassword = snapshot.child(username).child("password").getValue(String.class);
            if (getPassword.equals(password)){
                view.setWarning("Login Successful");
                view.setToast("Login Successful");

                if (snapshot.child(username).child("admin").getValue(Boolean.class) == true){
                    view.adminLogin();
                }
                else{
                    view.userLogin();
                }
            }
            else{
                view.setWarning("Wrong Password");
                view.setToast("Wrong Password");
            }
        }
        else {
            view.setWarning("Wrong Username");
            view.setToast("Wrong Username");
        }
    }
}
