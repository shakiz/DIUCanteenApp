package app.com.diucanteenapp.Student.Ac.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import app.com.diucanteenapp.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);
        //This method will be used to initialize all the attributes with xml and takes view as the parameter
        init();

        //This method will be used to get email of the logged on admin
        getIntentData();

        //Setting the on click listener for add cart details
        addCartDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoodCategoryActivity.this, AddToCartActivity.class).putExtra("email",email));
            }
        });
        //Setting up the viewpager for three different fragments
        setupViewPager(viewPager);
        //Setting the viewpager with tablayout
        tabLayout.setupWithViewPager(viewPager);
    }

    public void init(){
        viewPager=findViewById(R.id.viewPagerXML);
        tabLayout=findViewById(R.id.tabXML);
        addCartDetails=findViewById(R.id.addToCartXML);
    }

    public void getIntentData(){
        Intent intent=getIntent();
        email=intent.getStringExtra("email");
        Log.v("Email : ",email);
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
}
