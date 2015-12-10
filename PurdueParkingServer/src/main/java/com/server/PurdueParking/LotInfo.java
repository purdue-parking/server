package com.server.PurdueParking;

public class LotInfo {
	public long x1;
	public long y1;
	public long x2;
	public long y2;
	public String parkingPasses;
	public String timeRestrictions;
	public String color;
	
	public LotInfo(){}
	
	public LotInfo(long x1, long y1, long x2, long y2, String parkingPasses, String timeRestrictions, String color){
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.parkingPasses = parkingPasses;
		this.timeRestrictions = timeRestrictions;
		this.color = color;
	}

	public long getX1() {
		return x1;
	}

	public void setX1(long x1) {
		this.x1 = x1;
	}

	public long getY1() {
		return y1;
	}

	public void setY1(long y1) {
		this.y1 = y1;
	}

	public long getX2() {
		return x2;
	}

	public void setX2(long x2) {
		this.x2 = x2;
	}

	public long getY2() {
		return y2;
	}

	public void setY2(long y2) {
		this.y2 = y2;
	}

	public String getParkingPasses() {
		return parkingPasses;
	}

	public void setParkingPasses(String parkingPasses) {
		this.parkingPasses = parkingPasses;
	}

	public String getTimeRestrictions() {
		return timeRestrictions;
	}

	public void setTimeRestrictions(String timeRestrictions) {
		this.timeRestrictions = timeRestrictions;
	}
	
	public String getColor(){
		return color;
	}
	
	public void setColor(String color){
		this.color = color;
	}
}