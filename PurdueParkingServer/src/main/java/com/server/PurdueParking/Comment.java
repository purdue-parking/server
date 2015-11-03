package com.server.PurdueParking;

public class Comment {
	public String username;
	public String message;
	public long parent;
	
	public Comment(){}

	public Comment(String username, String message, long parent) {
		this.username = username;
		this.message = message;
		this.parent = parent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}
}
