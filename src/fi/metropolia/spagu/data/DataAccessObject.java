package fi.metropolia.spagu.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataAccessObject {

	protected static Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost/weegee_visit", "root",
                    "");
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return connection;	
	}

	protected static void close(Statement statement, Connection connection) {
		close(null, statement, connection);
	}

	protected static void close(ResultSet rs, Statement statement,
			Connection connection) {
		try {
			if (rs != null)
				rs.close();
			if (statement != null)
				statement.close();
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
}
