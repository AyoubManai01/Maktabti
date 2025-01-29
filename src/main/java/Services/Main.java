package Services;
import java.sql.*;

public class Main {

    private static Connection con;
    private static String url = "jdbc:mysql://localhost:3306/Maktabti";
    private static String user = "root";
    private static String pass = "";
    private static Statement stmt;

    public static void main(String[] args) {

        try {
            con = DriverManager.getConnection(url, user, pass);
            System.out.println(con);
            System.out.println("connexion établie");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
}
