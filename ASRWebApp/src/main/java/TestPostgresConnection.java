import java.sql.Connection;
import java.sql.DriverManager;

public class TestPostgresConnection {

    public static void main(String[] args) {

        String DB_URL =
        	"jdbc:postgresql://ccds-sql-server-1.postgres.database.azure.com:5432/subtitle_db?sslmode=require";;

        String DB_USER = "azureuser";
        String DB_PASSWORD = "Az@12345";

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