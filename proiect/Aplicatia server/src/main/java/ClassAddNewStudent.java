import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

public class ClassAddNewStudent extends JFrame {

    //JLabels
    JLabel LNumeStudent;
    JLabel LPrenumeStudent;
    JLabel LDataNastere;
    JLabel LCetatenie;
    JLabel LCNP;
    JLabel LSpecializare;
    JLabel LEmailRezerva;

    //JTextFields
    JTextField TNumeStudent;
    JTextField TPrenumeStudent;
    JTextField TDataNastere;
    JTextField TCetatenie;
    JTextField TCNP;
    JTextField TSpecializare;
    JTextField TEmailRezerva;

    //JButtons
    JButton Submit;
    JButton Cancel;

    //JPanels
    JPanel Panel;
    JPanel ButtonPanel;

    //Variabile globale
    String Error;
    String FinalString;
    String AnNastere;
    String LunaNastere;
    String ZiuaNastere;
    String ParolaNecriptata;

    public ClassAddNewStudent(SQLiteLocal DB)
    {
        super("Adauga student nou");
        setSize(640,480);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        /*
        Initializam TOATE variabilele
        - Butoanele - Folosite pentru finalizarea actiunilor
        - Labelele - Folosite pentru a indica utilizatorului unde se introduca anumite date.
        - Campurile de Text - Folosite pentru introducerea datelor in sine
        - Panele - Folosite pentru a "captura" anumite variabile in locatii specifice.
        */

        //Variabilele JButton
        Submit = new JButton("Incarca datele");
        Cancel = new JButton("Anuleaza");

        //Variabilele JLabel
        LNumeStudent = new JLabel("Nume Student:");
        LPrenumeStudent = new JLabel("Prenume Student:");
        LDataNastere = new JLabel("Data nastere ( DD/MM/YYYY ):");
        LCetatenie = new JLabel("Cetatenie Student:");
        LCNP = new JLabel("CNP Student:");
        LEmailRezerva = new JLabel("Email de rezerva: ");
        LSpecializare = new JLabel("Specializare:");

        //Variabilele JTextField
        TNumeStudent = new JTextField("");
        TPrenumeStudent = new JTextField("");
        TDataNastere = new JTextField("");
        TCetatenie = new JTextField("");
        TCNP = new JTextField("");
        TEmailRezerva = new JTextField("");
        TSpecializare = new JTextField("");

        //Variabilele JPanel
        Panel = new JPanel();
        ButtonPanel = new JPanel();

        Error = "";
        FinalString = "";

        Panel.setLayout(new BoxLayout(Panel, BoxLayout.PAGE_AXIS));
        ButtonPanel.setLayout(new FlowLayout());

        //Introducerea TUTUROR variabilelor in interfata de Introducere de student.
        Panel.add(LNumeStudent);
        Panel.add(TNumeStudent);

        Panel.add(LPrenumeStudent);
        Panel.add(TPrenumeStudent);

        Panel.add(LDataNastere);
        Panel.add(TDataNastere);

        Panel.add(LCetatenie);
        Panel.add(TCetatenie);

        Panel.add(LCNP);
        Panel.add(TCNP);

        Panel.add(LEmailRezerva);
        Panel.add(TEmailRezerva);

        Panel.add(LSpecializare);
        Panel.add(TSpecializare);

        ButtonPanel.add(Submit);
        ButtonPanel.add(Cancel);

        add(Panel, BorderLayout.PAGE_START);
        add(ButtonPanel, BorderLayout.PAGE_END);

        Cancel.addActionListener(actionEvent -> {
            String[] Buttons = {"Da", "Nu"};
            int Resultate = JOptionPane.showOptionDialog(null, "Sunteti siguri ca vreti sa anulati operatia?", "Introducere Student...",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[1]);
            if (Resultate==JOptionPane.YES_OPTION)
                dispose();
        });
        Submit.addActionListener(actionEvent -> {

            String NumeStudent;
            String PrenumeStudent;
            String DataNastereStudent;
            String CetatenieStudent;
            String CNPStudent;
            String EmailStudent;
            String SpecializareStudent;

            NumeStudent = TNumeStudent.getText();
            PrenumeStudent = TPrenumeStudent.getText();
            DataNastereStudent = TDataNastere.getText();
            CetatenieStudent = TCetatenie.getText();
            CNPStudent = TCNP.getText();
            EmailStudent = TEmailRezerva.getText();
            SpecializareStudent = TSpecializare.getText();

            if ( !VerificaNume(NumeStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificaPrenume(PrenumeStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificaDataNastere(DataNastereStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareCetatenie(CetatenieStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareCNP(CNPStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareEmail(EmailStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareSpecializare(SpecializareStudent).equals("none") )
            {
                PromptError(Error);
                return;
            }

            String[] Buttons = {"Ok"};
            int Resultate = JOptionPane.showOptionDialog(null, "Datele au fost introduse cu succes.", "Introducere reusita!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
            if (Resultate==JOptionPane.YES_OPTION)
            {
                System.out.println(DB.InsertNewStudent(CreateEmail(NumeStudent, PrenumeStudent, AnNastere), CreatePassword(), DataNastereStudent, CetatenieStudent,
                        CNPStudent, EmailStudent, SpecializareStudent, NumeStudent, PrenumeStudent, ParolaNecriptata));
                dispose();
            }
        });
    }

    public void PromptError(String Error)
    {
        String[] Buttons = {"Ok"};
        int Resultate = JOptionPane.showOptionDialog(null, Error, "Introducere eroanata a datelor.",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
        if (Resultate==JOptionPane.YES_OPTION)
        {
        }
    }

    public String VerificaNume(String Nume)
    {
        if (Nume.length()==0)
        {
            return Error = "Toate campurile sunt obligatorii!";
        }
        for (int i=0; i<Nume.length(); i++)
        {
            char ch = Nume.charAt(i);
            if (!(Character.isLetter(ch) || ch == ' '))
            {
                return Error = "Numele poate contine doar litere.";
            }
        }
        return "none";
    }

    public String VerificaPrenume(String Prenume)
    {
        if (Prenume.length()==0)
        {
            return Error = "Toate campurile sunt obligatorii!";
        }
        for (int i=0; i<Prenume.length(); i++)
        {
            char ch = Prenume.charAt(i);
            if (!(Character.isLetter(ch) || ch == ' '))
            {
                return Error = "Prenumele poate contine doar litere.";
            }
        }
        return "none";
    }

    public String VerificaDataNastere(String DataNastere)
    {
        String [] DDMMYY = DataNastere.split("/",3 );

        AnNastere = DDMMYY[2];
        LunaNastere = DDMMYY[1];
        ZiuaNastere = DDMMYY[0];

        int An = 0;
        int Luna = 0;
        int Ziua = 0;

        try {
            An = Integer.parseInt(DDMMYY[2]);
            Luna = Integer.parseInt(DDMMYY[1]);
            Ziua = Integer.parseInt(DDMMYY[0]);
        }
        catch (Exception e)
        {
            return Error = "Data de nastere invalida.";
        }

        //Persoanele nascute intre  (Anul Curent-80) si (Anul Curent-10)
        if ( An > Calendar.getInstance().get(Calendar.YEAR) - 80 && An < Calendar.getInstance().get(Calendar.YEAR) - 10 )
        {
            //Verificam daca Luna este introdusa corect ( 1-12 )
            if ( Luna > 0 && Luna < 13 )
            {
                //Verificam daca ziua este trecuta corect ( 1-31 )
                if ( Ziua > 0 && Ziua < 32)
                {
                    //Verificam daca este nascut in Februarie, dupa data de 28.
                    if ( Ziua > 28 && Luna == 2)
                    {
                        //Daca da, verificam daca este an bisect si suntem in 29
                        if ( CheckForYear(An) && Ziua == 29)
                    {
                        return Error = "none";
                    }
                        else
                        {
                            return Error = "Data de nastere invalida.";
                        }
                    }
                    //Verificam daca studentul este nascut in lunile 1 3 5 7 8 10 12
                    if ( Ziua == 31 && (Luna == 1 || Luna == 3 || Luna == 5 || Luna == 7 || Luna == 8 || Luna == 10 || Luna == 12) )
                    {
                        return Error = "none";
                    }
                    else if (Ziua == 31)
                    {
                        return Error = "Data de nastere invalida.";
                    }

                    return "none";
                }
                else
                {
                    return Error = "Data de nastere invalida";
                }
            }
            else
            {
                return Error = "Data de nastere invalida.";
            }
        }
        return Error = "Data de nastere invalida";
    }
    public boolean CheckForYear(int year)
    {
        if ( year % 4 == 0 )
        {
            if ( year % 100 == 0 )
            {
                if ( year % 400 == 0 )
                {
                    return true;
                } else return false;
            }else return true;
        }
        return false;
    }

    public String VerificareCetatenie(String Cetatenie)
    {
        if (Cetatenie.length()==0)
        {
            return Error = "Toate campurile sunt obligatorii!";
        }
        for (int i=0; i<Cetatenie.length(); i++)
        {
            char ch = Cetatenie.charAt(i);
            if (!(Character.isLetter(ch) || ch == ' '))
            {
                return Error = "Cetatenia poate continue doar litere.";
            }
        }

        return "none";

    }

    public String VerificareCNP(String CNP)
    {
        if (CNP.length()!=13)
        {
            return Error = "CNP Invalid";
        }

        if ( !CNP.matches("[0-9]+") )
        {
            return Error = "CNP-ul poate contine doar numere.";
        }

        String[] CNPBuilder = CNP.split("");
        int[] SumaCNP = new int[CNP.length()];
        int suma;

        if ( !ZiuaNastere.equals( CNP.substring(5,7)) )
        {
            return Error = "CNP-ul nu corespunde cu data de nastere";
        }
        if ( !LunaNastere.equals(CNP.substring(3,5)) )
        {
            return Error = "CNP-ul nu corespunde cu data de nastere";
        }
        if ( !AnNastere.substring(2).equals(CNP.substring(1,3)) )
        {
            return Error = "CNP-ul nu corespunde cu data de nastere";
        }

        for (int i=0; i<CNP.length(); i++)
        {
            SumaCNP[i] = Integer.parseInt(CNPBuilder[i]);
        }

        suma = SumaCNP[0]*2 + SumaCNP[1]*7 + SumaCNP[2]*9 + SumaCNP[3]*1 + SumaCNP[4]*4 + SumaCNP[5]*6 + SumaCNP[6]*3 + SumaCNP[7]*5
                + SumaCNP[8]*8 + SumaCNP[9]*2 + SumaCNP[10]*7 + SumaCNP[11]*9;

        suma%=11;
        if (suma==10)
        {
            suma = 1;
        }
        if (suma==SumaCNP[12])
        {
            return "none";
        }
        else
        {
            return Error = "CNP Invalid";
        }

    }

    public String VerificareEmail(String Email)
    {
        if ( Email.length()==0 )
        {
            return Error = "Toate campurile sunt obligatorii!";
        }
        if ( Email.contains("@") && Email.contains(".") )
        {
            return "none";
        }
        return Error = "Email Invalid.";
    }

    public String VerificareSpecializare(String Specializare)
    {
        if (Specializare.length()==0)
        {
            return Error = "Toate campurile sunt obligatorii!";
        }
        else
        {
            return "none";
        }
    }

    public String CreatePassword()
    {
        String PasswordChars = "abcdefghijklmnopqrstuvwxyz0123456789";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String parola = RandomStringUtils.random( 6, PasswordChars );

        ParolaNecriptata = parola;

        return passwordEncoder.encode(parola);
    }

    public String CreateEmail(String Nume, String Prenume, String An)
    {
        String Email = "";

        String[] BuildMessageNume = Nume.toLowerCase().split(" ", 2);
        String[] BuildMessagePrenume = Prenume.toLowerCase().split(" ", 2);

        Email += BuildMessagePrenume[0];
        Email += ".";
        Email += BuildMessageNume[0];
        Email += An.substring(2);
        Email += "@e-uvt.ro";

        return Email;
    }

    public String ReturnFinalString()
    {
        return FinalString;
    }
}
