package app.com.diucanteenapp.Student.Ac.Activities;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.diucanteenapp.R;
import app.com.diucanteenapp.Student.Ac.Adapter.RecyclerViewAdapterForCartItem;
import app.com.diucanteenapp.Student.Ac.DatabaseHelper.DatabaseHelperSaveCartDetails;
import app.com.diucanteenapp.Student.Ac.Model.CartModel;

public class AddToCartActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAddToCart;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;
    private ArrayList<CartModel> cartModelArrayList;
    private RecyclerViewAdapterForCartItem recyclerViewAdapterForCartItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String userEmailStr;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        //This method will be used to initialize all the attributes with xml
        init();
        //This method will be used to get intent data of email
        getIntentDataEmail();
        //Here we are populating our arrayList of cart item from database
        cartModelArrayList=databaseHelperSaveCartDetails.getAllFoodCartItems();
        //Here we are checking whether there are any carted item or not
        if (cartModelArrayList.size()>0){
            //This method will be used to set the adapter for cart recyclerView
            setAdapter();
        }
        else{
            Snackbar.make(linearLayout,"You do not have any cart item",Snackbar.LENGTH_LONG).show();
        }
        //Here we are implementing swipe to refresh option to our activity
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(),"Please swipe up to refresh",Toast.LENGTH_SHORT).show();
                cartModelArrayList=databaseHelperSaveCartDetails.getAllFoodCartItems();
                setAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getIntentDataEmail() {
        userEmailStr= getIntent().getStringExtra("email");
        Log.v("EMAIL :> ",userEmailStr);
    }

    private void setAdapter() {
        recyclerViewAdapterForCartItem=new RecyclerViewAdapterForCartItem(cartModelArrayList,getApplicationContext(),userEmailStr);
        recyclerViewAddToCart.setLayoutManager(layoutManager);
        recyclerViewAddToCart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAddToCart.setAdapter(recyclerViewAdapterForCartItem);
    }

    private void init() {
        recyclerViewAddToCart=findViewById(R.id.cartRecyclerViewXML);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        databaseHelperSaveCartDetails=new DatabaseHelperSaveCartDetails(getApplicationContext());
        cartModelArrayList=new ArrayList<>();
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        linearLayout=findViewById(R.id.linearLayoutCartXML);
    }
}
