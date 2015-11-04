package com.server.PurdueParking;

public class Comment {
	public String username;
	public String comment;
	public long parent;
	
	public Comment(){}

	public Comment(String username, String comment, long parent) {
		this.username = username;
		this.comment = comment;
		this.parent = parent;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}
}
