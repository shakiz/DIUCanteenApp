package app.com.diucanteenapp.activities.onboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import app.com.diucanteenapp.activities.admin.AdminHomeActivity;
import app.com.diucanteenapp.utils.dbhelper.DatabaseHelperLoginAndRegistration;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.activities.student.FoodCategoryActivity;

public class LoginActivity extends AppCompatActivity {

    private String TAG="LoginActivity";
    private TextView registerYourself;
    private Button loginButton;
    private EditText email,password;
    private String emailStr,passwordStr,userTypeStr;
    private Spinner userTypeSpinner;
    private ArrayList<String> spinnerDataArrayList;
    private ArrayAdapter<String> spinnerArrayAdapter;
    private DatabaseHelperLoginAndRegistration databaseHelperLoginAndRegistration;
    private SharedPreferences userDetailsSharedPreferences;
    private SharedPreferences.Editor userDetailsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //This method will be used to initialize all the attributes with xml
        init();
//        //Here we are checking if an user is already logged on or not
        try{
            if (userDetailsSharedPreferences.getString("type",null).equals("Admin")){
                startActivity(new Intent(LoginActivity.this,AdminHomeActivity.class));
            }
            else{
                startActivity(new Intent(LoginActivity.this,FoodCategoryActivity.class));
            }
        }
        catch (Exception e){
            Log.v(TAG,e.getMessage());
        }
        try{
            Log.v(TAG,""+userDetailsSharedPreferences.getString("email",null));
            Log.v(TAG,""+userDetailsSharedPreferences.getString("type",null));
        }
        catch (Exception e){
            Log.v(TAG,e.getMessage());
        }
        //This method will be used to insert data into spinnerDataArrayList
        insertData();
        //This method will be used to populate the spinner by using array adapter
        setSpinnerAdapter();
        //Setting the on item select listener for spinner user type
        userTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userTypeStr=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),"Please select user type",Toast.LENGTH_SHORT).show();
            }
        });
        //Setting the on click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Storing the user entered data into variables
                emailStr=email.getText().toString();
                passwordStr=password.getText().toString();
                //This method will be used to validate user inputs
                inputValidation(emailStr,passwordStr);
            }
        });

        //Setting the on click listener for register yourself textview
        registerYourself.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
            }
        });
    }

    private void setSpinnerAdapter() {
        // Creating adapter for spinner
        spinnerArrayAdapter=new ArrayAdapter<>(this,R.layout.spinner_drop,spinnerDataArrayList);
        // Drop down layout style - list view with radio button
        spinnerArrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        userTypeSpinner.setAdapter(spinnerArrayAdapter);
    }

    private void insertData() {
        spinnerDataArrayList.add("Select user type");
        spinnerDataArrayList.add("Student");
        spinnerDataArrayList.add("Admin");
    }

    private void inputValidation(String emailStr,String passwordStr) {

        if (emailStr.isEmpty() && passwordStr.isEmpty()){
            email.requestFocus();
            email.setError("Email can not be empty.");
        }
        else if (emailStr.isEmpty() || passwordStr.isEmpty()){
            password.requestFocus();
            password.setError("Password can not be empty.");
        }
        else{
            if (userTypeStr.isEmpty()){
                Toast.makeText(getApplicationContext(),"Please select user type",Toast.LENGTH_SHORT).show();
            }
            else if (userTypeStr.equals("Select user type")){
                Toast.makeText(getApplicationContext(),"Please select user type",Toast.LENGTH_SHORT).show();
            }
            else{
                //This method will be used to login an user based on user type
                login(emailStr,passwordStr,userTypeStr);
            }
        }
    }

    private void login(String emailStr,String passwordStr,String userTypeStr) {
        if (userTypeStr.equals("Admin")){
            //Here we are checking the email and password for admin with the admin table
            try{
                if (emailStr.equals("shakil.py@gmail.com") && passwordStr.equals("12345")){
                    Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                    //Here we are adding the user email for the purpose of session [For admin]
                    userDetailsEditor.putString("email",emailStr);
                    userDetailsEditor.putString("type",userTypeStr);
                    userDetailsEditor.commit();
                    //Starting the admin activity
                    startActivity(new Intent(LoginActivity.this,AdminHomeActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e){
                Log.v("ERROR","Admin login : "+e.getMessage());
            }
        }
        else if (userTypeStr.equals("Student")){
            if (databaseHelperLoginAndRegistration.getStudentStatus(emailStr).equals("Not Approve")){
                Toast.makeText(getApplicationContext(),"User not approved",Toast.LENGTH_SHORT).show();
            }
            else{
                //If the user type is student then go for the user table and match the email and password
                if (databaseHelperLoginAndRegistration.checkStudent(emailStr,passwordStr)){
                    Toast.makeText(getApplicationContext(),"Login successful",Toast.LENGTH_SHORT).show();
                    //Here we are adding the user email for the purpose of session [For student]
                    userDetailsEditor.putString("email",emailStr);
                    userDetailsEditor.putString("type",userTypeStr);
                    userDetailsEditor.commit();
                    //Starting the user activity for the item category
                    startActivity(new Intent(LoginActivity.this,FoodCategoryActivity.class));
                }
                else{
                    Toast.makeText(getApplicationContext(),"Login unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Log.v("USER TYPE : ","Please insert valid user type.");
        }
    }


    //This method will be used to initialize all the attributes with xml
    public void init(){
        registerYourself=findViewById(R.id.registerYourselfXML);
        loginButton=findViewById(R.id.logInButtonXML);
        email=findViewById(R.id.emailXML);
        password=findViewById(R.id.passwordXML);
        userTypeSpinner=findViewById(R.id.userTypeSpinnerXML);
        spinnerDataArrayList=new ArrayList<>();
        databaseHelperLoginAndRegistration =new DatabaseHelperLoginAndRegistration(getApplicationContext());
        //The following two lines of code will be used to start a session for the user
        userDetailsSharedPreferences=getSharedPreferences("user_details",MODE_PRIVATE);
        userDetailsEditor=userDetailsSharedPreferences.edit();
    }
}
