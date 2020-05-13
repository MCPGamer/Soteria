package ch.bbw.soteria.model;

/**
 * @author Duarte Goncalves Mendes & Joel Weiss
 * @version 1.0
 */

public class PasswordContext {
	private String domain;
	private String username;
	private String password;
	private long id;
	private long userId;

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
