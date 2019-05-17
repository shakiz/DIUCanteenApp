package app.com.diucanteenapp.Student.Ac.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

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
    private String TAG = "CartItemAdapterOrder";
    private ArrayList<CartModel> cartModelArrayList;
    private StoreFoodItemData storeFoodItemData;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;
    private Context context;
    private int itemQuantity=0;
    private String userEmailStr;
    private OrderItemModel orderItemModel;
    private DatabaseHelperPlaceOrder databaseHelperPlaceOrder;
    private ImageView qrCodeImageView;
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
                    //Here we are telling that after placing the order a qr code will also be generated
                    //and later it will show on a pop dialog
                    final Dialog dialog=new Dialog(context);
                    dialog.setContentView(R.layout.custom_layout_for_qr_code);
                    //This method helps to initialize the dialog components with xml
                    initDialogComponents(dialog);
                    //This method generates QR code and set it to imageView
                    generateQRCodeAndSetToImageView(cartModel.getItemName(),itemQuantity);
                    dialog.setTitle("QR code");
                    try{
                        dialog.show();
                    }
                    catch (Exception e){
                        Log.v(TAG,e.getMessage());
                    }
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

    private void generateQRCodeAndSetToImageView(String itemName,int itemQuantity) {
        MultiFormatWriter multiFormatWriter=new MultiFormatWriter();
        try{
            //First we are creating a bitMatrix by the help of multiFormatter
            //Second we need a barcodeEncoder
            //Thirs we are encoding a bitMap by the help of barcodeEncoder
            //And finally setting the bitMap to our imageView for showing the barCode
            BitMatrix bitMatrix = multiFormatWriter.encode(itemName+itemQuantity, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCodeImageView.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            Log.v(TAG,e.getMessage());
        }
    }

    private void initDialogComponents(Dialog dialog) {
        qrCodeImageView=dialog.findViewById(R.id.qrCodeImageXML);
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
            //Here we are initiating all the attributes with xml
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

    //This method helps to get the date as our defined date format which is [MM/dd/yyyy] and returns the date in string
    public String getDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateOnly = new SimpleDateFormat("MM/dd/yyyy");
        String date = dateOnly.format(cal.getTime());
        return date;
    }
}
