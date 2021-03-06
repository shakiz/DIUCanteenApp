package app.com.diucanteenapp.activities.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import app.com.diucanteenapp.fragments.admin.AddNewItemFragment;
import app.com.diucanteenapp.fragments.admin.OrderListFragment;
import app.com.diucanteenapp.fragments.admin.UserRequestApprovalFragment;
import app.com.diucanteenapp.fragments.admin.ViewAllProductFragment;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.activities.onboard.LoginActivity;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String email;
    private SharedPreferences userDetailsSharedPref;
    private SharedPreferences.Editor userDetailsSharedPredEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentScreen,new AddNewItemFragment());
        fragmentTransaction.commit();
        //This method will be used to initialize all the attributes with xml
        init();
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void init(){
        userDetailsSharedPref=getSharedPreferences("user_details",MODE_PRIVATE);
        userDetailsSharedPredEditor=userDetailsSharedPref.edit();
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        email=userDetailsSharedPref.getString("email",null);
        Log.v("Email : ",email);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle != null && toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handling navigation view item clicks based on their respective ids.
        int id = item.getItemId();
        Fragment fragment=null;
        Bundle bundle=new Bundle();
        bundle.putString("email",email);

        if (id == R.id.nav_add_item) {
            fragment=new AddNewItemFragment();
            fragment.setArguments(bundle);
        }
        else if (id == R.id.nav_view_item) {
            fragment=new ViewAllProductFragment();
        }
        else if (id == R.id.nav_order_list) {
            fragment=new OrderListFragment();
        }
        else if (id==R.id.nav_notification){
            fragment= new UserRequestApprovalFragment();
        }
        else if (id == R.id.nav_sign_out){
            userDetailsSharedPredEditor.clear();
            userDetailsSharedPredEditor.commit();
            startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
        }
        if (fragment!=null){
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            fragmentTransaction.replace(R.id.fragmentScreen,fragment);
            fragmentTransaction.commit();
        }
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            exitApp();
        }
    }

    public void exitApp(){
        Intent exitIntent = new Intent(Intent.ACTION_MAIN);
        exitIntent.addCategory(Intent.CATEGORY_HOME);
        exitIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(exitIntent);
    }

}
