
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;


public class DatabaseConnector extends Observable {
    private static DatabaseConnector instance;
    private String tableName;
    private ResultSet resultSet;
    private Connection connector;

    private DatabaseConnector() throws SQLException{
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public static DatabaseConnector getInstance() throws SQLException{
        if(instance == null){
            instance = new DatabaseConnector();
        }
        return instance;
    }

    public boolean connection(String username, String password) throws SQLException{
        String url = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
        connector = DriverManager.getConnection(url, username, password);
        return true;
    }
    public void quit() throws SQLException{
        if(connector == null){
            return;
        }
        connector.close();
    }

}
