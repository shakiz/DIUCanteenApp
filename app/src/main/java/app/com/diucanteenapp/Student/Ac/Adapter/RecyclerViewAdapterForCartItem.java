package app.com.diucanteenapp.Student.Ac.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.SharedDatabaseClasses.DatabaseHelperPlaceOrder;
import app.com.diucanteenapp.SharedModel.OrderItemModel;
import app.com.diucanteenapp.Student.Ac.Activities.PaymentAndOrderActivity;
import app.com.diucanteenapp.Student.Ac.DatabaseHelper.DatabaseHelperSaveCartDetails;
import app.com.diucanteenapp.Student.Ac.Model.CartModel;

public class RecyclerViewAdapterForCartItem extends RecyclerView.Adapter<RecyclerViewAdapterForCartItem.ViewHolder> {
    private String TAG = "CartItemAdapterOrder";
    private ArrayList<CartModel> cartModelArrayList;
    private StoreFoodItemData storeFoodItemData;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;
    private Context context;
    private double price=0;
    private int itemQuantity=0,itemStock=0,totalAmount=0;
    private int[] singleTotal;
    private int[] singleQuantity;
    private String userEmailStr;
    private OrderItemModel orderItemModel;
    private DatabaseHelperPlaceOrder databaseHelperPlaceOrder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view_for_cart_item_recyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    //This is our constructor which we use to instantiate out components
    public RecyclerViewAdapterForCartItem(ArrayList<CartModel> cartModelArrayList,Context context,String userEmailStr){
        this.cartModelArrayList=cartModelArrayList;
        this.context=context;
        this.userEmailStr=userEmailStr;
        storeFoodItemData=new StoreFoodItemData(context);
        databaseHelperSaveCartDetails=new DatabaseHelperSaveCartDetails(context);
        databaseHelperPlaceOrder=new DatabaseHelperPlaceOrder(context);
        singleTotal = new int[cartModelArrayList.size()];
        singleQuantity = new int[cartModelArrayList.size()];
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final CartModel cartModel=cartModelArrayList.get(position);

        itemStock=storeFoodItemData.getItemStockBasedOnName(cartModel.getItemName());
        price=storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName());
        viewHolder.itemName.setText(cartModel.getItemName());
        viewHolder.itemPrice.setText(price+""+" Tk.");
        Log.v(TAG,"STOCK : > "+itemStock);
        Log.v(TAG,"PRICE  : > "+price);
        viewHolder.itemStock.setText(""+itemStock);


        viewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (singleQuantity[position]<storeFoodItemData.getItemStockBasedOnName(cartModel.getItemName())){
                    itemQuantity=++itemQuantity;
                    singleQuantity[position]++;
                    Log.v(TAG,"SINGLE QUAN : "+singleQuantity[position]);
                    viewHolder.itemQuantity.setText(""+singleQuantity[position]);

                    singleTotal[position]= (int) (singleQuantity[position]*storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName()));
                    Log.v(TAG,"SINGLE : "+singleTotal[position]);
                    viewHolder.totalAmount.setText("Total amount : "+singleTotal[position]+" Tk.");

                }
                else{
                    Toast.makeText(context,"Stock is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singleQuantity[position]>0){
                    singleQuantity[position]--;
                    singleTotal[position]= (int) (singleQuantity[position]*storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName()));
                    viewHolder.itemQuantity.setText(""+singleQuantity[position]);
                    viewHolder.totalAmount.setText("Total Amount : "+singleTotal[position]+" Tk.");
                }
                else if (singleQuantity[position]<=0){
                    Toast.makeText(context,"Quantity can not be zero.",Toast.LENGTH_SHORT).show();
                }
            }
        });


        viewHolder.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (singleQuantity[position]>0){
                    orderItemModel=new OrderItemModel();
                    orderItemModel.setEmail(userEmailStr);
                    orderItemModel.setQuantity(itemQuantity);
                    orderItemModel.setItemName(cartModel.getItemName());
                    orderItemModel.setDate(getDate());
                    orderItemModel.setAmount(totalAmount);
                    databaseHelperPlaceOrder.addOrder(orderItemModel);
                    Toast.makeText(context,"Item added",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context,"Please check quantity",Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.deleteIemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Please swipe up to refresh",Toast.LENGTH_SHORT).show();
                databaseHelperSaveCartDetails.deleteRow(cartModel.getItemName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemName,itemPrice,itemQuantity,increaseQuantity,decreaseQuantity,totalAmount,itemStock;
        Button placeOrderButton,deleteIemButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //Here we are initiating all the attributes with xml
            itemName=itemView.findViewById(R.id.textViewItemNameXML);
            itemPrice=itemView.findViewById(R.id.textViewItemPriceXML);
            itemStock=itemView.findViewById(R.id.textViewItemStockXML);
            itemQuantity=itemView.findViewById(R.id.textViewItemQuantityXML);
            placeOrderButton=itemView.findViewById(R.id.placeOrderButtonXML);
            deleteIemButton=itemView.findViewById(R.id.deleteItemButtonXML);
            totalAmount=itemView.findViewById(R.id.totalAmountXML);
            increaseQuantity=itemView.findViewById(R.id.textViewItemQuantityIncreaseXML);
            decreaseQuantity=itemView.findViewById(R.id.textViewItemQuantityDecreaseXML);
        }
    }

    //This method helps to get the date as our defined date format which is [MM/dd/yyyy] and returns the date in string
    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateOnly.format(cal.getTime());
        return date;
    }
}
