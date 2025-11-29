package dao;

import model.*;
import java.sql.*;

public class DatabaseConnection {
	private static final String DB_NAME = "protocol-zero";
	private static final String DB_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
	private static final String USN = "root";
	private static final String PASS = "";

	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, USN, PASS);
	}

	public boolean registerUser(String username, String password) {
    	final String sql = "INSERT INTO user(username, password) VALUES(?, ?)";

    	try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      		ps.setString(1, username);
      		ps.setString(2, password); 
      		return ps.executeUpdate() == 1;
    	} catch (SQLIntegrityConstraintViolationException dupe) {
      		return false;
    	} catch (SQLException e) {
      	if (e.getErrorCode() == 1062 || "23000".equals(e.getSQLState())) return false;
      		throw new RuntimeException("DB error saat register: " + e.getMessage(), e);
    	}
  	}

  	public User loginUser(String username, String password) throws SQLException {
	    final String sql = "SELECT user_id, username, password FROM user WHERE username=? AND password=?";
    	try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
	        ps.setString(1, username);
    	    ps.setString(2, password);
        	try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
	                int id = rs.getInt("user_id");
    	            String uss = rs.getString("username");
        	        String pass = rs.getString("password");
            	    return new User(id, uss, pass);
            	}
        	}
    	}
    	return null;
	}
}
