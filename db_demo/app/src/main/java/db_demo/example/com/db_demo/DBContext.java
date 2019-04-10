package db_demo.example.com.db_demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBContext extends SQLiteOpenHelper {

    //TABLE NAME PREFIX
    public static final String TABLE_STUDENT = "student";

    // photo count table info
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_STUDENT_AGE = "student_age";
    public static final String COLUMN_STUDENT_STATUS = "student_status";

    private static final String LOG_TAG = "Database";
    private static final int DATABASE_VERSION = 3;
    // -----------------------------------------//
    private static final String DATABASE_NAME = "myDatabase.db";

    //db name
    private static final String[] DATABASE_TABLES = {TABLE_STUDENT};


    // create table query
    private static final String QUERY_CREATE_PHOTO_COUNT_TABLE = String.format(
            "CREATE TABLE %s ("
                    + "%s INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "%s TEXT NOT NULL, "
                    + "%s INTEGER NOT NULL, "

                    + "%s INTEGER  NOT NULL DEFAULT  1 )", TABLE_STUDENT,
            COLUMN_STUDENT_ID, COLUMN_STUDENT_NAME,
            COLUMN_STUDENT_AGE, COLUMN_STUDENT_STATUS);

    //query to create table list
    private static final String[] DATABASE_TABLES_QUERY = {
            QUERY_CREATE_PHOTO_COUNT_TABLE
    };

    public DBContext(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (String table : DATABASE_TABLES_QUERY) {
            try {
                db.execSQL(table);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error creating table", ex);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table : DATABASE_TABLES) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            } catch (Exception ex) {
                Log.e(LOG_TAG, "Error deleting table", ex);
            }
        }
        onCreate(db);
    }
}