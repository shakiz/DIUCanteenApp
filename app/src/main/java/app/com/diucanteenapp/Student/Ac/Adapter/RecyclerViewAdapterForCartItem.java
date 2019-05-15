package app.com.diucanteenapp.Student.Ac.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
import app.com.diucanteenapp.Student.Ac.DatabaseHelper.DatabaseHelperSaveCartDetails;
import app.com.diucanteenapp.Student.Ac.Model.CartModel;

public class RecyclerViewAdapterForCartItem extends RecyclerView.Adapter<RecyclerViewAdapterForCartItem.ViewHolder> {
    private ArrayList<CartModel> cartModelArrayList;
    private StoreFoodItemData storeFoodItemData;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;
    private Context context;
    private int itemQuantity=0;
    private String userEmailStr;
    private OrderItemModel orderItemModel;
    private DatabaseHelperPlaceOrder databaseHelperPlaceOrder;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_view_for_cart_item_recyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    public RecyclerViewAdapterForCartItem(ArrayList<CartModel> cartModelArrayList,Context context,String userEmailStr){
        this.cartModelArrayList=cartModelArrayList;
        this.context=context;
        this.userEmailStr=userEmailStr;
        storeFoodItemData=new StoreFoodItemData(context);
        databaseHelperSaveCartDetails=new DatabaseHelperSaveCartDetails(context);
        databaseHelperPlaceOrder=new DatabaseHelperPlaceOrder(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        final CartModel cartModel=cartModelArrayList.get(i);
        viewHolder.itemName.setText(cartModel.getItemName());
        viewHolder.itemPrice.setText(storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName())+""+" Tk.");
        viewHolder.increaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemQuantity=++itemQuantity;
                viewHolder.itemQuantity.setText(""+itemQuantity);
                viewHolder.totalAmount.setText(""+(itemQuantity*storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName()))+" Tk.");
            }
        });
        viewHolder.decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemQuantity>0){
                    itemQuantity=--itemQuantity;
                    viewHolder.itemQuantity.setText(""+itemQuantity);
                    viewHolder.totalAmount.setText(""+(itemQuantity*storeFoodItemData.getItemPriceBasedOnName(cartModel.getItemName()))+" Tk.");
                }
                else if (itemQuantity<=0){
                    Toast.makeText(context,"Quantity can not be zero.",Toast.LENGTH_SHORT).show();
                }
            }
        });
        viewHolder.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemQuantity>0){
                    orderItemModel=new OrderItemModel();
                    orderItemModel.setEmail(userEmailStr);
                    orderItemModel.setQuantity(itemQuantity);
                    orderItemModel.setItemName(cartModel.getItemName());
                    orderItemModel.setDate(getDate());
                    databaseHelperPlaceOrder.addOrder(orderItemModel);
                    Toast.makeText(context,"Order Placed",Toast.LENGTH_SHORT).show();
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
        TextView itemName,itemPrice,itemQuantity,increaseQuantity,decreaseQuantity,totalAmount;
        Button placeOrderButton,deleteIemButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.textViewItemNameXML);
            itemPrice=itemView.findViewById(R.id.textViewItemPriceXML);
            itemQuantity=itemView.findViewById(R.id.textViewItemQuantityXML);
            placeOrderButton=itemView.findViewById(R.id.placeOrderButtonXML);
            deleteIemButton=itemView.findViewById(R.id.deleteItemButtonXML);
            totalAmount=itemView.findViewById(R.id.totalAmountXML);
            increaseQuantity=itemView.findViewById(R.id.textViewItemQuantityIncreaseXML);
            decreaseQuantity=itemView.findViewById(R.id.textViewItemQuantityDecreaseXML);
        }
    }

    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateOnly.format(cal.getTime());
        return date;
    }
}
