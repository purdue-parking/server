package com.server.PurdueParking;

public class Account {
	public String username;
	public String password;
	public String name;
	public String email;
	public String phoneNumber;
	public String accountType;
	public boolean ticketEmail;
	public boolean helpEmail;
	public boolean responseEmail;
	
	public Account(){}
	
	public Account(String username, String password, String name, String email, String phoneNumber, String accountType,
					boolean ticketEmail, boolean helpEmail, boolean responseEmail){
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.accountType = accountType;
		this.ticketEmail = ticketEmail;
		this.helpEmail = helpEmail;
		this.responseEmail = responseEmail;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isTicketEmail() {
		return ticketEmail;
	}

	public void setTicketEmail(boolean ticketEmail) {
		this.ticketEmail = ticketEmail;
	}

	public boolean isHelpEmail() {
		return helpEmail;
	}

	public void setHelpEmail(boolean helpEmail) {
		this.helpEmail = helpEmail;
	}

	public boolean isResponseEmail() {
		return responseEmail;
	}

	public void setResponseEmail(boolean responseEmail) {
		this.responseEmail = responseEmail;
	}
}