package com.server.PurdueParking;

public class LotInfo {
	public long[] coordinate1 = new long[2];
	public long[] coordinate2 = new long[2];
	public String parkingPasses;
	public String timeRestrictions;
	
	public LotInfo(){}
	
	public LotInfo(long[] coordinate1, long[] coordinate2, String parkingPasses, String timeRestrictions){
		this.coordinate1 = coordinate1;
		this.coordinate2 = coordinate2;
		this.parkingPasses = parkingPasses;
		this.timeRestrictions = timeRestrictions;
	}

	public long[] getCoordinate1() {
		return coordinate1;
	}

	public void setCoordinate1(long[] coordinate1) {
		this.coordinate1 = coordinate1;
	}

	public long[] getCoordinate2() {
		return coordinate2;
	}

	public void setCoordinate2(long[] coordinate2) {
		this.coordinate2 = coordinate2;
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
}
