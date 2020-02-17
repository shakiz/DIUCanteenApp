package app.com.diucanteenapp.utils.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.com.diucanteenapp.model.students.CartModel;

public class DatabaseHelperSaveCartDetails extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 5;
    // Database Name
    private static final String DATABASE_NAME = "CartItemsUser.db";
    // User cart details table name
    private static final String TABLE_USER_CART_DETAILS = "cartitems";
    // These are the columns for user cart details table
    private static final String COLUMN_CART_ID = "cart_id";
    private static final String COLUMN_USER_EMAIL = "cart_user_email";
    private static final String COLUMN_ITEM_NAME = "cart_item_name";
    // create cart details details table sql query
    private String CREATE_CART_DETAILS_TABLE = "CREATE TABLE " + TABLE_USER_CART_DETAILS + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT" + ")";

    // drop table sql query
    private String DROP_CART_DETAILS_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER_CART_DETAILS;
    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelperSaveCartDetails(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_CART_DETAILS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_CART_DETAILS_TABLE);
    }

    /**
     * This method is to create cart record
     *
     * @param cartModel
     */
    public void addCartItem(CartModel cartModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, cartModel.getItemName());
        values.put(COLUMN_USER_EMAIL, cartModel.getEmail());
        // Inserting Row
        db.insert(TABLE_USER_CART_DETAILS, null, values);
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param itemName
     * @return true/false
     */
    public boolean checkItemExistence(String itemName) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_ITEM_NAME + " = ?";

        // selection argument
        String[] selectionArgs = {itemName};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER_CART_DETAILS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method is to fetch all the cart items
     * @return list of CartModel carrying all the available information of all the item
     */
    public ArrayList<CartModel> getAllFoodCartItems() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID,
                COLUMN_ITEM_NAME,
        };
        // sorting orders
        String sortOrder =
                COLUMN_ITEM_NAME + " ASC";
        ArrayList<CartModel> cartFoodItemList = new ArrayList<CartModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the usercartdetails table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this is query function
         */
        Cursor cursor = db.query(TABLE_USER_CART_DETAILS, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                CartModel cartModel = new CartModel();
                cartModel.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                // Adding food item record to list
                cartFoodItemList.add(cartModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return cartitemList list
        return cartFoodItemList;
    }

    //This method will be used to delete a single row from database based on the title
    public void deleteRow(String itemName){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_USER_CART_DETAILS + " WHERE " + COLUMN_ITEM_NAME + "='"+itemName+"'");
        Log.v("DELETED","ITEM DELETED : "+itemName);
        sqLiteDatabase.close();
    }
}
