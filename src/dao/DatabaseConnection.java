package dao;

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

  	public int loginUser(String username, String password) {
    	final String sql = "SELECT user_id FROM user WHERE username=? AND password=?";
   		try (Connection c = getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
      		ps.setString(1, username);
      		ps.setString(2, password);
      	try (ResultSet rs = ps.executeQuery()) {
        	if (rs.next()) return rs.getInt("user_id");
      	}
    	} catch (SQLException e) {
      		throw new RuntimeException("DB error saat login: " + e.getMessage(), e);
    	}
    	return -1;
  	}

}
