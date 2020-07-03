import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveGrade extends JFrame{

    SQLiteLocal database;

    JLabel LStudent;
    JTextField TStudent;

    JButton Cauta;

    JPanel Information;
    JPanel Button;

    public RemoveGrade(SQLiteLocal db)
    {
        super("Scoate nota unui student.");
        setSize(640, 480);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        database = db;

        LStudent = new JLabel("Introduceti email-ul studentlui: ");

        TStudent = new JTextField("");

        Cauta =  new JButton("Cauta student...");

        Information = new JPanel();
        Button =  new JPanel();

        Information.setLayout(new BoxLayout(Information, BoxLayout.PAGE_AXIS));
        Button.setLayout(new FlowLayout());

        Information.add(LStudent, BorderLayout.PAGE_START);
        Information.add(TStudent, BorderLayout.PAGE_START);

        Button.add(Cauta, BorderLayout.PAGE_END);

        add(Information, BorderLayout.PAGE_START);
        add(Button, BorderLayout.PAGE_END);

        Cauta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String EmailStudent = TStudent.getText();

                if ( !database.CheckStudent(EmailStudent) )
                {
                    PromptError("Studentul nu exista in baza de date");
                    return;
                }

                StringBuilder StudentGrades = database.GetStudentGrades(EmailStudent);
                String Temporary = StudentGrades.toString();

                if ( Temporary.length()== 0 )
                {
                    PromptError("Studentul nu are note in baza de date.");
                    return;
                }

                new ShowGrades(database, Temporary, EmailStudent);

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

}
