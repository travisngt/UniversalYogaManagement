package com.example.universalyoga_management.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;
import android.database.Cursor;

import com.example.universalyoga_management.models.ClassInstance;

import java.util.ArrayList;

public class YogaClassDatabaseHelper extends SQLiteOpenHelper {

    // Log tag for debugging
    private static final String TAG = "YogaClassDBHelper";

    // Database Info
    private static final String DATABASE_NAME = "yoga_classes.db";
    private static final int DATABASE_VERSION = 4; // Incremented version for schema changes

    // Yoga Classes Table
    public static final String TABLE_CLASSES = "yoga_classes";

    // Columns for Yoga Classes
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY = "class_day";
    public static final String COLUMN_TIME = "class_time";
    public static final String COLUMN_CAPACITY = "class_capacity";
    public static final String COLUMN_DURATION = "class_duration";
    public static final String COLUMN_PRICE = "class_price";
    public static final String COLUMN_TYPE = "class_type";
    public static final String COLUMN_DESCRIPTION = "class_description";

    // Yoga Class Instances Table
    public static final String TABLE_CLASS_INSTANCES = "class_instances";

    // Columns for Yoga Class Instances
    public static final String COLUMN_INSTANCE_ID = "instance_id";
    public static final String COLUMN_CLASS_ID = "class_id"; // Foreign key to link to yoga_classes
    public static final String COLUMN_INSTANCE_DATE = "instance_date"; // Date of instance
    public static final String COLUMN_TEACHER = "teacher"; // Teacher name
    public static final String COLUMN_COMMENTS = "comments"; // Optional comments

    public YogaClassDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "YogaClassDatabaseHelper: Constructor called");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create yoga_classes table
        String createClassesTable = "CREATE TABLE " + TABLE_CLASSES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DAY + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                COLUMN_CAPACITY + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_TYPE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createClassesTable);
        Log.d(TAG, "onCreate: Table created with query: " + createClassesTable);

        // Create class_instances table
        String createInstancesTable = "CREATE TABLE " + TABLE_CLASS_INSTANCES + " (" +
                COLUMN_INSTANCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CLASS_ID + " INTEGER, " +
                COLUMN_INSTANCE_DATE + " TEXT, " +
                COLUMN_TEACHER + " TEXT, " +
                COLUMN_COMMENTS + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_CLASS_ID + ") REFERENCES " + TABLE_CLASSES + "(" + COLUMN_ID + "))";
        db.execSQL(createInstancesTable);
        Log.d(TAG, "onCreate: Table created with query: " + createInstancesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: Upgrading database from version " + oldVersion + " to " + newVersion);
        // Drop old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASS_INSTANCES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLASSES);
        onCreate(db);
        Log.d(TAG, "onUpgrade: Tables recreated");
    }

    // Insert new yoga class into the database
    public boolean insertYogaClass(String day, String time, String capacity, String duration, String price, String type, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, day);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_CAPACITY, capacity);
        contentValues.put(COLUMN_DURATION, duration);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_DESCRIPTION, description);

        long result = db.insert(TABLE_CLASSES, null, contentValues);
        if (result == -1) {
            Log.e(TAG, "insertYogaClass: Insertion failed");
            return false;
        } else {
            Log.d(TAG, "insertYogaClass: Insertion successful, ID: " + result);
            return true;
        }
    }

    // Insert new class instance into the database
    public boolean insertClassInstance(long classId, String instanceDate, String teacher, String comments) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_CLASS_ID, classId); // Link to the yoga class
        contentValues.put(COLUMN_INSTANCE_DATE, instanceDate);
        contentValues.put(COLUMN_TEACHER, teacher);
        contentValues.put(COLUMN_COMMENTS, comments);

        long result = db.insert(TABLE_CLASS_INSTANCES, null, contentValues);
        if (result == -1) {
            Log.e(TAG, "insertClassInstance: Insertion failed");
            return false;
        } else {
            Log.d(TAG, "insertClassInstance: Insertion successful, Instance ID: " + result);
            return true;
        }
    }

    // Get all instances for a specific yoga class
    public ArrayList<ClassInstance> getClassInstances(long classId) {
        ArrayList<ClassInstance> instanceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query to get all class instances for the specified classId
        Cursor cursor = db.query(TABLE_CLASS_INSTANCES, null, COLUMN_CLASS_ID + " = ?",
                new String[]{String.valueOf(classId)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Retrieve values from the cursor for each instance
                long instanceId = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_ID));
                String instanceDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INSTANCE_DATE));
                String teacher = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEACHER));
                String comments = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COMMENTS));

                // Create ClassInstance object and add it to the list
                ClassInstance instance = new ClassInstance(instanceId, classId, instanceDate, teacher, comments);
                instanceList.add(instance);

                Log.d(TAG, "Retrieved instance - ID: " + instanceId + ", Date: " + instanceDate + ", Teacher: " + teacher);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.d(TAG, "No instances found for class ID: " + classId);
        }

        return instanceList; // Return the list of class instances
    }

    // Update an existing yoga class in the database
    public boolean updateYogaClass(long id, String day, String time, String capacity,
                                   String duration, String price, String type,
                                   String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_DAY, day);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_CAPACITY, capacity);
        contentValues.put(COLUMN_DURATION, duration);
        contentValues.put(COLUMN_PRICE, price);
        contentValues.put(COLUMN_TYPE, type);
        contentValues.put(COLUMN_DESCRIPTION, description);

        int rowsAffected = db.update(TABLE_CLASSES, contentValues, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        if (rowsAffected > 0) {
            Log.d(TAG, "updateYogaClass: Update successful for ID: " + id);
            return true;
        } else {
            Log.e(TAG, "updateYogaClass: Update failed for ID: " + id);
            return false;
        }
    }

    // Delete a yoga class from the database
    public boolean deleteYogaClass(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_CLASSES, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});

        if (rowsDeleted > 0) {
            Log.d(TAG, "deleteYogaClass: Deletion successful for ID: " + id);
            return true;
        } else {
            Log.e(TAG, "deleteYogaClass: Deletion failed for ID: " + id);
            return false;
        }
    }

    // Reset the database by deleting all records from the yoga_classes table
    public void resetDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CLASSES);
        Log.d(TAG, "resetDatabase: All records deleted from yoga_classes table.");
    }
}
