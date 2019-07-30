package app.com.diucanteenapp.SharedDatabaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;

import app.com.diucanteenapp.SharedModel.OrderItemModel;

public class DatabaseHelperPlaceOrder extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "OrderManagerr.db";

    // Order table name
    private static final String TABLE_ORDER = "itemorder";


    // These are the columns for order table
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDERED_ITEM_NAME = "ordered_item_name";
    private static final String COLUMN_ORDER_BY_USER_EMAIL = "order_by_user_email";
    private static final String COLUMN_ORDER_QUANTITY = "order_quantity";
    private static final String COLUMN_ORDER_AMOUNT = "order_amount";
    private static final String COLUMN_ORDER_DATE = "order_date";


    // create order table sql query
    private String CREATE_ORDER_TABLE = "CREATE TABLE " + TABLE_ORDER + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ORDERED_ITEM_NAME + " TEXT,"+ COLUMN_ORDER_AMOUNT + " REAL,"
            + COLUMN_ORDER_DATE + " TEXT," + COLUMN_ORDER_BY_USER_EMAIL + " TEXT," + COLUMN_ORDER_QUANTITY + " REAL" + ")";


    // drop table sql query
    private String DROP_ORDER_TABLE = "DROP TABLE IF EXISTS " + TABLE_ORDER;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelperPlaceOrder(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_ORDER_TABLE);
    }

    /**
     * This method is to create order record
     *
     * @param orderItemModel
     */
    public void addOrder(OrderItemModel orderItemModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDERED_ITEM_NAME, orderItemModel.getItemName());
        values.put(COLUMN_ORDER_BY_USER_EMAIL, orderItemModel.getEmail());
        values.put(COLUMN_ORDER_DATE, orderItemModel.getDate());
        values.put(COLUMN_ORDER_QUANTITY, orderItemModel.getQuantity());
        values.put(COLUMN_ORDER_AMOUNT, orderItemModel.getAmount());
        // Inserting Row
        db.insert(TABLE_ORDER, null, values);
        Log.v("--------Order--------","");
        Log.v("Name : ",orderItemModel.getItemName());
        Log.v("Email : ",orderItemModel.getEmail());
        Log.v("Date : ",orderItemModel.getDate());
        Log.v("Quantity : ",""+orderItemModel.getQuantity());
        Log.v("Total Amount : ",""+orderItemModel.getAmount());
        Log.v("--------Order--------","");
        db.close();
    }

    /**
     * This method is to fetch all the order that been ordered by students
     * @return list of OrderItemModel carrying all the available information of all the item
     */
    public ArrayList<OrderItemModel> getAllFoodOrders() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ORDERED_ITEM_NAME,
                COLUMN_ORDER_BY_USER_EMAIL,
                COLUMN_ORDER_DATE,
                COLUMN_ORDER_AMOUNT,
                COLUMN_ORDER_QUANTITY
        };
        // sorting orders
        String sortOrder =
                COLUMN_ORDERED_ITEM_NAME + " ASC";
        ArrayList<OrderItemModel> orderItemModels = new ArrayList<OrderItemModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        // query the itemorder table
        /**
         * Here query function is used to fetch records from itemorder table this function works like we use sql query.
         * SQL query equivalent to this is query function
         */
        Cursor cursor = db.query(TABLE_ORDER, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                OrderItemModel orderItemModel = new OrderItemModel();
                orderItemModel.setItemName(cursor.getString(cursor.getColumnIndex(COLUMN_ORDERED_ITEM_NAME)));
                orderItemModel.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_BY_USER_EMAIL)));
                orderItemModel.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_DATE)));
                orderItemModel.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_QUANTITY)));
                orderItemModel.setAmount(cursor.getInt(cursor.getColumnIndex(COLUMN_ORDER_AMOUNT)));
                // Adding food item record to list
                orderItemModels.add(orderItemModel);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        // return orderItemModels list
        return orderItemModels;
    }
}
