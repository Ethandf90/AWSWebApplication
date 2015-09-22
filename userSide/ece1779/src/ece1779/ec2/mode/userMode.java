package ece1779.ec2.mode;

public class userMode {

	private int id;
	private String login;
	private String password;
	
	public userMode() {
		// TODO Auto-generated constructor stub
	}
	public userMode(String login, String password) {
		this.login = login;
		this.password = password;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
