package app.com.diucanteenapp.fragments.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import app.com.diucanteenapp.utils.dbhelper.StoreFoodItemData;
import app.com.diucanteenapp.model.shared.FoodItemModel;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.utils.adapters.RecyclerViewItemDetailsAdapter;

public class ViewAllProductFragment extends Fragment {

    private RecyclerView recyclerViewItemDetails;
    private ArrayList<FoodItemModel> foodItemModelArrayList;
    private RecyclerViewItemDetailsAdapter recyclerViewItemDetailsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private StoreFoodItemData storeFoodItemData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private TextView noData;

    public ViewAllProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_all_product, container, false);
        //This method will be used to initialize all the attributes with xml
        init(view);
        //This method will be used to get all the available food item list from our database
        getAllFoodItemData();
        //This method will be used to set recyclerview adapter
        if (foodItemModelArrayList != null) {
            if (foodItemModelArrayList.size() !=0 ){
                setAdapter();
            }
        }
        else {
            noData.setVisibility(View.VISIBLE);
        }
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Whenever the admin delete any item the adapter will get refreshed with new data
                getAllFoodItemData();
                setAdapter();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void getAllFoodItemData() {
        foodItemModelArrayList=storeFoodItemData.getAllFoodItems();
    }

    private void setAdapter() {
        recyclerViewItemDetailsAdapter=new RecyclerViewItemDetailsAdapter(foodItemModelArrayList,getContext(),linearLayout);
        recyclerViewItemDetails.setLayoutManager(layoutManager);
        recyclerViewItemDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewItemDetails.setAdapter(recyclerViewItemDetailsAdapter);
    }

    private void init(View view) {
        recyclerViewItemDetails = view.findViewById(R.id.recyclerViewAllItemForAdminXML);
        noData = view.findViewById(R.id.NoData);
        foodItemModelArrayList=new ArrayList<>();
        layoutManager=new LinearLayoutManager(getContext());
        storeFoodItemData=new StoreFoodItemData(getContext());
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayoutAllItemXML);
        linearLayout=view.findViewById(R.id.linearLayoutAllItemXML);
    }

}
