import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowGrades extends JFrame
{
    SQLiteLocal database;
    JList<String> ListaNote;
    JButton Submite;

    JPanel LNote;
    JPanel LSubmite;

    JScrollPane Scrollpane;

    public ShowGrades(SQLiteLocal db, String Grades, String EmailStudent)
    {
        super("Scoate nota unui student.");
        setSize(400, 400);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        database = db;

        LNote = new JPanel();
        LSubmite = new JPanel();

        String[] Note = Grades.split("~");
        final DefaultListModel<String> defaultListModel = new DefaultListModel<>();
        for (String parse: Note)
        {
            String[] Informatii = parse.split("=");
            defaultListModel.addElement("Nota=" + Informatii[0] + ", Materia=" + Informatii[1] + ", Data=" +Informatii[2]);
        }

        ListaNote = new JList<>(defaultListModel);
        Submite = new JButton("Selecteaza nota");

        LSubmite.setLayout(new FlowLayout());

        LSubmite.add(Submite, BorderLayout.PAGE_END);

        add(new JScrollPane(ListaNote), BorderLayout.PAGE_START);
        add(LSubmite, BorderLayout.PAGE_END);

        Submite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if ( ListaNote.isSelectionEmpty() )
                {
                    PromptError("Nu ati facut nici o selectie.");
                    return;
                }
                String Information = ListaNote.getSelectedValue();

                String[] Buttons = {"Da", "Nu"};
                int Resultate = JOptionPane.showOptionDialog(null, "Sunteti sigur ca vreti sa scoateti aceasta nota?", "",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, Buttons, Buttons[0]);
                if (Resultate==JOptionPane.YES_OPTION)
                {
                    database.RemoveGrade(EmailStudent + "~" + Information);
                    dispose();
                }
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
