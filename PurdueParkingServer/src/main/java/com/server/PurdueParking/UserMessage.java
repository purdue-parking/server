package com.server.PurdueParking;

import java.util.Date;

public class UserMessage {
	public long messageID;
	public String username;
	public String message;
	public long votes;
	public boolean helpNeeded;
	public Date timePosted;
	public boolean special;
	
	public UserMessage(){}
	
	public UserMessage(String username, String message, boolean helpNeeded){
		this.username = username;
		this.message = message;
		votes = 0;
		this.helpNeeded = helpNeeded;
	}
	
	public long getMessageID(){
		return messageID;
	}
	
	public void setMessageID(long messageID){
		this.messageID = messageID;
	}
	
	public Date getTimePosted(){
		return timePosted;
	}
	
	public void setTimePosted(Date timePosted){
		this.timePosted = timePosted;
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

	public long getVotes() {
		return votes;
	}

	public void setVotes(long votes) {
		this.votes = votes;
	}

	public boolean isHelpNeeded() {
		return helpNeeded;
	}

	public void setHelpNeeded(boolean helpNeeded) {
		this.helpNeeded = helpNeeded;
	}

	public boolean isSpecial() {
		return special;
	}

	public void setSpecial(boolean special) {
		this.special = special;
	}
}