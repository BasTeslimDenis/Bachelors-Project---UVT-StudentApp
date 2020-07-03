package com.example.uvtstudentapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PersonalDetails extends AppCompatActivity {

    DataBase dataBase;
    TextView DataNastere;
    TextView Cetatenie;
    TextView CNP;
    TextView Email;
    TextView Specializare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        dataBase = new DataBase(this);
        DataNastere = (TextView) findViewById(R.id.Edit_DataNastere);
        Cetatenie = (TextView) findViewById(R.id.Edit_Cetatenie);
        CNP = (TextView) findViewById(R.id.edit_CNP);
        Email = (TextView) findViewById(R.id.edit_Email);
        Specializare = (TextView) findViewById(R.id.edit_Spec);

        ShowStudentPersonalDetails();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_dashboard:
                    {
                        startActivity(new Intent(getApplicationContext(), Grades.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                    case R.id.navigation_home:
                    {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void ShowStudentPersonalDetails()
    {
        Cursor data = dataBase.returnData();
        ArrayList<String> parse = new ArrayList<>();

        while( data.moveToNext() )
        {
            parse.add(data.getString(1));
            parse.add(data.getString(2));
            parse.add(data.getString(3));
            parse.add(data.getString(4));
            parse.add(data.getString(5));
        }

        for ( String s: parse )
        {
            System.out.println(s);
        }

        DataNastere.setText(parse.get(0));
        Cetatenie.setText(parse.get(1));
        CNP.setText(parse.get(2));
        Email.setText(parse.get(3));
        Specializare.setText(parse.get(4));
    }
}
