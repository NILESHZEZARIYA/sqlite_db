package db_demo.example.com.db_demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DBManager mDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDb();
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertStudent();
            }
        });

        findViewById(R.id.btnShow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStudentDetails();
            }
        });

        findViewById(R.id.btnupdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudentDetails();
            }
        });


        findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });


    }

    private void initDb() {
        DBManager.initializeDB(this);
        mDBManager = DBManager.getInstance();
    }
    private void insertStudent(){
        Student mStudent = new Student();
        mStudent.setName("ALICE");
        mStudent.setAge(26);
        mDBManager.insertStudent(mStudent);
    }

    private void getStudentDetails(){
        ArrayList<Student> arrStudent = mDBManager.getAllStudentDetails();

        for (Student mStudent :
        arrStudent) {
            Log.i("Student : " ,""+ mStudent.toString());
        }
    }

    private void updateStudentDetails(){
        mDBManager.updateStudent(1,"EMA",24);
    }

    private void deleteStudent(){
        mDBManager.deleteStudent(1);
    }
}
