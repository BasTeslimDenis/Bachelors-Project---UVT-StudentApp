import javax.swing.*;
import java.io.IOException;
import java.io.OutputStream;

public class ConsoleOutput extends OutputStream {
    private JTextArea jta;

    public ConsoleOutput(JTextArea textarea)
    {
        this.jta = textarea;
    }

    @Override
    public void write(int i) throws IOException{
        jta.append(String.valueOf((char)i));
        jta.setCaretPosition(jta.getDocument().getLength());
    }
}
