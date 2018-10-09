package com.example.wijen.training;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    private static EditText username;
    private static EditText password;
    private static Button login_button;
    public static String testing = "Test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login();
}
    public void login() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login_button = (Button) findViewById(R.id.login_btn);

        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (username.getText().toString().equals("user") && password.getText().toString().equals("pass")) {
                            Toast.makeText(Login.this, "Username and password are correct", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(Login.this, Home.class));
//                            Intent intent = new Intent("com.wijen.loginapp.Home");
//                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(Login.this, "Username or password is wrong",Toast.LENGTH_SHORT).show();
                        }

                    }
                }


        );

    }

    }