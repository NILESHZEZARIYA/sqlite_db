package db_demo.example.com.db_demo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class DBManager {
    private static final String LOG_TAG = "Database";
    private static DBManager dbManager = null;
    private static DBContext databaseContext = null;
    private static SQLiteDatabase database = null;
    private AtomicInteger openCounter = new AtomicInteger();

    public synchronized static DBManager getInstance() {
        if (dbManager == null) {
            throw new IllegalStateException(
                    String.format(
                            "%s is not initialized, App initializeDB(..) method first.",
                            DBManager.class.getSimpleName()));
        }
        return dbManager;
    }

    public static synchronized boolean initializeDB(Context context) {
        try {
            if (dbManager == null) {
                dbManager = new DBManager();
                databaseContext = new DBContext(context);
            }
            return true;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error initializing DB", ex);
            return false;
        }
    }

    public synchronized SQLiteDatabase getWritableDB() {
        try {
            if (openCounter.incrementAndGet() == 1) {
                database = databaseContext.getWritableDatabase();
            }
            return database;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error opening DB in W mode ", ex);
            return null;
        }
    }

    public synchronized boolean closeDatabase() {
        if (openCounter.decrementAndGet() == 0) {
            database.close();
        }
        return false;
    }

    public synchronized void insertStudent(
            Student mStudent) {
        ContentValues values = new ContentValues();
        values.put(DBContext.COLUMN_STUDENT_NAME,
                mStudent.getName());
        values.put(DBContext.COLUMN_STUDENT_AGE,
                mStudent.getAge());
        getWritableDB().insertWithOnConflict(DBContext.TABLE_STUDENT, null,
                values, SQLiteDatabase.CONFLICT_IGNORE);
        closeDatabase();
    }

    public synchronized ArrayList<Student> getAllStudentDetails(
    ) {
        ArrayList<Student> allData = new ArrayList<Student>();
        String select_query = "SELECT * FROM " + DBContext.TABLE_STUDENT
                + " WHERE " + DBContext.COLUMN_STUDENT_STATUS
                + " ='1'"
                + " ORDER BY "
                + DBContext.COLUMN_STUDENT_ID + " DESC";

        Log.i("select_query ",select_query + "");
        Cursor mCursor = getWritableDB().rawQuery(select_query, null);
        if (mCursor.getCount() != 0) {
            if (mCursor != null) {
                mCursor.moveToFirst();
                for (int i = 0; i < mCursor.getCount(); i++) {
                    Student mStudent = new Student();
                    mStudent
                            .setId(mCursor.getInt(mCursor
                                    .getColumnIndex(DBContext.COLUMN_STUDENT_ID)));
                    mStudent
                            .setName(mCursor.getString(mCursor
                                    .getColumnIndex(DBContext.COLUMN_STUDENT_NAME)));
                    mStudent
                            .setAge(mCursor.getInt(mCursor
                                    .getColumnIndex(DBContext.COLUMN_STUDENT_AGE)));
                    allData.add(mStudent);
                    mCursor.moveToNext();
                }
            }
        }
        mCursor.close();
        closeDatabase();
        return allData;
    }


    public synchronized void updateStudent(int studentId, String studentName,
                                               int studentAge) {
        String updateStudentQuery = "UPDATE "
                + DBContext.TABLE_STUDENT + " SET "
                + DBContext.COLUMN_STUDENT_NAME + " = '"
                + studentName + "' , " + DBContext.COLUMN_STUDENT_AGE + " = '" + studentAge
                + "' WHERE "
                + DBContext.COLUMN_STUDENT_ID + " = '"
                + studentId + "' AND "
                + DBContext.COLUMN_STUDENT_STATUS
                + " =  '1'";
        Log.i("SQL_QUERY_", updateStudentQuery);
        getWritableDB().execSQL(updateStudentQuery);
        closeDatabase();
    }


    public synchronized void deleteStudent(int studentId){
       String deleteStudentById = "DELETE FROM "
               + DBContext.TABLE_STUDENT
               + " WHERE  "
               +  DBContext.COLUMN_STUDENT_ID + " = '" + studentId+"' AND "
               +  DBContext.COLUMN_STUDENT_STATUS + " = '1'";
       getWritableDB().execSQL(deleteStudentById);
       closeDatabase();
    }
}