import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GoogleEmail {

    final static String fromEmail = "uvtstudentapp@gmail.com"; //requires valid gmail id
    final static String password = "1234abcdMonitor"; // correct password for gmail id

        public static void SendEmailConfirmation(String toEmail, String content)
        {
            try
            {
                System.out.println("Se pregateste email-ul.");

                Properties properties = new Properties();
                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");

                Authenticator auth = new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(fromEmail, password);
                    }
                };

                Session session = Session.getInstance(properties, auth);

                sendEmail(session, toEmail, "[INFORMATII PERSONALE] Credentiale pentru aplicatia UVT StudentApp", content);
            }
            catch ( Exception e)
            {
                System.out.println("Nu s-a putut trimite mail-ul.");
                return;
            }
        }

    public static void sendEmail(Session session, String toEmail, String subject, String body){
        try
        {
            MimeMessage message = new MimeMessage(session);

            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress("no_reply@example.com", "UVT StudentApp"));
            message.setReplyTo(InternetAddress.parse("no_reply@example.com", false));
            message.setSubject(subject, "UTF-8");
            message.setText(body, "UTF-8");
            message.setSentDate(new Date());
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));

            Transport.send(message);
            System.out.println("Email-ul a fost trimis cu succes.!");
        }
        catch (Exception e) {
            System.out.println("Nu s-a putut trimite email-ul. ");
            return;
        }
    }
}














