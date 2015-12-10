package com.server.PurdueParking;

public class Vehicle {
	public String username;
	public long carID;
	public String plateNumber;
	public String plateState;
	public String make;
	public String model;
	public String year;
	public String color;
	
	public Vehicle(){}

	public Vehicle(String username, String plateNumber, String plateState, String make, String model, String year, String color){
		this.username = username;
		this.plateNumber = plateNumber;
		this.plateState = plateState;
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public long getCarID() {
		return carID;
	}

	public void setCarID(long carID) {
		this.carID = carID;
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

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	};
}