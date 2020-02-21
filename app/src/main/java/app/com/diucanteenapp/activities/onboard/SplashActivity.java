package app.com.diucanteenapp.activities.onboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.activities.admin.AdminHomeActivity;
import app.com.diucanteenapp.activities.student.FoodCategoryActivity;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Shakil-SplashActivity";
    private LinearLayout linearLayout;
    private SharedPreferences userDetailsSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();

        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        linearLayout.startAnimation(a);
        bindUIWithComponents();
    }

    private void initUI() {
        linearLayout = findViewById(R.id.mainLayout);
        userDetailsSharedPreferences=getSharedPreferences("user_details",MODE_PRIVATE);
    }

    private void bindUIWithComponents() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                //Here we are checking if an user is already logged on or not
                try{
                    if (userDetailsSharedPreferences.getString("type",null) == null){
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    else if (userDetailsSharedPreferences.getString("type",null).equals("Admin")){
                        startActivity(new Intent(SplashActivity.this, AdminHomeActivity.class));
                    }
                    else if (userDetailsSharedPreferences.getString("type",null).equals("Student")){
                        startActivity(new Intent(SplashActivity.this, FoodCategoryActivity.class));
                    }
                }
                catch (Exception e){
                    Log.v(TAG,e.getMessage());
                }
            }
        }, 1500);

    }
}
