package com.server.PurdueParking;

public class Ticket {
	public int ticketNum;
	public String plateNum;
	public String plateState;
	public String time;
	public String date;
	public String reason;
	public String towAddress;
	
	public Ticket(){}
	
	public Ticket(int ticketNum, String plateNum, String plateState, String time, String date, String reason, String towAddress){
		this.ticketNum = ticketNum;
		this.plateNum = plateNum;
		this.plateState = plateState;
		this.time = time;
		this.date = date;
		this.reason = reason;
		this.towAddress = towAddress;
	}
	
	public int getTicketNum() {
		return ticketNum;
	}

	public void setTicketNum(int ticketNum) {
		this.ticketNum = ticketNum;
	}

	public String getPlateNum() {
		return plateNum;
	}

	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}

	public String getPlateState() {
		return plateState;
	}

	public void setPlateState(String plateState) {
		this.plateState = plateState;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getTowAddress() {
		return towAddress;
	}

	public void setTowAddress(String towAddress) {
		this.towAddress = towAddress;
	}
}
