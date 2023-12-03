package com.example.cscb07groupproject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

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


    @Test
    public void checkEmptyUsername(){
        LoginPresenter presenter = new LoginPresenter(view, model);
        presenter.checkDB("", "k");
        verify(view).setWarning("Enter username");
    }

}