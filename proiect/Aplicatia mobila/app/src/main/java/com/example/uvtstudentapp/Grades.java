package com.example.uvtstudentapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Grades extends AppCompatActivity {

    DataBase dataBase;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grades);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        dataBase = new DataBase(this);
        listView = findViewById(R.id.GradeView);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.navigation_dashboard:
                    {
                        return true;
                    }
                    case R.id.navigation_home:
                    {
                        startActivity(new Intent(getApplicationContext(), PersonalDetails.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                }
                return false;
            }
        });

        ShowStudentGrades();
    }

    public void ShowStudentGrades()
    {
        Cursor data = dataBase.returnNote();
        ArrayList<String> parse = new ArrayList<>();

        while( data.moveToNext() )
        {
            parse.add(data.getString(2) + "\n" + "Nota: " + data.getString(1) + "\nData: " + data.getString(3)
                   + "\nSemestrul: "  + data.getString(4));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, parse);
        listView.setAdapter(adapter);
    }
}
