package app.com.diucanteenapp.Admin.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.diucanteenapp.Admin.Adapter.RecyclerViewAdapterForUserRequests;
import app.com.diucanteenapp.Admin.Adapter.RecyclerViewItemDetailsAdapter;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedDatabaseClasses.DatabaseHelperLoginAndRegistration;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserRequestApprovalFragment extends Fragment {

    private static String TAG = "UserRequestApproval";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerViewAdapterForUserRequests recyclerViewAdapterForUserRequestsAdapter;
    private DatabaseHelperLoginAndRegistration databaseHelperLoginAndRegistration;
    private ArrayList<String> userArrayList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout linearLayout;
    private Context context;

    public UserRequestApprovalFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user_request_approval, container, false);
        //This method will be used to initialize all the attributes with XML
        init(view);
        //Here we need to fetch the users who has the status as "Not Approve"
        getUserData();
        if (userArrayList.size()>0){
            //Now we need to set the adapter which will be used to attach all the data into the recyclerView
            setAdapter();
        }
        else {
            Toast.makeText(context,"No user requests to approve",Toast.LENGTH_LONG).show();
        }
        //Setting the swipe listener when the user swipe to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserData();
                setAdapter();
                swipeRefreshLayout.setRefreshing(false);
                for (int start=0;start<databaseHelperLoginAndRegistration.getAllUserName().size();start++){
                    Log.v(TAG,databaseHelperLoginAndRegistration.getAllUserName().get(start));
                }
            }
        });
        return view;
    }

    private void getUserData() {
        userArrayList=databaseHelperLoginAndRegistration.getAllUserStatus("Not Approve");
    }

    private void setAdapter() {
        recyclerViewAdapterForUserRequestsAdapter =new RecyclerViewAdapterForUserRequests(userArrayList,getContext(),linearLayout);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapterForUserRequestsAdapter);
    }

    private void init(View view) {
        recyclerView=view.findViewById(R.id.recyclerViewUserRequestsXML);
        userArrayList=new ArrayList<>();
        linearLayout=view.findViewById(R.id.linearLayoutUserStatus);
        swipeRefreshLayout=view.findViewById(R.id.swipeRefreshLayoutApproveUser);
        databaseHelperLoginAndRegistration=new DatabaseHelperLoginAndRegistration(getActivity());
        layoutManager=new LinearLayoutManager(context);
    }

}
