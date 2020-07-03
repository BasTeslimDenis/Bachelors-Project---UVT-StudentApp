package com.example.uvtstudentapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static BCryptPasswordEncoder passwordEncoder;
    private static Socket socket;
    private static PrintStream printStream;
    private static BufferedReader bufferedReader;
    private static String IP = "79.114.110.221";
    EditText Username;
    EditText Password;
    Button View;
    Button Login;
    DataBase database;
    private String MessageReceived = "Email sau parola gresita.";
    private String MessageToSend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = new DataBase(this);

        Username = (EditText) findViewById(R.id.EDITTEXT_Username);
        Password = (EditText) findViewById(R.id.EDITTEXT_PSSWD);
        Login = (Button) findViewById(R.id.BUTTON_Login);
        View = (Button) findViewById(R.id.ViewGrades);

        Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message_sender(v);
            }
        });

        View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if ( !database.CheckIfEmpty() )
                {
                    ToastMaker("Sincronizati-va cel putin o data cu serverul.");
                }
                else
                {
                    startActivity(new Intent(getApplicationContext(), PersonalDetails.class));
                }
            }
        });

        if ( !CheckForInternetConnection() )
        {
            ToastMaker("Nu exista o retea mobila disponibila.");
            return;
        }

    }

    public void Message_sender(View _view) {
        if (!(Username.getText().toString().contains("@")) || Password.getText().toString().length() < 6)
        {
            Toast.makeText(this, "Email sau parola invalida.", Toast.LENGTH_LONG).show();
            return;
        }

        passwordEncoder = new BCryptPasswordEncoder();

        MessageToSend = Username.getText().toString() + ":" + Password.getText().toString();

        ServerCommunication RN = new ServerCommunication();

        try {
            RN.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!MessageReceived.equals("Email sau parola gresita.")) {
            String[] Message = MessageReceived.split(";", 2);

            database.UpdataData();

            if (database.InserareDate(Message[0]))
            {
                try
                {
                    database.InserareNote(Message[1]);

                }catch ( Exception e )
                {
                    e.printStackTrace();
                }
                Intent intent = new Intent(this, PersonalDetails.class);
                startActivity(intent);
            }
            else
            {
                ToastMaker("A aparut o eroare.");
                return;
            }
        } else
        {
            ToastMaker(MessageReceived);
        }

    }

    public boolean CheckIfServerOnline()
    {
        try {
            Socket SocketCheck = new Socket(IP, 13322);
            if (SocketCheck.isConnected()) {
                SocketCheck.close();
                return true;
            }
        } catch (Exception e) {
            MessageReceived = "Server is currently offline.";
            return false;
        }
        return false;
    }

    public boolean CheckForInternetConnection() {
        ConnectivityManager ConnectManag = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (ConnectManag.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                ConnectManag.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    public void ToastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    class ServerCommunication extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                socket = new Socket(IP, 13322);
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                printStream = new PrintStream(socket.getOutputStream());

                printStream.println(MessageToSend);

                MessageReceived = bufferedReader.readLine();

                printStream.close();
                bufferedReader.close();
                socket.close();

            } catch (IOException e) {
            }
            return null;
        }
    }
}
