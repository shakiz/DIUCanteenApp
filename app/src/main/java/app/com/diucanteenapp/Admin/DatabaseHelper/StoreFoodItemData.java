package app.com.diucanteenapp.Admin.DatabaseHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.com.diucanteenapp.SharedModel.FoodItemModel;

public class StoreFoodItemData extends SQLiteOpenHelper {

    //Database version
    private static final int DATABASE_VERSION = 1;

    //Database name
    private static final String DATABASE_NAME = "FoodItemData.db";

    // User table name
    private static final String TABLE_FOOD_ITEM = "fooditem";

    // These are the columns for fooditem table
    private static final String COLUMN_ITEM_ID = "item_id";
    private static final String COLUMN_ITEM_NAME = "item_name";
    private static final String COLUMN_ITEM_PRICE = "item_price";
    private static final String COLUMN_ITEM_CATEGORY = "item_category";
    private static final String COLUMN_ITEM_ICON = "item_icon";

    // create fooditem table sql query
    private String CREATE_ITEM_TABLE = "CREATE TABLE " + TABLE_FOOD_ITEM + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM_NAME + " TEXT,"
            + COLUMN_ITEM_PRICE + " REAL," + COLUMN_ITEM_CATEGORY + " TEXT,"
            + COLUMN_ITEM_ICON + " TEXT" + ")";

    // drop table sql query
    private String DROP_ITEM_TABLE = "DROP TABLE IF EXISTS " + TABLE_FOOD_ITEM;


    public StoreFoodItemData(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ITEM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_ITEM_TABLE);
    }

    /**
     * This method is to create user record
     *
     * @param foodItemModel
     */
    public void addNewFoodItem(FoodItemModel foodItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, foodItemModel.getItemName());
        values.put(COLUMN_ITEM_PRICE, foodItemModel.getItemPrice());
        values.put(COLUMN_ITEM_CATEGORY, foodItemModel.getItemCategory());
        values.put(COLUMN_ITEM_ICON, foodItemModel.getItemIcon());

        // Inserting Row
        db.insert(TABLE_FOOD_ITEM, null, values);
        db.close();
    }

    /**
     * This method is to update single item record based on its name
     */
    public boolean updateIntoDatabase(int ID ,String name,Double price,String category,String itemIconPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("UPDATE ITEM : ",""+name);
        // delete user record by name
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_ITEM_NAME,name);
            cv.put(COLUMN_ITEM_PRICE,price);
            cv.put(COLUMN_ITEM_CATEGORY,category);
            cv.put(COLUMN_ITEM_ICON,itemIconPath);

            int updateValueConfirmation=db.update(TABLE_FOOD_ITEM,cv,"item_id="+ID,null);
            Log.v("Update Value : ",""+updateValueConfirmation);
            db.close();
            return true;
        }
        catch (Exception e){
            Log.v("EXCEPTION : ",""+e.getMessage());
            return false;
        }
    }
    /**
     * This method is to fetch all the product and return the list of product records
     *
     * @return list of FoodItemModel carrying all the available information of all the item
     */
    public ArrayList<FoodItemModel> getAllFoodItems() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_PRICE,
                COLUMN_ITEM_CATEGORY,
                COLUMN_ITEM_ICON
        };
        // sorting orders
        String sortOrder =
                COLUMN_ITEM_NAME + " ASC";
        ArrayList<FoodItemModel> foodItemList = new ArrayList<FoodItemModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the fooditem table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this is query function
         */
        Cursor cursor = db.query(TABLE_FOOD_ITEM, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FoodItemModel foodItemModel = new FoodItemModel();
                Log.v("___________","____________________________");
                Log.v("All saved :","Single product details");
                Log.v("ID : ",""+cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID)));
                Log.v("Name : ",""+cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                Log.v("Price : ",""+cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_PRICE)));
                Log.v("Category : ",""+cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                Log.v("Path :  : ",""+cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ICON)));
                Log.v("__________","____________________________________");

                foodItemModel.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                foodItemModel.setItemPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_PRICE)));
                foodItemModel.setItemCategory((cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY))));
                foodItemModel.setItemIcon(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ICON)));

                // Adding food item record to list
                foodItemList.add(foodItemModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return foodItemList list
        return foodItemList;
    }

    /**
     * This method will be used to get the item lists based on their category
     * Like if the category is Drinks then we get the list of food item which belongs to Drinks category
     */
    public ArrayList<FoodItemModel> getItemListBasedOnCategory(String category){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select * from " + TABLE_FOOD_ITEM + " where "+ COLUMN_ITEM_CATEGORY + "='" + category + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query, null);
        ArrayList<FoodItemModel> foodItemModels=new ArrayList<>();
        //Log.v("Date : ",""+date);
        if (cursor.moveToFirst()){
            do {
                FoodItemModel foodItemModel=new FoodItemModel();
                foodItemModel.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                foodItemModel.setItemPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_PRICE)));
                foodItemModel.setItemCategory(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                foodItemModel.setItemIcon(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ICON)));
                foodItemModels.add(foodItemModel);
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return foodItemModels;
    }

    /**
     * This method will be used to get the item lists based on their category
     * Like if the category is Drinks then we get the list of food item which belongs to Drinks category
     */
    public double getItemPriceBasedOnName(String itemName){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_ITEM_PRICE +" from " + TABLE_FOOD_ITEM + " where "+ COLUMN_ITEM_NAME + "='" + itemName + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query, null);
        double itemPrice=0;
        //Log.v("Date : ",""+date);
        if (cursor.moveToFirst()){
            do {
                itemPrice=cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_PRICE));
            }while (cursor.moveToNext());
        }
        Log.v("Price : ",""+itemPrice);
        cursor.close();
        sqLiteDatabase.close();
        return itemPrice;
    }

    //This method will be helpful to get item id based on its name
    public int getItemID(String itemName){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_ITEM_ID +" from " + TABLE_FOOD_ITEM + " where "+ COLUMN_ITEM_NAME + "='" + itemName + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        int itemID=0;
        if (cursor.moveToFirst()){
            do {
                itemID=cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID));
            }while (cursor.moveToNext());
        }
        Log.v("ID : ",""+itemID);
        cursor.close();
        sqLiteDatabase.close();
        return itemID;
    }


    //This method will be used to delete a single row from database based on the title
    public void deleteRow(String itemName){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_FOOD_ITEM + " WHERE " + COLUMN_ITEM_NAME + "='"+itemName+"'");
        Log.v("DELETED","ITEM DELETED : "+itemName);
        sqLiteDatabase.close();
    }
}
