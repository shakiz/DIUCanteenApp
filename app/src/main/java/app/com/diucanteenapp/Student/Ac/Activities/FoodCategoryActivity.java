package app.com.diucanteenapp.Student.Ac.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedActivities.LoginActivity;
import app.com.diucanteenapp.Student.Ac.Fragments.DrinksFragment;
import app.com.diucanteenapp.Student.Ac.Fragments.EveningSnacksFragment;
import app.com.diucanteenapp.Student.Ac.Fragments.LunchFragment;
import app.com.diucanteenapp.Student.Ac.Fragments.MorningSnacksFragment;

public class FoodCategoryActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView addCartDetails;
    private String email;
    private Bundle userEmailBundle;
    private String TAG="FoodCategoryActivity";
    private SharedPreferences userDetailsSharedPref;
    private SharedPreferences.Editor userDetailsSharedPredEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        //This method will be used to initialize all the attributes with xml and takes view as the parameter
        init();

        //Setting the on click listener for add cart details
        addCartDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodCategoryActivity.this, AddToCartActivity.class).putExtra("email",email));
            }
        });
        //Setting up the viewpager for three different fragments
        setupViewPager(viewPager);
        //Setting the viewpager with tabLayout
        tabLayout.setupWithViewPager(viewPager);
    }

    public void init(){
        viewPager=findViewById(R.id.viewPagerXML);
        tabLayout=findViewById(R.id.tabXML);
        addCartDetails=findViewById(R.id.addToCartXML);
        userDetailsSharedPref=getSharedPreferences("user_details",MODE_PRIVATE);
        userDetailsSharedPredEditor=userDetailsSharedPref.edit();

        email=userDetailsSharedPref.getString("email",null);
        try{
            Log.v("Email : ",email);
        }
        catch (Exception e){
            Log.v(TAG,""+e.getMessage());
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        userEmailBundle =new Bundle();
        userEmailBundle.putString("email",email);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Creating the instance of morning snacks fragment and setting arguments in it
        MorningSnacksFragment morningSnacksFragment=new MorningSnacksFragment();
        morningSnacksFragment.setArguments(userEmailBundle);
        //Creating the instance of drinks fragment and setting arguments in it
        DrinksFragment drinksFragment=new DrinksFragment();
        drinksFragment.setArguments(userEmailBundle);
        //Creating the instance of lunch fragment and setting arguments in it
        LunchFragment lunchFragment=new LunchFragment();
        lunchFragment.setArguments(userEmailBundle);
        //Creating the instance of evening snacks fragment and setting arguments in it
        EveningSnacksFragment eveningSnacksFragment=new EveningSnacksFragment();
        eveningSnacksFragment.setArguments(userEmailBundle);
        //Finally adding each fragment one by one and their respective titles
        adapter.addFragment(morningSnacksFragment, "Morning Snacks");
        adapter.addFragment(drinksFragment, "Drinks");
        adapter.addFragment(lunchFragment, "Lunch");
        adapter.addFragment(eveningSnacksFragment, "Evening Snacks");
        viewPager.setAdapter(adapter);
    }
    //This class will act as the blueprint or the adapter for fragments
    //Because we have three different fragments to be visible into viewpager
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //This will create the option menu on the top of the activity
        getMenuInflater().inflate(R.menu.student_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Here we are taking the id and passing it to switch in order to perform several actions based on the id
        int id=item.getItemId();
        switch (id){
            case R.id.menu_signOutStu:
                userDetailsSharedPredEditor.clear();
                userDetailsSharedPredEditor.commit();
                Toast.makeText(getApplicationContext(),"Signing out",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FoodCategoryActivity.this, LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
