package com.example.cscb07groupproject;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;

public class LoginPresenter {

    LoginModel model;
    LoginView view;

    public LoginPresenter(){
    }

    public LoginPresenter(LoginView view, LoginModel model) {
        this.model = model;
        this.view = view;
    }

    //Checks if username and password are empty. If they aren't, it checks to see if they appear
    //on the database
    public void checkDB(String username, String password) {
        if (username.equals("")) {
            view.setWarning("Enter username");
            view.setToast("Enter username");
            return;
        } else if (password.equals("")) {
            view.setWarning("Enter password");
            view.setToast("Enter password");
            return;
        } else {
            model.queryDB(this, username, password);
        }
    }

    //Checks if username and password are valid credentials, sending warnings if they aren't and
    //sending the user to the appropriate page if they are valid
    public void checkCredentials(DataSnapshot snapshot, String username, String password) {

        if (model.checkChild(snapshot, username)) {
            String getPassword = model.getPassword(snapshot, username);
            if (getPassword.equals(password)){
                view.setWarning("Login Successful");
                view.setToast("Login Successful");

                if (model.isAdmin(snapshot, username)){
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
