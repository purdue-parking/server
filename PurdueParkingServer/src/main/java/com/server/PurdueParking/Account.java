package com.server.PurdueParking;

public class Account {
	public String username;
	public String password;
	public String name;
	public String email;
	public String phoneNumber;
	public String accountType;
	
	public Account(){}
	
	public Account(String username, String password, String name, String email, String phoneNumber, String accountType){
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.accountType = accountType;
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

	public String getAccounttType() {
		return accountType;
	}

	public void setAccountType(String acctType) {
		this.accountType = acctType;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}

