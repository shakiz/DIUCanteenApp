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

public class DrinksFragment extends Fragment {

    private RecyclerView recyclerViewDrinksItem;
    private RecyclerViewItemDetailsAdapter recyclerViewItemDetailsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<FoodItemModel> foodItemModelArrayList;
    private StoreFoodItemData storeFoodItemData;
    private Bundle userEmailBundle;
    private String userEmailStr;

    public DrinksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drinks, container, false);
        //This method will be used to initialize all the attributes with xml
        init(view);
        //This method will be used to get bundle data from previous activity
        getBundleData();
        //This method will be used to get all the drinks category data from database
        getDrinksData();
        //This method will be used to set the adapter for recyclerView
        setAdapter();
        return view;
    }

    private void setAdapter() {
        recyclerViewItemDetailsAdapter=new RecyclerViewItemDetailsAdapter(foodItemModelArrayList,getContext(),userEmailStr);
        recyclerViewDrinksItem.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDrinksItem.setLayoutManager(layoutManager);
        recyclerViewDrinksItem.setAdapter(recyclerViewItemDetailsAdapter);
    }

    public void getBundleData(){
        userEmailBundle=getArguments();
        userEmailStr=userEmailBundle.getString("email");
        Log.v("Email :::",userEmailStr);
    }

    private void getDrinksData() {
        foodItemModelArrayList=storeFoodItemData.getItemListBasedOnCategory("Drinks");
    }

    private void init(View view) {
        recyclerViewDrinksItem=view.findViewById(R.id.recyclerViewItemDrinksXML);
        layoutManager=new LinearLayoutManager(getContext());
        foodItemModelArrayList=new ArrayList<>();
        storeFoodItemData=new StoreFoodItemData(getContext());
    }

}
