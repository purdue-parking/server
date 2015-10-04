package com.server.PurdueParking;

public class Vehicle {
	public String owner;
	public int carID;
	public String plateNum;
	public String plateState;
	public String make;
	public String model;
	public int year;
	public String color;
	
	public Vehicle(){}

	public Vehicle(String owner, int carID, String plateNum, String plateState, String make, String model, int year, String color){
		this.owner = owner;
		this.carID = carID;
		this.plateNum = plateNum;
		this.plateState = plateState;
		this.make = make;
		this.model = model;
		this.year = year;
		this.color = color;
	}
	
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
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

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	};
}
