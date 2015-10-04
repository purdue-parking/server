package com.server.PurdueParking;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.User;
import com.google.appengine.api.datastore.Key;

import java.util.ArrayList;

import javax.inject.Named;

@Api(
		name = "purdueParking",
		version = "1",
		clientIds = {Constants.WEB_CLIENT_ID},
		audiences = {Constants.ANDROID_AUDIENCE}
		)
public class Functions {
	
	static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	
	@ApiMethod(name = "ticket.add", httpMethod = "post")
	public Key createTicket(Ticket t){
		Entity ticket = new Entity("Ticket", t.getTicketNum());
		ticket.setProperty("Ticket Number", t.getTicketNum());
		ticket.setProperty("License Plate Number", t.getPlateNum());
		ticket.setProperty("License Plate State", t.getPlateState());
		ticket.setProperty("Time", t.getTime());
		ticket.setProperty("Date", t.getDate());
		ticket.setProperty("Reason", t.getReason());
		ticket.setProperty("Tow Company Address", t.getTowAddress());
		return datastore.put(ticket);
	}
/*	
	public void getTickets(){

	}

	public void getVehicles(){

	}
*/	
	@ApiMethod(name = "vehicle.add", httpMethod = "post")
	public Key addVehicle(Vehicle v){
		Entity vehicle = new Entity("Vehicle", v.getCarID());
		vehicle.setProperty("Car ID", v.getCarID());
		vehicle.setProperty("Owner", v.getOwner());
		vehicle.setProperty("License Plate Number", v.getPlateNum());
		vehicle.setProperty("License Plate State", v.getPlateState());
		vehicle.setProperty("Make", v.getMake());
		vehicle.setProperty("Model", v.getModel());
		vehicle.setProperty("Year", v.getYear());
		vehicle.setProperty("Color", v.getColor());
		return datastore.put(vehicle);
	}
/*
	public void deleteVehicle(){

	}

	//@ApiMethod(name = "vehicle.edi", httpMethod = "post")
	public void editVehicle(){

	}
*/
	@ApiMethod(name = "account.add", httpMethod = "post")
	public Key addAccount(Account a){
		Entity account = new Entity("Account", a.getUsername());
		account.setProperty("Username", a.getUsername());
		account.setProperty("Name", a.getName());
		account.setProperty("Email", a.getEmail());
		account.setProperty("Phone Number", a.getPhoneNumber());
		account.setProperty("Account Type", a.getAcctType());
		return datastore.put(account);
	}
/*
	public void deleteAccount(){

	}

	//@ApiMethod(name = "account.edit", httpMethod = "post")
	public void editAccount(){

	}*/
}
