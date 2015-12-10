package com.server.PurdueParking;

public class Ticket {
	public String ticketNumber;
	public String plateNumber;
	public String plateState;
	public String time;
	public String date;
	public String reason;
	public String towAddress;
	
	public Ticket(){}
	
	public Ticket(String ticketNumber, String plateNumber, String plateState, String time, String date, String reason, String towAddress){
		this.ticketNumber = ticketNumber;
		this.plateNumber = plateNumber;
		this.plateState = plateState;
		this.time = time;
		this.date = date;
		this.reason = reason;
		this.towAddress = towAddress;
	}
	
	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getPlateNumber() {
		return plateNumber;
	}

	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
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