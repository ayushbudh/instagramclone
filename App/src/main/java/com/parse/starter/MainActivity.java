
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

  TextView password, username, login;
  Button signupbtn;
  RelativeLayout bgLayout;
  ImageView logoimg;
  boolean signupModeActive  = true;

  public void showUserList(){
    Intent intent = new Intent(getApplicationContext(), UserListActivity.class);
    startActivity(intent);
  }
  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {

    if(i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
      signup(view);
    }
    return false;
  }

  @Override
  public void onClick(View view) {
    if(view.getId() == R.id.logintxt) {
      if (signupModeActive) {
        signupModeActive = false;
        signupbtn.setText("Login");
        login.setText("Or, Sign Up");
      } else {
        signupModeActive = true;
        signupbtn.setText("Sign Up");
        login.setText("Or, Login");
      }
    }
    // user is trying to hide keyboard
    else if(view.getId() == R.id.logoimg || view.getId() == R.id.backgoundLayout){
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
    }
  }


    public void signup(View view){

      if(username.getText().toString().matches("") || password.getText().toString().matches("")){
        Toast.makeText(this, "A username and a password are required. ", Toast.LENGTH_SHORT).show();
      }else {
        if(signupModeActive) {
          // Sign Up situation
          ParseUser user = new ParseUser();
          user.setUsername(username.getText().toString());
          user.setPassword(password.getText().toString());

          user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
              if (e == null) {
                Log.i("Success!", "Signed Up");
                // new activity
                showUserList();
              } else {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
          });
        }else{
          // Login situation
          ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
              if(user!=null){
                Log.i("Login","Successful!");
                showUserList();
              }else{
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
              }
            }
          });
        }
      }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("Instagram");
    // so that clicking login changed to sign up
    TextView loginTextView = findViewById(R.id.logintxt);
    loginTextView.setOnClickListener(this);

    password = findViewById(R.id.passwordtxt);
    username = findViewById(R.id.usernametxt);
    signupbtn = findViewById(R.id.signupbtn);
    login = findViewById(R.id.logintxt);
    bgLayout = findViewById(R.id.backgoundLayout);
    logoimg = findViewById(R.id.logoimg);

    // sp that keyboard is hidden after clicking on log or white space
    logoimg.setOnClickListener(this);
    bgLayout.setOnClickListener(this);

    // for key event
    password.setOnClickListener(this);

    if(ParseUser.getCurrentUser()!= null){
      showUserList();
    }


    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}