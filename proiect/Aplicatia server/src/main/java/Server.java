import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends JFrame{

    //Variabile pentru initializarea serverului.
    private static BCryptPasswordEncoder PasswordEncoder;
    private static ServerSocket serversocket;
    private static Socket socket;
    private static int Port = 13322;

    //Variabilele de comunicare intre student si server.
    private static BufferedReader bufferedreader;
    private static PrintStream printStream;

    //Elemente de interfata grafica.
    private JButton AddNewStudent = new JButton("Adauga student nou");
    private JButton AddNewGrade = new JButton("Adauga nota unui student");
    private JButton RemoveGrade = new JButton("Scoate o nota introdusa gresit.");
    private PrintStream Output;

    //Baza de date in sine
    public static SQLiteLocal DB;

    //Variabile folosite pe parcursul programului.
    private static String Input[];

    //Variabile folosite pentru a stoca informatiile necesare de la student.
    static String Username;
    static String Password;

    public static void main(String[] args) throws ClassNotFoundException, IOException {

        PasswordEncoder = new BCryptPasswordEncoder();
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DB = new SQLiteLocal();
                    Server server = new Server();
                    System.out.println("Serverul a fost deschis cu succes!\n");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        try
        {
            while(true)
            {
                //Conexiune intre utilizator si server
                /* IMPORTANT
                Conexiunea intre utilizator si server va fi de -> 1 mesaj primit, 1 mesaj trimis.
                In acest fel, utilizatorul va trimite datele de login la port-ul deschis de server,
                    serverul va verifica in baza de date daca se poate regasi studentul si va
                    trimite 1 din 2 mesaje:
                        - Datele studentului respectiv
                        - String-ul "Wrong username or password."
                Dupa mesajul trimis de la server, conexiunea va fi taiata.
                 */
                System.out.println("Asteptam conexiune noua...");

                serversocket = new ServerSocket(Port);
                socket = serversocket.accept();
                bufferedreader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                printStream = new PrintStream(socket.getOutputStream());
                PasswordEncoder = new BCryptPasswordEncoder();

                Input = bufferedreader.readLine().split(":", 2);

                Username = Input[0];
                Password = Input[1];

                System.out.println("Conexiune stabilita cu " + Username);

                String password = DB.SearchStudentAndRetrievePass(Username);

                if ( !password.equals("Eroare") )
                {
                    if (PasswordEncoder.matches(Password, password))
                    {
                        //Trimitem datele studentului inapoi
                        System.out.println("Utilizator gasit. Trimitem datele.");
                        printStream.println(DB.ReturnStudentData());
                    }
                    else
                    {
                        //Informam stundetul de o posibila eroare din partea lui.
                        System.out.println("Utilizatorul a fost gasit in baza de date, dar parola a fost incorecta.");
                        printStream.println("Email sau parola gresita.");
                    }
                }
                else
                {
                    System.out.println("Utilizatorul nu a fost gasit in baza de date.");
                    printStream.println("Email sau parola gresita.");
                }

                bufferedreader.close();
                printStream.close();
                serversocket.close();
                socket.close();

                System.out.println("Conexiune incheiata cu " + Username + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Interfata principala a serverului.
    public Server() {
        super("Server");

        setSize(640,480);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTextArea jTextArea = new JTextArea(60, 15);
        jTextArea.setEditable(false);
        PrintStream ps = new PrintStream(new ConsoleOutput(jTextArea));

        Output = System.out;
        System.setOut(ps);
        System.setErr(ps);

        RemoveGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new RemoveGrade(DB);
            }
        });

        AddNewStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String InsertStudent = new ClassAddNewStudent(DB).ReturnFinalString();
                System.out.println(InsertStudent);
            }
        });

        AddNewGrade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new ClassAddNewGrade(DB);
            }
        });

        setLayout(new GridBagLayout());
        GridBagConstraints Cons = new GridBagConstraints();

        Cons.gridx = 0;
        Cons.gridy = 0;
        Cons.insets = new Insets(10,10,10,10);
        Cons.anchor = GridBagConstraints.NORTH;
        add(AddNewStudent, Cons);

        Cons.gridx = 1;
        Cons.anchor = GridBagConstraints.FIRST_LINE_START;
        add(RemoveGrade, Cons);

        Cons.anchor = GridBagConstraints.FIRST_LINE_END;
        add(AddNewGrade, Cons);

        Cons.gridy = 1;
        Cons.gridx = 0;
        Cons.gridwidth = 2;
        Cons.fill = GridBagConstraints.BOTH;
        Cons.weighty = 1.0;
        Cons.weightx = 1.0;

        add(new JScrollPane(jTextArea), Cons);
    }
}
