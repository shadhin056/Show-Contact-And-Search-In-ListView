package com.example.contactlistshowandsearchinlistview.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.contactlistshowandsearchinlistview.SQLite.SQLiteData.TABLE_NAME;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bank_asia_smartapp_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(SQLiteData.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public int insertNumber(String number,String name,String image, int count) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them

        values.put(SQLiteData.COLUMN_NUMBER, number);
        values.put(SQLiteData.COLUMN_NAME, name);
        values.put(SQLiteData.COLUMN_IMAGE, image);
        values.put(SQLiteData.COLUMN_PRIORITY, count);
        // insert row
        int id = (int) db.insert(TABLE_NAME, null, values);
        // close db connection
        db.close();
        // return newly inserted row id
        return id;
    }

    public boolean hasNumber(String phonenumber) {
        List<SQLiteData> datas = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_ID, SQLiteData.COLUMN_NUMBER,SQLiteData.COLUMN_NAME,SQLiteData.COLUMN_IMAGE, SQLiteData.COLUMN_PRIORITY},
                SQLiteData.COLUMN_NUMBER + "=?",
                new String[]{phonenumber}, null, null, null , "1");
        // looping through all rows and adding to list

        int count = cursor.getCount();
        if (cursor != null && count>0 ) {
            return true;
        }
        // close db connection
        db.close();
        // return notes list
        return false;
    }
    public List<SQLiteData> getNumbers() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_ID, SQLiteData.COLUMN_NAME, SQLiteData.COLUMN_IMAGE,SQLiteData.COLUMN_NUMBER,SQLiteData.COLUMN_PRIORITY,},
                null, null, null, null, SQLiteData.COLUMN_PRIORITY+" DESC");
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setId(cursor.getInt(cursor.getColumnIndex(SQLiteData.COLUMN_ID)));
                data.setName(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_NAME)));
                data.setNumber(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_NUMBER)));
                data.setImage(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_IMAGE)));
                data.setPriority(cursor.getInt(cursor.getColumnIndex(SQLiteData.COLUMN_PRIORITY)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }
    public int getID(String number){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_NAME,new String[]{"id"},"number =?",new String[]{number},null,null,null,"1");
        if (c.moveToFirst()) //if the row exist then return the id
            return c.getInt(c.getColumnIndex("id"));
        return -1;
    }

    public long update(String numberSelected, String nameSelected, String imageSelected) {
        SQLiteDatabase dbCheckPriority = this.getReadableDatabase();
        Cursor c = dbCheckPriority.query(TABLE_NAME,new String[]{SQLiteData.COLUMN_PRIORITY},"number = ?",new String[]{numberSelected},null,null,null,"1");
        Integer priorityValue=0;
        if (c.moveToFirst()) {
             priorityValue= c.getInt(c.getColumnIndex(SQLiteData.COLUMN_PRIORITY));
        }priorityValue=priorityValue+1;

        ContentValues cv = new ContentValues();
        cv.put(SQLiteData.COLUMN_NUMBER,numberSelected); //These Fields should be your String values of actual column names
        cv.put(SQLiteData.COLUMN_NAME,nameSelected);
        cv.put(SQLiteData.COLUMN_IMAGE,imageSelected);
        cv.put(SQLiteData.COLUMN_PRIORITY,priorityValue);
        SQLiteDatabase db = this.getReadableDatabase();

        //Integer a=db.update(TABLE_NAME, cv, "number = "+numberSelected, null);
        Integer a =db.update(TABLE_NAME, cv, "number = ?", new String[]{numberSelected});
        return a;

    }

   /* public SQLiteData getDataByID(String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{String.valueOf(data)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        SQLiteData note = new SQLiteData(
                cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<SQLiteData> getUserType() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"CLG"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getEduType() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"ED"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getRecusitionType() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"RT"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getDesignationType() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"DG"}, null, null, null, null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public SQLiteData getDivisionCode(String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_CODDES + "=? AND " + SQLiteData.COLUMN_HARDCOD + " =?",
                new String[]{data, "DIV"}, null, null, null, null);
        SQLiteData note = null;
        if(cursor != null && cursor.moveToFirst()){
            // prepare note object
             note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }

        

        // close the db connection
        cursor.close();

        return note;
    }


    public SQLiteData getfatherProfessionCode(String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_CODDES + "=? AND " + SQLiteData.COLUMN_HARDCOD + " =?",
                new String[]{data, "PF"}, null, null, null, null);

        SQLiteData note = null;
        if(cursor != null && cursor.moveToFirst()){
            // prepare note object
             note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }
        

        // close the db connection
        cursor.close();

        return note;
    }

    public SQLiteData getDistrictCode(String divisionCode, String district) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_CODDES + " =?",
                new String[]{divisionCode, district}, null, null, null, null);
        SQLiteData note = null;
        if(cursor != null && cursor.moveToFirst()){
             note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }

        // prepare note object
        

        // close the db connection
        cursor.close();

        return note;
    }

    public SQLiteData getUnionCode(String upazillaCode, String union) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_CODDES + " =?",
                new String[]{upazillaCode, union}, null, null, null, null);

        SQLiteData note = null;
        if(cursor != null && cursor.moveToFirst()){
            // prepare note object
             note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }
        

        // close the db connection
        cursor.close();

        return note;
    }

    public List<SQLiteData> getDistrictData(String data) {
        List<SQLiteData> datas1 = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{data}, null, null, SQLiteData.COLUMN_CODDES + " ASC", null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data1 = new SQLiteData();
                data1.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data1.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data1.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas1.add(data1);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas1;
    }

    public List<SQLiteData> getDivision() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"DIV"}, null, null, SQLiteData.COLUMN_CODDES + " ASC", null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst() ) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getUpazilla(String districtCode) {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{districtCode}, null, null, SQLiteData.COLUMN_CODDES + " ASC", null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getAllData() {
        List<SQLiteData> datas = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY " +
                SQLiteData.COLUMN_CODDES + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return datas;
    }

    public int getDataCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public void deleteNote() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from " + TABLE_NAME);
        db.close();
    }

    public SQLiteData getDivision(String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_SOFCOD + " =?",
                new String[]{"DIV", data}, null, null, null, null);

        if (cursor != null) {

        }
        cursor.moveToFirst();

        // prepare note object
        if (cursor.getCount() > 0  && cursor != null && cursor.moveToFirst()) {
            SQLiteData note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
            // close the db connection
            cursor.close();

            return note;
        } else {
            cursor.close();

            return null;
        }


    }

    public SQLiteData getFatherProfessionNamefromDb(String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_SOFCOD + " =?",
                new String[]{"PF", data}, null, null, null, null);

        if (cursor != null) {

        }
        cursor.moveToFirst();

        // prepare note object
        if (cursor.getCount() > 0 && cursor != null && cursor.moveToFirst()) {
            SQLiteData note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
            // close the db connection
            cursor.close();

            return note;
        } else {
            cursor.close();

            return null;
        }


    }

    public SQLiteData getDistrict(String hardCode, String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_SOFCOD + " =?",
                new String[]{hardCode, data}, null, null, null, null);
        SQLiteData note = null;
        if (cursor != null && cursor.moveToFirst()) {
            note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }


        // prepare note object


        // close the db connection
        cursor.close();

        return note;
    }

    public SQLiteData getUpazilla(String hardCode, String data) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=? AND " + SQLiteData.COLUMN_SOFCOD + " =?",
                new String[]{hardCode, data}, null, null, null, null);
        SQLiteData note = null;
        if (cursor != null && cursor.moveToFirst()) {
            note = new SQLiteData(
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)),
                    cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
        }

        // prepare note object


        // close the db connection
        cursor.close();

        return note;
    }

    public List<SQLiteData> getFatherProfession() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"PF"}, null, null, SQLiteData.COLUMN_CODDES + " ASC", null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }

    public List<SQLiteData> getSkill() {
        List<SQLiteData> datas = new ArrayList<>();
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{SQLiteData.COLUMN_HARDCOD, SQLiteData.COLUMN_SOFCOD, SQLiteData.COLUMN_CODDES},
                SQLiteData.COLUMN_HARDCOD + "=?",
                new String[]{"SKL"}, null, null, SQLiteData.COLUMN_CODDES + " ASC", null);
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                SQLiteData data = new SQLiteData();
                data.setCoddes(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_CODDES)));
                data.setHardcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_HARDCOD)));
                data.setSofcod(cursor.getString(cursor.getColumnIndex(SQLiteData.COLUMN_SOFCOD)));

                datas.add(data);
            } while (cursor.moveToNext());
        }
        // close db connection
        db.close();
        // return notes list
        return datas;
    }*/

}
