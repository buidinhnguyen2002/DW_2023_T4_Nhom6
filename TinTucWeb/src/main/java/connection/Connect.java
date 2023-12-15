package connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connect {
    public Connection getconnecttion() throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/data_mart";
            String user = "root";
            String pass = "123456";
            return DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
        return null;
    }
}
