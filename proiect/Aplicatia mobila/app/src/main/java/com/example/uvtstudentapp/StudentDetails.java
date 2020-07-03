package com.example.uvtstudentapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.uvtstudentapp.ui.main.PageViewModel;

import java.util.ArrayList;

public class StudentDetails extends AppCompatActivity {

    DataBase dataBase;
    private ListView listView;
    private TextView DataNastere;
    private TextView Cetatenie;
    private TextView CNP;
    private TextView BackupEmail;
    private TextView Specializare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_details);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        dataBase = new DataBase(this);
        //DataNastere = (TextView) findViewById(R.id.View_Datanastere);
        //Cetatenie = (TextView) findViewById(R.id.View_Cetatenie);

        PageViewModel pageViewModel = new PageViewModel();

        pageViewModel.setIndex(0);

        ShowStudentPersonalDetails();
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

        //DataNastere.setText("xD");
        //Cetatenie.setText(parse.get(1));
    }
}
