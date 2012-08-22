package Libreria;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Database {
	
	private static Connection connection = null;
	private static Statement statement = null;
	private static ResultSet resultset = null;
	private static String dbName = "taks";
	private static String dbUrl  = "jdbc:mysql://localhost:3306/taks";
	private static String dbUser = "root";
	private static String dbPass = "";
	
	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		Database.connection = connection;
	}

	public static Statement getStatement() {
		return statement;
	}

	public static void setStatement(Statement statement) {
		Database.statement = statement;
	}
	
	public static ResultSet getResultset() {
		return resultset;
	}
	public static void setResultset(ResultSet resultset) {
		Database.resultset = resultset;
	}
	
	public static void connectDBSelect(String QueryVariable) throws SQLException {

		String SQLStatement = QueryVariable;
		String driver = "com.mysql.jdbc.Driver";
		try {
			System.out.println(QueryVariable);
			Class.forName(driver);
			Database.setConnection(connection = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPass));
			Database.setStatement(statement = (Statement) connection.createStatement());
			statement.execute(SQLStatement);
			System.out.println("flag1");
			Database.setResultset((ResultSet) statement.getResultSet());
			System.out.println("flag2");
		
		} catch (Exception error) {
			System.out.println("Error: " + error);
			error.printStackTrace();
		}
	}
	
	public static void connectDBInsert(String QueryVariable) throws SQLException {
		String SQLStatement = QueryVariable;
		String driver = "com.mysql.jdbc.Driver";
		try {
			Class.forName(driver);
			Database.setConnection(connection = (Connection) DriverManager.getConnection(dbUrl, dbUser, dbPass));
			PreparedStatement preparedStatement = (PreparedStatement) connection.prepareStatement(SQLStatement);
			preparedStatement.executeUpdate(SQLStatement);
			preparedStatement.close();
			connection.close();
		} catch (Exception error) {
			System.out.println("Error: " + error);
			error.printStackTrace();
		}
	}
	
	public static String getDbName() {
		return dbName;
	}

	public static void setDbName(String dbName) {
		Database.dbName = dbName;
	}	
}
