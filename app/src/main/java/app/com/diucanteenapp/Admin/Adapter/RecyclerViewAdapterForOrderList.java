package app.com.diucanteenapp.Admin.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedModel.OrderItemModel;

public class RecyclerViewAdapterForOrderList extends RecyclerView.Adapter<RecyclerViewAdapterForOrderList.ViewHolder> {

    private Context context;
    private ArrayList<OrderItemModel> orderItemModels;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.itemview_for_order_list_recycler_view,viewGroup,false);
        return new ViewHolder(view);
}

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final OrderItemModel orderItemModel=orderItemModels.get(i);
        viewHolder.orderItem.setText("Item name : "+orderItemModel.getItemName());
        viewHolder.orderUserEmail.setText(orderItemModel.getEmail());
        viewHolder.orderDate.setText("Order date : "+orderItemModel.getDate());
        viewHolder.orderQuantity.setText("Quantity : "+orderItemModel.getQuantity());
    }

    public RecyclerViewAdapterForOrderList(ArrayList<OrderItemModel> orderItemModels, Context context){
        this.orderItemModels=orderItemModels;
        this.context=context;
    }
    @Override
    public int getItemCount() {
        return orderItemModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderItem,orderUserEmail,orderQuantity,orderDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderItem=itemView.findViewById(R.id.orderItemNameXML);
            orderUserEmail=itemView.findViewById(R.id.orderUserEmailXML);
            orderDate=itemView.findViewById(R.id.orderDateItemXML);
            orderQuantity=itemView.findViewById(R.id.orderQuantityItemXML);
        }
    }
}
