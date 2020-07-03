import javax.swing.*;
import java.awt.*;
import java.util.Calendar;

/*
INTERFATA PRINCIPALA FOLOSITA PENTRU ADAUGAREA UNEI NOTE NOI UNUI STUDENT.
 */

public class ClassAddNewGrade extends JFrame {

    //Database variable
    SQLiteLocal DB;

    //JLabels
    JLabel LNota;
    JLabel LMaterie;
    JLabel LStudent;
    JLabel LData;

    //JTextFields
    JTextField TNota;
    JTextField TMaterie;
    JTextField TStudent;
    JTextField TData;

    //JButtons
    JButton Submit;
    JButton Cancel;

    //JPanels
    JPanel Panel;
    JPanel ButtonPanel;

    String Error = "";

    public ClassAddNewGrade(SQLiteLocal DB)
    {
        super("Adauga nota unui student");
        setSize(640,480);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        this.DB = DB;
        //Variabilele JButton
        Submit = new JButton("Incarca datele");
        Cancel = new JButton("Anuleaza");

        //Variabilele JLabel
        LNota = new JLabel("Nota:");
        LMaterie = new JLabel("Materia:");
        LStudent = new JLabel("Email-ul studentului:");
        LData = new JLabel("Data la care a fost pusa nota:");

        //Variabilele JTextField
        TNota = new JTextField("");
        TMaterie = new JTextField("");
        TStudent = new JTextField("");
        TData = new JTextField("");

        //Variabilele JPanel
        Panel = new JPanel();
        ButtonPanel = new JPanel();

        Panel.setLayout(new BoxLayout(Panel, BoxLayout.PAGE_AXIS));
        ButtonPanel.setLayout(new FlowLayout());

        Panel.add(LNota);
        Panel.add(TNota);

        Panel.add(LMaterie);
        Panel.add(TMaterie);

        Panel.add(LStudent);
        Panel.add(TStudent);

        Panel.add(LData);
        Panel.add(TData);

        ButtonPanel.add(Submit);
        ButtonPanel.add(Cancel);

        add(Panel, BorderLayout.PAGE_START);
        add(ButtonPanel, BorderLayout.PAGE_END);

        Cancel.addActionListener(actionEvent -> {
            String[] Buttons = {"Da", "Nu"};
            int Resultate = JOptionPane.showOptionDialog(null, "Sunteti siguri ca vreti sa anulati operatia?", "Introducere Nota...",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[1]);
            if (Resultate==JOptionPane.YES_OPTION)

                dispose();
        });

        Submit.addActionListener(actionEvent -> {
            String Nota;
            String Materie;
            String Student;
            String Data;

            Nota = TNota.getText();
            Materie = TMaterie.getText();
            Student = TStudent.getText();
            Data = TData.getText();

            if ( !VerificaNota(Nota).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificaMaterie(Materie).equals("none") )
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareStudent(Student).equals("none"))
            {
                PromptError(Error);
                return;
            }
            if ( !VerificareData(Data).equals("none") )
            {
                PromptError(Error);
                return;
            }

            String[] Buttons = {"Ok"};
            int Resultate = JOptionPane.showOptionDialog(null, "Datele au fost introduse cu succes.", "Introducere reusita!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
            if (Resultate==JOptionPane.YES_OPTION)
            {
                System.out.println( DB.InsertNewGrade(Nota, Materie, Student, Data) );
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
            return;
        }
    }

    public String VerificaNota(String Nota)
    {
        if (Nota.length()==0)
        {
            return Error = "Toata campurile sunt obligatorii.";
        }

        try {
            double nota = Double.parseDouble(Nota);

            if ( nota >= 0 && nota <= 10)
            {
                return "none";
            }
        }
        catch (Exception e)
        {
            return Error = "Nota poate fi doar o cifra intre 0-10.";
        }

        return Error = "Nota poate fi doar intre 0-10.";
    }

    public String VerificaMaterie(String Materie)
    {
        if (Materie.length()==0)
        {
            return Error = "Toata campurile sunt obligatorii.";
        }
        for (int i=0; i<Materie.length(); i++)
        {
            char ch = Materie.charAt(i);
            if (!(Character.isLetter(ch) || ch == ' '))
            {
                return Error = "Materia poate contine doar litere.";
            }
        }

        if ( !DB.CheckMaterie(Materie ) )
        {
            return Error = "Materia nu a fost gasita in baza de date.";
        }

        return "none";
    }

    public String VerificareStudent(String Student)
    {
        if (Student.length()==0)
        {
            return Error = "Toata campurile sunt obligatorii.";
        }
        if ( Student.contains("@") && Student.contains(".") && Student.matches(".*\\d.*") )
        {
            if ( !DB.CheckStudent(Student) )
            {
                return Error = "Nu s-a gasit email-ul in baza de date.";
            }
            return "none";
        }

        return Error = "Email-ul studentului nu contine criteriile necesare.";
    }

    public String VerificareData(String Data)
    {
        String[] DDMMYY = Data.split("/", 3);

        int An = 0;
        int Luna = 0;
        int Ziua = 0;

        try {
            Ziua = Integer.parseInt(DDMMYY[0]);
            Luna = Integer.parseInt(DDMMYY[1]);
            An = Integer.parseInt(DDMMYY[2]);
        }
        catch ( Exception e )
        {
            return Error = "Data introdusa nu este valida";
        }

        if ( An == Calendar.getInstance().get(Calendar.YEAR) || An == (Calendar.getInstance().get(Calendar.YEAR)-1) )
        {
            if ( Luna >= 1 && Luna <= 12)
            {
                if ( Ziua >= 1 && Ziua <= 31 )
                {
                    if ( Luna == 2 && Ziua > 28 )
                    {
                        if ( CheckForYear(An) && Ziua == 29)
                        {
                            return "none";
                        }
                        else
                        {
                            return Error = "Introducere eronata a datelor";
                        }
                    }

                    if ( An == Calendar.getInstance().get(Calendar.YEAR) )
                    {
                        if ( Luna > Calendar.getInstance().get(Calendar.MONTH)+1 )
                        {
                            return Error = "Data este invalida";
                        }
                    }

                    if ( Ziua == 31 && Luna % 2 == 1 )
                    {
                        return Error = "none";
                    }
                    else if (Ziua == 31)
                    {
                        return Error = "Data invalida";
                    }
                    return "none";
                }
                else
                {
                    return Error = "Data introdusa nu este valida";
                }
            }
            else
            {
                return Error = "Data introdusa nu este valida";
            }
        }
        else
        {
            return Error = "Anul poate sa fie doar cel curent sau anterior";
        }
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
}
















