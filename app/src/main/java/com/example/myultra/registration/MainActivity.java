package com.example.myultra.registration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

import android.content.Intent;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


public class MainActivity extends ActionBarActivity {

    // TWITTER LOGIN KEYS, HERE THE KEYS ARE OF MY ACCOUNT , REPLACE IT WITH YOURS 
    private static final String TWITTER_KEY = "Aym8wPz3KUzyF3xv6o2O37ZWc";
    private static final String TWITTER_SECRET = "OgQzuorwIFphoHwMMHfrrWakvpwzdoUQvjXT8IzkM0aPH9rpDm";
    private TwitterLoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      // TWITTER INSTANCES , AS DESCRIBED IN FABRIC   
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_main);

 //INITIALIZATION OF BUTTONS
        Button button;
        final EditText edit_name, edit_pass, edit_email, edit_phone;
        final CheckBox check;
        SharedPreferences pref;
        final SharedPreferences.Editor editor;





//INSTANCES
        button = (Button) findViewById(R.id.button);
        edit_name = (EditText) findViewById(R.id.edit_name);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        edit_email = (EditText) findViewById(R.id.edit_email);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        check = (CheckBox) MainActivity.this.findViewById(R.id.checkBox);
        pref = getSharedPreferences("Registration", 0);
        editor = pref.edit();

// ON THE CLICK OF TWITTER BUTTON
        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            
            //IF RESULT IS SUCCESSFUL
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Toast.makeText(getApplicationContext(),"LOGIN SUCCESSFUL ",Toast.LENGTH_SHORT).show();

                Intent i = new Intent(MainActivity.this,upload_photo.class);
                startActivity(i);
            }

            //IF RESULT IS UNSUCCESSFUL
            @Override
            public void failure(TwitterException exception) {
                // Do something on failure

                Toast.makeText(getApplicationContext(),"Back to Login ",Toast.LENGTH_SHORT).show();
            }
        });




    //CHECKBOX FUNTION FOR PASSWORD TO SHOW 

    check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(!check.isChecked())
            {
                edit_pass.setTransformationMethod(new PasswordTransformationMethod());
            }
            else
            {
                edit_pass.setTransformationMethod(null);
            }
        }
    });



    //ON CLICK OF BUTTON , ALL THESE THINGS WILL BE CHECKED , IF THEY ARE VALID OR NOT 

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            boolean flag =true;
            String name=edit_name.getText().toString();
            String email=edit_email.getText().toString();
            String pass=edit_pass.getText().toString();
            String phone=edit_phone.getText().toString();


            if(!isValidEmail(email))
            {
                edit_email.setError("Invalid Email");
                flag=false;
            }
            if(!isValidPassword(pass))
            {
                edit_pass.setError("Invalid Password");
                flag=false;
            }
            if (!isValidPhone(phone))
            {
                edit_phone.setError("Invalid phone");
                flag=false;
            }



            if(flag==true)
            {
                Toast.makeText(getApplicationContext(), "LOGIN SUCCESSFUL", Toast.LENGTH_SHORT).show();
                editor.putString("Name", name);
                editor.putString("Email", email);
                editor.putString("phone",phone);
                editor.putString("password",pass);
                editor.commit();
                Intent i = new Intent(MainActivity.this,upload_photo.class);
                startActivity(i);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"LOGIN UNSUCCESSFUL",Toast.LENGTH_SHORT).show();
            }
        }
    });

}


    //FUNCTION TO CHECK IF EMAIL ID IS VALID OR NOT 

    private boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern=Pattern.compile(EMAIL_PATTERN);
        Matcher matcher=pattern.matcher(email);
        return matcher.matches();
    }

 //FUNCTION TO CHECK IF PASSWORD IS VALID OR NOT 
    private boolean isValidPassword(String password)
    {
        if(password!=null && password.length()>6)
        {
            return true;
        }
        else
            return false;
    }
    
     //FUNCTION TO CHECK IF PHONE NUMBER IS VALID OR NOT 
    private boolean isValidPhone(String phone)
    {
        if (phone.length()==10)
        {
            return true;
        }

        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
