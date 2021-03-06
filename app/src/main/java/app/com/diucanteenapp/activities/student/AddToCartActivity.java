package app.com.diucanteenapp.activities.student;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.util.ArrayList;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.utils.adapters.RecyclerViewAdapterForCartItem;
import app.com.diucanteenapp.utils.dbhelper.DatabaseHelperSaveCartDetails;
import app.com.diucanteenapp.model.students.CartModel;

public class AddToCartActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerViewAddToCart;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;
    private ArrayList<CartModel> cartModelArrayList;
    private RecyclerViewAdapterForCartItem recyclerViewAdapterForCartItem;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String TAG="AddToCartActivity";
    private int total=0;
    private String userEmailStr;
    private LinearLayout ListItemLayout;
    private RelativeLayout NoDataLayout, mainLayout;
    private Button orderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        //This method will be used to initialize all the attributes with xml
        init();

        setSupportActionBar(toolbar);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddToCartActivity.this, FoodCategoryActivity.class));
            }
        });

        //This method will be used to get intent data of email
        getIntentDataEmail();
        //Here we are populating our arrayList of cart item from database
        cartModelArrayList=databaseHelperSaveCartDetails.getAllFoodCartItems();
        //Here we are checking whether there are any carted item or not
        if (cartModelArrayList.size()>0){
            //This method will be used to set the adapter for cart recyclerView
            setAdapter();
            ListItemLayout.setVisibility(View.VISIBLE);
            NoDataLayout.setVisibility(View.GONE);
        }
        else{
            ListItemLayout.setVisibility(View.GONE);
            NoDataLayout.setVisibility(View.VISIBLE);
            Snackbar.make(mainLayout,"You do not have any cart item",Snackbar.LENGTH_LONG).show();
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


        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int amount : recyclerViewAdapterForCartItem.singleTotal){
                    total+=amount;
                }
                startActivity(new Intent(AddToCartActivity.this,PaymentAndOrderActivity.class).putExtra("amount",total));
            }
        });
    }

    private void getIntentDataEmail() {
        userEmailStr= getIntent().getStringExtra("email");
        Log.v(TAG,"Email : "+userEmailStr);
    }

    private void setAdapter() {
        recyclerViewAdapterForCartItem=new RecyclerViewAdapterForCartItem(cartModelArrayList,AddToCartActivity.this,userEmailStr);
        recyclerViewAddToCart.setLayoutManager(layoutManager);
        recyclerViewAddToCart.setItemAnimator(new DefaultItemAnimator());
        recyclerViewAddToCart.setAdapter(recyclerViewAdapterForCartItem);
    }

    private void init() {
        toolbar = findViewById(R.id.tool_bar);
        recyclerViewAddToCart=findViewById(R.id.cartRecyclerViewXML);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        databaseHelperSaveCartDetails=new DatabaseHelperSaveCartDetails(getApplicationContext());
        cartModelArrayList=new ArrayList<>();
        swipeRefreshLayout=findViewById(R.id.swipeRefreshLayout);
        orderBtn = findViewById(R.id.OrderAllItem);
        mainLayout =findViewById(R.id.linearLayoutCartXML);
        NoDataLayout = findViewById(R.id.NoDataLayout);
        ListItemLayout = findViewById(R.id.ListLayout);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddToCartActivity.this,FoodCategoryActivity.class));
    }
}
