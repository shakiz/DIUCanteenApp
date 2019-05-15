package app.com.diucanteenapp.Student.Ac.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.SharedModel.FoodItemModel;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.Student.Ac.Adapter.RecyclerViewItemDetailsAdapter;


public class LunchFragment extends Fragment {

    private RecyclerView recyclerViewLunchItem;
    private RecyclerViewItemDetailsAdapter recyclerViewItemDetailsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FoodItemModel> foodItemModelArrayList;
    private StoreFoodItemData storeFoodItemData;
    private Bundle userEmailBundle;
    private String userEmailStr;
    private String TAG="LunchFragment";

    public LunchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lunch, container, false);

        //This method will be used to initialize all the attributes with xml
        init(view);
        //This method will be used to get bundle data from previous activity
        getBundleData();
        //This method will be used to get all the drinks category data from database
        getEveningSnacksData();
        //This method will be used to set the adapter for recyclerView
        setAdapter();
        return view;
    }

    public void getBundleData(){
        userEmailBundle=getArguments();
        userEmailStr=userEmailBundle.getString("email");
        try{
            Log.v("Email :::",userEmailStr);
        }
        catch (Exception e){
            Log.v(TAG,e.getMessage());
        }
    }

    private void setAdapter() {
        recyclerViewItemDetailsAdapter=new RecyclerViewItemDetailsAdapter(foodItemModelArrayList,getContext(),userEmailStr);
        recyclerViewLunchItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewLunchItem.setLayoutManager(layoutManager);
        recyclerViewLunchItem.setAdapter(recyclerViewItemDetailsAdapter);
    }

    private void getEveningSnacksData() {
        foodItemModelArrayList=storeFoodItemData.getItemListBasedOnCategory("Lunch");
    }

    private void init(View view) {
        recyclerViewLunchItem =view.findViewById(R.id.recyclerViewItemLunchXML);
        layoutManager=new LinearLayoutManager(getContext());
        foodItemModelArrayList=new ArrayList<>();
        storeFoodItemData=new StoreFoodItemData(getContext());
    }


}
