package app.com.diucanteenapp.Admin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedDatabaseClasses.DatabaseHelperLoginAndRegistration;

public class RecyclerViewAdapterForUserRequests extends RecyclerView.Adapter<RecyclerViewAdapterForUserRequests.ViewHolder>{
    private ArrayList<String> userArrayList;
    private DatabaseHelperLoginAndRegistration databaseHelperLoginAndRegistration;
    private Context context;
    private LinearLayout linearLayout;

    public RecyclerViewAdapterForUserRequests(ArrayList<String> userArrayList, Context context, LinearLayout linearLayout){
        this.userArrayList=userArrayList;
        this.context=context;
        this.linearLayout=linearLayout;
        databaseHelperLoginAndRegistration=new DatabaseHelperLoginAndRegistration(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view_for_user_requests,viewGroup,false);
        return new RecyclerViewAdapterForUserRequests.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int position=i;
        viewHolder.userName.setText("Want to add user "+userArrayList.get(i)+" ?");
        viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = databaseHelperLoginAndRegistration.getUserName(userArrayList.get(position));
                Log.v("Username: ",username);
                databaseHelperLoginAndRegistration.updateUserStatus(userArrayList.get(position),"Approve");
                Toast.makeText(context,"User status updated",Toast.LENGTH_LONG).show();
                Snackbar.make(linearLayout,"Please refresh",Snackbar.LENGTH_LONG).show();
            }
        });
        viewHolder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelperLoginAndRegistration.deleteUser(userArrayList.get(position));
                Snackbar.make(linearLayout,"Please refresh",Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView userName;
        Button addButton,removeButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userNameToApproveXML);
            addButton=itemView.findViewById(R.id.addUserButtonXML);
            removeButton=itemView.findViewById(R.id.removeUserButtonXML);
        }
    }
}
