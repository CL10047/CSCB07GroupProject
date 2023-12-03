package com.example.cscb07groupproject;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.view.View;

import com.google.firebase.database.DataSnapshot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {

    @Mock
    LoginModel model;
    @Mock
    LoginView view;
    @Mock
    DataSnapshot snapshot;
    @Mock
    View baseView;




    @Test
    public void checkConstructor(){
        LoginPresenter presenter = new LoginPresenter();
        assertNotNull(presenter);
    }
    @Test
    public void checkEmptyUsername(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkDB("", "k");
        verify(view).setWarning("Enter username");
    }

    @Test
    public void checkEmptyPassword(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkDB("k", "");
        verify(view).setWarning("Enter password");
    }

    @Test
    public void checkFullFields(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkDB("k", "k");
        verify(model).queryDB(presenter, "k", "k");
    }

    @Test
    public void checkWrongUsername(){
        String username = "kasak";
        String password = "kasak";
     //   when(model.checkChild(snapshot, username)).thenReturn(false);

        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkCredentials(snapshot,username, password);
        verify(view).setWarning("Wrong Username");
    }

    @Test
    public void checkWrongPassword(){
        String username = "Samuel";
        String password = "kasak";
        when(model.checkChild(snapshot, username)).thenReturn(true);
        when(model.getPassword(snapshot, username)).thenReturn("12345");

        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkCredentials(snapshot,username, password);
        verify(view).setWarning("Wrong Password");
    }

    @Test
    public void checkCorrect(){
        String username = "Samuel";
        String password = "12345";
        when(model.checkChild(snapshot, username)).thenReturn(true);
        when(model.getPassword(snapshot, username)).thenReturn("12345");

        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkCredentials(snapshot,username, password);
        verify(view).setWarning("Login Successful");
    }

    @Test
    public void notAdmin(){
        String username = "Samuel";
        String password = "12345";
        when(model.checkChild(snapshot, username)).thenReturn(true);
        when(model.getPassword(snapshot, username)).thenReturn("12345");
        when(model.isAdmin(snapshot,username)).thenReturn(false);

        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkCredentials(snapshot,username, password);
        verify(view).userLogin();
    }

    @Test
    public void yesAdmin(){
        String username = "Samuel";
        String password = "12345";
        when(model.checkChild(snapshot, username)).thenReturn(true);
        when(model.getPassword(snapshot, username)).thenReturn("12345");
        when(model.isAdmin(snapshot,username)).thenReturn(true);

        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkCredentials(snapshot,username, password);
        verify(view).adminLogin();
    }

}