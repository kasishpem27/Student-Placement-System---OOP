package dbconnection;

import java.sql.*;



public class DBConnection {
	
	static final String DB_URL = "jdbc:mysql://localhost:3306/studentplacementsystem";
	static final String username = "root";
	static final String password = "12345678";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("MySQL JDBC Driver not found", e);
		}
		
	}
	
		
	public static Connection getConnection() throws SQLException{	
		
		return DriverManager.getConnection(DB_URL, username, password);
	}
	
	
	
}

