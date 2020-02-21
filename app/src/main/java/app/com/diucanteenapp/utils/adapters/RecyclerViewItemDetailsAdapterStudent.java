package app.com.diucanteenapp.utils.adapters;

import android.content.Context;
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

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import app.com.diucanteenapp.model.shared.FoodItemModel;
import app.com.diucanteenapp.R;
import app.com.diucanteenapp.utils.dbhelper.DatabaseHelperSaveCartDetails;
import app.com.diucanteenapp.model.students.CartModel;

public class RecyclerViewItemDetailsAdapterStudent extends RecyclerView.Adapter<RecyclerViewItemDetailsAdapterStudent.ViewHolder>{
    private ArrayList<FoodItemModel> foodItemModelArrayList;
    private Context context;
    private String picturePath,userEmail;
    private DatabaseHelperSaveCartDetails databaseHelperSaveCartDetails;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //This will create and inflater which will later be used to inflate the layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_recycler_for_all_item_for_student_recyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FoodItemModel foodItemModel=foodItemModelArrayList.get(i);
        viewHolder.itemName.setText(foodItemModel.getItemName());
        viewHolder.itemPrice.setText(""+foodItemModel.getItemPrice()+" Tk.");
        Log.v("VIEW ADAPTER","STOCK CHECK : "+foodItemModel.getItemStockAvailability());
        viewHolder.itemStock.setText(""+foodItemModel.getItemStockAvailability());
        viewHolder.itemCategory.setText(foodItemModel.getItemCategory());
        //Here we are getting the picture path that we stored and then retrieving it and finally decoding it and setting it to imageview
        picturePath=foodItemModel.getItemIcon();
        Log.v("Path : ",""+picturePath);
        //In order to load an image from path with Picasso
        //First we need to convert that path into file , then we can set it to imageView
        try{
            File f=new File(picturePath);
            Picasso.with(context).load(f).resize(100,100).into(viewHolder.itemIcon);
        }
        catch (Exception e){
            Log.v("ERROR PATH : ",""+e.getMessage());
        }
        //Handling the add to cart button works
        viewHolder.itemAddTocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseHelperSaveCartDetails.checkItemExistence(foodItemModel.getItemName())){
                    Toast.makeText(context,"Item already in cart",Toast.LENGTH_LONG).show();
                }
                else{
                    CartModel cartModel = new CartModel();
                    cartModel.setEmail(userEmail);
                    cartModel.setItemName(foodItemModel.getItemName());
                    databaseHelperSaveCartDetails.addCartItem(cartModel);
                    Log.v("CHECK FROM ADAPTER: ","STOCK : "+foodItemModel.getItemStockAvailability());
                    Toast.makeText(context,"Item carted",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Here we are initializing all the components that we have for item
        private TextView itemName,itemPrice,itemCategory,itemStock;
        private ImageView itemIcon,itemAddTocart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.foodItemItemNameXML);
            itemPrice=itemView.findViewById(R.id.foodItemPriceXML);
            itemStock=itemView.findViewById(R.id.foodItemStockXML);
            itemCategory=itemView.findViewById(R.id.foodItemCategoryXML);
            itemIcon=itemView.findViewById(R.id.foodItemIconXML);
            itemAddTocart =itemView.findViewById(R.id.foodItemAddToCartXML);
        }
    }

    //Default constructor
    public RecyclerViewItemDetailsAdapterStudent(ArrayList<FoodItemModel> foodItemModelArrayList, Context context, String userEmail){
        this.foodItemModelArrayList=foodItemModelArrayList;
        this.context=context;
        this.userEmail=userEmail;
        databaseHelperSaveCartDetails=new DatabaseHelperSaveCartDetails(context);
    }


}
