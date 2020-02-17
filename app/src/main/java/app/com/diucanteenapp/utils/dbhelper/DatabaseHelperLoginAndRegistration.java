package app.com.diucanteenapp.utils.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import app.com.diucanteenapp.model.shared.User;

public class DatabaseHelperLoginAndRegistration extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "UserManager.db";

    // User table name
    private static final String TABLE_USER = "user";

    // Admin table name
    private static final String TABLE_ADMIN = "admin";

    // These are the columns for user table
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_PASSWORD = "user_password";
    private static final String COLUMN_USER_MOBILE_NUMBER = "user_mobile";
    private static final String COLUMN_USER_TYPE = "user_type";
    private static final String COLUMN_USER_STATUS="user_status";

    // These are the columns for admin table
    private static final String COLUMN_ADMIN_ID = "admin_id";
    private static final String COLUMN_ADMIN_EMAIL = "admin_email";
    private static final String COLUMN_ADMIN_PASSWORD = "admin_password";

    // create user table sql query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_MOBILE_NUMBER + " TEXT," + COLUMN_USER_TYPE + " TEXT," + COLUMN_USER_STATUS + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")";

    // create admin table sql query
    private String CREATE_ADMIN_TABLE = "CREATE TABLE " + TABLE_ADMIN + "("
            + COLUMN_ADMIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ADMIN_EMAIL + " TEXT,"
            + COLUMN_ADMIN_PASSWORD + " TEXT" + ")";


    // drop table sql query
    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;
    private String DROP_ADMIN_TABLE = "DROP TABLE IF EXISTS " + TABLE_ADMIN;

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelperLoginAndRegistration(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ADMIN_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_ADMIN_TABLE);
        // Create tables again
        onCreate(db);
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getName());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_MOBILE_NUMBER, user.getMobileNumber());
        values.put(COLUMN_USER_TYPE, user.getUserType());
        values.put(COLUMN_USER_STATUS,user.getUserStatus());
        // Inserting Row
        db.insert(TABLE_USER, null, values);
        db.close();
    }

    /**
     * This method is to create user record
     *
     * @param user
     */
    public void addAdmin(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ADMIN_EMAIL, user.getEmail());
        values.put(COLUMN_ADMIN_PASSWORD, user.getPassword());
        // Inserting Row
        db.insert(TABLE_ADMIN, null, values);
        Log.v("Successful","Admin sign up");
        db.close();
    }


    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUserExistance(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
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
     * This method to check student exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkStudent(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
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
     * This method to get student status whether his status approved or not
     *
     * @param email
     * @return a string
     */
    public String getStudentStatus(String email) {

        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_USER_STATUS +" from " + TABLE_USER + " where "+ COLUMN_USER_EMAIL + "='" + email + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query,null);
        String status="";
        if (cursor.moveToFirst()){
            do {
                status=cursor.getString(cursor.getColumnIndex(COLUMN_USER_STATUS));
            }while (cursor.moveToNext());
        }
        Log.v("STATUS : ",""+status);
        cursor.close();
        sqLiteDatabase.close();
        return status;
    }

    /**
     * This method to check admin exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkAdmin(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_ADMIN_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_ADMIN_EMAIL + " = ?" + " AND " + COLUMN_ADMIN_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query(TABLE_ADMIN, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
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
     * This method will be used to get the users based on the status
     */
    public ArrayList<String> getAllUserStatus(String userStatus){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_USER_EMAIL +" from " + TABLE_USER+ " where "+ COLUMN_USER_STATUS + "='" + userStatus + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query, null);
        ArrayList<String> userArrayList=new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                userArrayList.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return userArrayList;
    }

    /**
     * This method will be used to get the username on the email
     */
    public String getUserName(String email){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_USER_NAME +" from " + TABLE_USER+ " where "+ COLUMN_USER_EMAIL + "='" + email + "'";
        Cursor cursor=sqLiteDatabase.rawQuery(query, null);
        String userName="";
        if (cursor.moveToFirst()){
            do {
                userName=(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return userName;
    }

    public ArrayList<String> getAllUserName(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        String query = "select "+ COLUMN_USER_NAME +" from " + TABLE_USER;
        Cursor cursor=sqLiteDatabase.rawQuery(query, null);
        ArrayList<String > userName=new ArrayList<>();
        if (cursor.moveToFirst()){
            do {
                userName.add(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        sqLiteDatabase.close();
        return userName;
    }

    //This method will be used to delete a single row from database based on the user email
    public void deleteUser(String email){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM " + TABLE_USER + " WHERE " + COLUMN_USER_EMAIL + "='"+email+"'");
        Log.v("DELETED","USER DELETED : "+email);
        sqLiteDatabase.close();
    }

    /**
     * This method is to update single user status based on its name
     */
    public void updateUserStatus(String userName,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.v("UPDATE ITEM : ",""+userName);
        // delete user record by name
        try{
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_STATUS,"Approve");

            int updateValueConfirmation=db.update(TABLE_USER,cv,COLUMN_USER_EMAIL+" = '"+userName+"'",null);
            Log.v("Update Value : ",""+updateValueConfirmation);
        }
        catch (Exception e){
            Log.v("EXCEPTION : ",""+e.getMessage());
        }
        db.close();
    }

}
