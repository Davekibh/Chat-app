package com.example.whatsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    Boolean loginModeActive = false;

    public void redirectIfLoggedIn(){

        if (ParseUser.getCurrentUser() != null){

            Intent intent = new Intent(getApplicationContext(), UserListActivity.class);

            startActivity(intent);

            finish();

        }

    }

    public void toggleLoginMode(View view){

        Button loginSignupButton = findViewById(R.id.loginSignupButton);

        TextView toggleLoginModeTextView = findViewById(R.id.toggleLoginModeTextView);

        if (loginModeActive){

            loginModeActive = false;
            loginSignupButton.setText("Sign Up");
            toggleLoginModeTextView.setText("Or, Login");

        }else{

            loginModeActive = true;
            loginSignupButton.setText("Login");
            toggleLoginModeTextView.setText("Or, Sign Up");

        }

    }

    public void signupLogin(View view){

        EditText usernameEditText = findViewById(R.id.usernameEditText);

        EditText passwordEditText = findViewById(R.id.passwordEditText);

        if (loginModeActive){

            ParseUser.logInInBackground(usernameEditText.getText().toString(), passwordEditText.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {

                    if (e == null){

                        Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        redirectIfLoggedIn();

                        finish();

                    }else{

                        String message = e.getMessage();

                        if (message.toLowerCase().contains("java")){

                            message = e.getMessage().substring(e.getMessage().indexOf(" "));

                        }

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }else {

            ParseUser user = new ParseUser();

            user.setUsername(usernameEditText.getText().toString());

            user.setPassword(passwordEditText.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        Toast.makeText(MainActivity.this, "SignUp Successful!", Toast.LENGTH_SHORT).show();

                        redirectIfLoggedIn();

                        finish();

                    } else {

                        String message = e.getMessage();

                        if (message.toLowerCase().contains("java")){

                            message = e.getMessage().substring(e.getMessage().indexOf(" "));

                        }

                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        redirectIfLoggedIn();

        // Save the current Installation to Back4App
        //ParseInstallation.getCurrentInstallation().saveInBackground();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }}