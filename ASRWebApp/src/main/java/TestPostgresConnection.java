import java.sql.Connection;
import java.sql.DriverManager;

public class TestPostgresConnection {

    public static void main(String[] args) {

        String DB_URL =
        	"<your_db_connection_url>";;

        String DB_USER = "azureuser";
        String DB_PASSWORD = "<your_db_password>";

        try {

            Class.forName("org.postgresql.Driver");

            Connection conn = DriverManager.getConnection(
                    DB_URL,
                    DB_USER,
                    DB_PASSWORD
            );

            System.out.println("Connection to PostgreSQL successful!");

            conn.close();

        } catch (Exception e) {
            System.out.println("Database connection failed!");
            e.printStackTrace();
        }
    }
}
