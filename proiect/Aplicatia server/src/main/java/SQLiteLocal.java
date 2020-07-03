import java.sql.*;

public class SQLiteLocal {

    String Email;
    String DataNastere;
    String Cetatenie;
    String CNP;
    String Specializare;
    String BackupEmail;

    public Connection SqlConnect() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:F:\\BazaDeDateLicenta.db");
            connection.setAutoCommit(false);
        } catch (Exception e) {
            System.out.println(e.getClass().getName() + ". " + e.getMessage());
        }

        return connection;
    }

    public String SearchStudentAndRetrievePass(String Email) {
        String SQLStatement = "SELECT * FROM Student WHERE Email=?";

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
            preparedStatement.setString(1, Email);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();

            this.Email = Email;

            DataNastere = resultSet.getString("DataNastere");
            Cetatenie = resultSet.getString("Cetatenie");
            CNP = resultSet.getString("CNP");
            BackupEmail = resultSet.getString("BackupEmail");
            Specializare = resultSet.getString("Specializare");

            return resultSet.getString("Parola");

        } catch (SQLException e) {
            return "Eroare";
        }
    }

    public StringBuilder InsertNewStudent(String Email, String Parola, String DataNastere, String Cetatenie, String CNP, String BackupEmail, String Specializare,
                                          String Nume, String Prenume, String ParolaNecriptata) {
        String SQLString = "INSERT INTO Student(Email, Parola, DataNastere, Cetatenie, CNP, BackupEmail, Specializare, Nume, Prenume) VALUES (?,?,?,?,?,?,?,?,?)";
        StringBuilder builder = new StringBuilder();

        Email = CheckIfEmailExists(Email);

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLString)
        ) {

            builder.append("Pregatim mesajul pentru baza de date...\n");

            preparedStatement.setString(1, Email);
            preparedStatement.setString(2, Parola);
            preparedStatement.setString(3, DataNastere);
            preparedStatement.setString(4, Cetatenie);
            preparedStatement.setString(5, CNP);
            preparedStatement.setString(6, BackupEmail);
            preparedStatement.setString(7, Specializare);
            preparedStatement.setString(8, Nume);
            preparedStatement.setString(9, Prenume);

            preparedStatement.executeUpdate();

            builder.append("Datele au fost introduse cu succes in baza de date. ");

            connection.commit();

            GoogleEmail.SendEmailConfirmation(BackupEmail, "Buna ziua, \n\nAveti atasat in acest mesaj, email-ul si parola dumneavoastra.\n\n" +
                    "Email: " + Email + "\n" +
                    "Parola: " + ParolaNecriptata + "\n\n" +
                    "O zi buna.");

        } catch (SQLException e) {
            builder.append("Something went wrong.");
        }

        return builder;
    }

    public StringBuilder InsertNewGrade(String Nota, String Materie, String Email, String Data) {
        String SQLString = "INSERT INTO Note(Nota, Materie, Student, Date) VALUES (?,?,?,?)";
        StringBuilder builder = new StringBuilder();

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLString)
        ) {

            builder.append("Pregatim mesajul pentru baza de date...\n");

            preparedStatement.setString(1, Nota);
            preparedStatement.setString(2, Materie);
            preparedStatement.setString(3, Email);
            preparedStatement.setString(4, Data);

            preparedStatement.executeUpdate();

            builder.append("Datele au fost introduse cu succes in baza de date. ");

            connection.commit();

        } catch (SQLException e) {
            builder.append("Something went wrong.");
        }

        return builder;
    }

    public StringBuilder ReturnStudentData() {
        StringBuilder builder = new StringBuilder();

        builder.append(DataNastere);
        builder.append(":");
        builder.append(Cetatenie);
        builder.append(":");
        builder.append(CNP);
        builder.append(":");
        builder.append(BackupEmail);
        builder.append(":");
        builder.append(Specializare);

        builder.append(ReturnStudentGrades());

        return builder;
    }

    public StringBuilder ReturnStudentGrades() {
        StringBuilder builder = new StringBuilder();

        String SQLStatement = "select Note.Nota,Note.Materie, Note.Date, Materie.Semestru " +
                "from Note left join Materie on Note.Materie=Materie.Materie " +
                "where Note.Student=?";

        builder.append(";");

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
            preparedStatement.setString(1, Email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Nota = resultSet.getString("Nota");
                String Materie = resultSet.getString("Materie");
                String Data = resultSet.getString("Date");
                String Semestru = resultSet.getString("Semestru");

                builder.append(Nota);
                builder.append(":");
                builder.append(Materie);
                builder.append(":");
                builder.append(Data);
                builder.append(":");
                builder.append(Semestru);
                builder.append("~");
            }
        } catch (SQLException e) {
            System.out.println("Something went wrong.");
        }

        return builder;
    }

    public String CheckIfEmailExists(String Email) {
        int parse = 0;
        String ReturnedNumber = "";
        String CheckForEmail = Email;

        while (true) {
            String SQLStatement = "SELECT * FROM Student WHERE Email=?";

            try (Connection connection = this.SqlConnect();
                 PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
                preparedStatement.setString(1, CheckForEmail);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    parse++;
                    CheckForEmail = parse + "." + Email;
                } else {
                    break;
                }

            } catch (SQLException e) {
                System.out.println("Someting went wrong.");
            }
        }

        if (parse == 0)
            return Email;
        else
            ReturnedNumber = String.valueOf(parse);

        return ReturnedNumber + "." + Email;
    }

    public boolean CheckMaterie(String Materie) {
        String SQLStatement = "SELECT * FROM Materie WHERE Materie=?";

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
            preparedStatement.setString(1, Materie);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong while searching the database.");
        }

        return false;
    }

    public boolean CheckStudent(String Student) {
        String SQLStatement = "SELECT * FROM Student WHERE Email=?";

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
            preparedStatement.setString(1, Student);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println("Something went wrong while searching the database.");
        }
        return false;
    }

    public StringBuilder GetStudentGrades(String Email) {
        String SQLStatement = "SELECT Note.Nota, Note.Materie, Note.Student, Note.Date FROM Note WHERE Note.Student=? ORDER BY Note.Date";
        StringBuilder builder = new StringBuilder();

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement)) {
            preparedStatement.setString(1, Email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String Nota = resultSet.getString("Nota");
                String Materie = resultSet.getString("Materie");
                String Data = resultSet.getString("Date");

                builder.append(Nota);
                builder.append("=");
                builder.append(Materie);
                builder.append("=");
                builder.append(Data);
                builder.append("~");
            }

        } catch (SQLException e) {
            System.out.println("A aparut o eroare in baza de date..");
        }

        return builder;
    }

    public boolean RemoveGrade(String Informatii) {
        String SQLStatement = "DELETE FROM Note WHERE Nota=? AND Materie=? AND Student=? AND Date=?";
        String[] SplitInformatii = Informatii.split("~");

        String NumeStudent = SplitInformatii[0];

        String[] SecondSplit = SplitInformatii[1].split(",");

        String[] Nota = SecondSplit[0].split("=");
        String[] Materie = SecondSplit[1].split("=");
        String[] Data = SecondSplit[2].split("=");

        try (Connection connection = this.SqlConnect();
             PreparedStatement preparedStatement = connection.prepareStatement(SQLStatement))
        {
            preparedStatement.setString(1, Nota[1]);
            preparedStatement.setString(2, Materie[1]);
            preparedStatement.setString(3, NumeStudent);
            preparedStatement.setString(4, Data[1]);

            preparedStatement.executeUpdate();
            connection.commit();

            return true;
        } catch (SQLException e) {
            System.out.println("A aparut o eroare in sistem.");
        }

        return false;
    }
}







