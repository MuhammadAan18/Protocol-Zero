package model;

public class User {
	private int user_id;
	private String username;
	private String password;

	public User(int id, String uss, String pass) {
		this.user_id = id;
		this.username = uss;
		this.password = pass;
	}

	public int getUserID() {
		return user_id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
