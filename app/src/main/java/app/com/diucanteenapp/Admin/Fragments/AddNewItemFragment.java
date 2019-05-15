package app.com.diucanteenapp.Admin.Fragments;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import app.com.diucanteenapp.Admin.DatabaseHelper.StoreFoodItemData;
import app.com.diucanteenapp.SharedModel.FoodItemModel;
import app.com.diucanteenapp.R;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class AddNewItemFragment extends Fragment {

    private static final int REQUEST_GET_SINGLE_FILE = 1;
    private Context context;
    private EditText itemName,itemPrice;
    private String itemNameStr,itemCategoryStr;
    private Double itemPriceDouble;
    private ImageView thumbnailImage;
    private Button addItemButton,importImageButton;
    private Spinner itemCategorySpinner;
    private ArrayList<String> categoryNamesArrayList;
    private ArrayAdapter<String> stringArrayAdapterForCategoryItem;
    private String picturePath;
    private String adminEmail;
    private StoreFoodItemData storeFoodItemData;
    private FoodItemModel foodItemModel;
    private SharedPreferences userDetailsSharedPref;
    private SharedPreferences.Editor userDetailsSharedPredEditor;

    public AddNewItemFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_new_item, container, false);
        //This method will be used to initialize all the attributes with xml
        init(view);
        //This method will be used to get bundle data from the previous activity
        getUserData();
        //This method will be used to populate arraylist for category
        insertData();
        //This method will be used to set the spinner adapter
        setSpinnerAdapter();
        //Setting the on click listener to launch pck from gallery option
        importImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This method will be used to ask the permissions that required to load an image from gallery app
                askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE,1);
                askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,1);
                //This method will be used to lunch gallery
                pickFromGallery();
            }
        });
        //Setting the on click listener on add item button
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the user input into string and double
                itemNameStr=itemName.getText().toString();
                try{
                    itemPriceDouble=Double.parseDouble(itemPrice.getText().toString());
                }
                catch (Exception e){
                    Log.v("ERROR : ",""+e.getMessage());
                }
                //This method will be used to check the inputs
                inputValidation();
                //Here we are checking whether we have selected the valid category or not
                if (itemCategoryStr.isEmpty() || itemCategoryStr.equals("Select Item Category")){
                    Toast.makeText(context,"Please check category",Toast.LENGTH_LONG).show();
                }
                else if (itemCategoryStr.isEmpty() && itemCategoryStr.equals("Select Item Category")){
                    Toast.makeText(context,"Please check category",Toast.LENGTH_LONG).show();
                }
                else{
                    foodItemModel.setItemName(itemNameStr);
                    foodItemModel.setItemPrice(itemPriceDouble);
                    foodItemModel.setItemCategory(itemCategoryStr);
                    try{
                        foodItemModel.setItemIcon(picturePath);
                    }
                    catch (Exception e){
                        Toast.makeText(context,"Please check all data",Toast.LENGTH_LONG).show();
                    }

                    Log.v("Name : ",itemNameStr );
                    Log.v("Price : ",""+itemPriceDouble );
                    Log.v("Category : ",itemCategoryStr );
//                    Log.v("Picture path : ",picturePath );

                    try{
                        if (picturePath.isEmpty()){
                            Toast.makeText(context,"Please import an image.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            storeFoodItemData.addNewFoodItem(foodItemModel);
                            Toast.makeText(context,"Record added successfully",Toast.LENGTH_LONG).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(context,"Please import an image.",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        //Setting the on item select listener on spinner
        itemCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemCategoryStr=adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context,"Please select a category",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    public void getUserData(){
        userDetailsSharedPref=context.getSharedPreferences("user_details",MODE_PRIVATE);
        userDetailsSharedPredEditor=userDetailsSharedPref.edit();
        adminEmail=userDetailsSharedPref.getString("email",null);
        Log.v("Email : ","Debug : "+adminEmail);
    }

    private void inputValidation() {
        if (itemName.getText().toString().isEmpty() || itemPrice.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter valid data.",Toast.LENGTH_LONG).show();
        }
        else if (itemName.getText().toString().isEmpty() && itemPrice.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter valid data.",Toast.LENGTH_LONG).show();
        }
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            // Here we are explaining that should we ask for a permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                //This is called if user has denied the permission before
                //In this case are are just asking the permission again
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
            else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        }
        else {
            //This will pop up if the permissions are already granted
            Toast.makeText(context, "Already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK
        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png" , "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,REQUEST_GET_SINGLE_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Here we are checking three things one is our request code another one is the result code and finally the data
        if (requestCode == REQUEST_GET_SINGLE_FILE && resultCode == RESULT_OK && null != data) {
            //We are retrieving data from intent and storing it into uri
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            //Setting the image path by means of decoding the path
            thumbnailImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            //Logging the image path
            Log.v("Image path : ",picturePath);

        }
    }

    private void setSpinnerAdapter() {
        stringArrayAdapterForCategoryItem=new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,categoryNamesArrayList);
        stringArrayAdapterForCategoryItem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategorySpinner.setAdapter(stringArrayAdapterForCategoryItem);
    }

    private void insertData() {
        categoryNamesArrayList.add("Select Item Category");
        categoryNamesArrayList.add("Morning Snacks");
        categoryNamesArrayList.add("Drinks");
        categoryNamesArrayList.add("Lunch");
        categoryNamesArrayList.add("Evening snacks");
    }

    private void init(View view) {
        itemName=view.findViewById(R.id.itemNameXMl);
        itemPrice=view.findViewById(R.id.itemPriceXML);
        itemCategorySpinner=view.findViewById(R.id.spinnerItemCategoryXML);
        addItemButton=view.findViewById(R.id.addItemButtonXML);
        importImageButton=view.findViewById(R.id.addImageForItemXML);
        thumbnailImage=view.findViewById(R.id.imageThumbnailOfImportedImageXML);
        categoryNamesArrayList=new ArrayList<>();
        storeFoodItemData=new StoreFoodItemData(context);
        foodItemModel=new FoodItemModel();
    }

}
