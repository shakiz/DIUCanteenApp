package app.com.diucanteenapp.Admin.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

import app.com.diucanteenapp.Admin.Adapter.RecyclerViewAdapterForOrderList;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedDatabaseClasses.DatabaseHelperPlaceOrder;
import app.com.diucanteenapp.SharedModel.OrderItemModel;

public class OrderListFragment extends Fragment {

    private ArrayList<OrderItemModel> orderItemModels;
    private RecyclerView recyclerViewOrderList;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapterForOrderList recyclerViewAdapterForOrderList;
    private DatabaseHelperPlaceOrder databaseHelperPlaceOrder;
    private LinearLayout linearLayout;

    public OrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_order_list, container, false);
        //This method will be used to initialize all the attributes with xml
        init(view);
        //Getting the order lists data from database
        orderItemModels=databaseHelperPlaceOrder.getAllFoodOrders();
        //Here we are checking that if we have any order in our database or not
        if (orderItemModels.size()>0){
            //This method will be used to set the adapter
            setAdapter();
        }
        else{
            Snackbar.make(linearLayout,"No orderes are available",Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    private void setAdapter() {
        recyclerViewAdapterForOrderList=new RecyclerViewAdapterForOrderList(orderItemModels,getContext());
        recyclerViewOrderList.setItemAnimator(new DefaultItemAnimator());
        recyclerViewOrderList.setLayoutManager(layoutManager);
        recyclerViewOrderList.setAdapter(recyclerViewAdapterForOrderList);
    }

    private void init(View view) {
        recyclerViewOrderList=view.findViewById(R.id.recyclerViewOrderListXML);
        orderItemModels=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        linearLayout=view.findViewById(R.id.linearLayoutOrderListXML);
        databaseHelperPlaceOrder=new DatabaseHelperPlaceOrder(getContext());
    }

}