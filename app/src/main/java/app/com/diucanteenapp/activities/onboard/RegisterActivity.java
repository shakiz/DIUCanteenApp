package app.com.diucanteenapp.activities.onboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import app.com.diucanteenapp.utils.dbhelper.DatabaseHelperLoginAndRegistration;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.model.shared.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText email,password,mobileNumber,username,confirmPassword;
    private Button registerButton;
    private String emailStr,usernameStr,passwordStr,confirmPasswordStr,mobileNumberStr;
    private DatabaseHelperLoginAndRegistration databaseHelperLoginAndRegistration;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //This method will be used to initialize all the attributes with xml
        init();
        //Setting the on click listener for register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Storing the user inputs into variables
                emailStr=email.getText().toString();
                passwordStr=password.getText().toString();
                usernameStr=username.getText().toString();
                confirmPasswordStr=confirmPassword.getText().toString();
                mobileNumberStr=(mobileNumber.getText().toString());
                //This method will be used to validate inputs
                if (inputValidation(emailStr,passwordStr,usernameStr,confirmPasswordStr,mobileNumberStr)){
                    //This method will be used to check whether both the password filed match or not
                    if (checkPassword()){
                        //Here we are checking that if the registering user already exists in our database or not
                        if (databaseHelperLoginAndRegistration.checkUserExistance(emailStr)){
                            Toast.makeText(getApplicationContext(),"User already exists!!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //Adding or setting the data into user details
                            user.setEmail(emailStr);
                            user.setName(usernameStr);
                            user.setPassword(passwordStr);
                            user.setMobileNumber(mobileNumberStr);
                            user.setUserType("Student");
                            user.setUserStatus("Not Approve");
                            //Adding or inserting user details in our database as a new record
                            databaseHelperLoginAndRegistration.addUser(user);
                            databaseHelperLoginAndRegistration.addAdmin(user);
                            Toast.makeText(getApplicationContext(),"User registered successfully.",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        }
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Password not matched",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private boolean checkPassword() {
        passwordStr=password.getText().toString();
        confirmPasswordStr=confirmPassword.getText().toString();
        if (passwordStr.equals(confirmPasswordStr)){
            return true;
        }
        else{
            Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean inputValidation(String emailStr,String passwordStr,String usernameStr,String confirmPasswordStr,String mobileNumberStr) {
        boolean valid = false;
        if (usernameStr.isEmpty()){
            username.requestFocus();
            username.setError("Username can not be empty.");
        }
        else if (emailStr.isEmpty()){
            email.requestFocus();
            email.setError("Email can not be empty.");
        }
        else if (passwordStr.isEmpty()){
            password.requestFocus();
            password.setError("Password can not be empty.");
        }
        else if (confirmPasswordStr.isEmpty()){
            confirmPassword.requestFocus();
            confirmPassword.setError("Confirm Password can not be empty.");
        }
        else if (mobileNumberStr.isEmpty()){
            mobileNumber.requestFocus();
            mobileNumber.setError("Mobile Number can not be empty.");
        }
        else if(!usernameStr.isEmpty() && !emailStr.isEmpty() && !passwordStr.isEmpty() && !confirmPasswordStr.isEmpty() && !mobileNumberStr.isEmpty()){
            valid = true;
        }
        return valid;
    }

    private void init() {
        email=findViewById(R.id.emailXML);
        username=findViewById(R.id.usernameXML);
        mobileNumber=findViewById(R.id.mobileNumberXML);
        password=findViewById(R.id.passwordXML);
        confirmPassword=findViewById(R.id.confirmPasswordXML);
        registerButton=findViewById(R.id.registerButtonXML);
        databaseHelperLoginAndRegistration =new DatabaseHelperLoginAndRegistration(getApplicationContext());
        user=new User();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
    }
}
