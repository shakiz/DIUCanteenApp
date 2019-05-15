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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.SharedModel.FoodItemModel;
import app.com.diucanteenapp.R;

public class RecyclerViewItemDetailsAdapter extends RecyclerView.Adapter<RecyclerViewItemDetailsAdapter.ViewHolder>{
    private ArrayList<FoodItemModel> foodItemModelArrayList;
    private Context context;
    private StoreFoodItemData storeFoodItemData;
    private String picturePath;
    private LinearLayout linearLayout;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //This will create and inflater which will later be used to inflate the layout
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view_for_all_item_recyclerview,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FoodItemModel foodItemModel=foodItemModelArrayList.get(i);
        viewHolder.itemName.setText(foodItemModel.getItemName());
        viewHolder.itemPrice.setText(""+foodItemModel.getItemPrice()+" Tk.");
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
        viewHolder.itemDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Here we are deleting ain item from database
                storeFoodItemData.deleteRow(foodItemModel.getItemName());
                Snackbar.make(linearLayout,"Please swipe up to refresh.",Snackbar.LENGTH_LONG).show();
            }
        });
        viewHolder.itemEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItemModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Here we are initializing all the components that we have for item
        private TextView itemName,itemPrice,itemCategory;
        private ImageView itemIcon;
        private Button itemEdit,itemDelete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName=itemView.findViewById(R.id.foodItemItemNameXML);
            itemPrice=itemView.findViewById(R.id.foodItemPriceXML);
            itemCategory=itemView.findViewById(R.id.foodItemCategoryXML);
            itemIcon=itemView.findViewById(R.id.foodItemIconXML);
            itemEdit=itemView.findViewById(R.id.foodItemEditXML);
            itemDelete=itemView.findViewById(R.id.foodItemDeleteXML);
        }
    }

    //Default constructor
    public RecyclerViewItemDetailsAdapter(ArrayList<FoodItemModel> foodItemModelArrayList, Context context, LinearLayout linearLayout){
        this.foodItemModelArrayList=foodItemModelArrayList;
        this.context=context;
        storeFoodItemData=new StoreFoodItemData(context);
        this.linearLayout=linearLayout;
    }


}