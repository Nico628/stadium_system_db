
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;


public class DatabaseConnector extends Observable {
    private static DatabaseConnector instance;
    private ResultSet resultSet;
    private Connection connection;

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
        connection = DriverManager.getConnection(url, username, password);
        return true;
        }
    
    public void quit() throws SQLException{
        if(connection == null){
            return;
        }
        connection.close();
    }
    
    // ship SQL to Oracle
    public void SQL2Oracle(String query)  throws SQLException {
		ResultSet resultSet = null;
		Statement statement = connection.createStatement();
		resultSet = statement.executeQuery(query);

		this.resultSet = resultSet;
		if (resultSet != null) {
			setChanged();
			notifyObservers(resultSet);	
		} else {
			throw new SQLException("SQL2Oracle null");
		}
	}
    
    // Pass in table name, Select all tuples from that table
    public void selectWholeTable(String tableName) throws SQLException {
    	if (tableName == null)
			return;
    	
		String query = "SELECT * FROM " + tableName;
		SQL2Oracle(query);
	}
    
    public void selectProjectTable(String tableName, String attributes) throws SQLException {
    	if(tableName == null || attributes == null)
    		return;
    	
    	String query = "SELECT " + attributes + " FROM " + tableName;
    	SQL2Oracle(query);
    }
	

}
