package com.server.PurdueParking;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
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
	public Entity createTicket(Ticket t){
		Entity ticket = new Entity("Ticket", t.getTicketNum());
		ticket.setProperty("ticketNumber", t.getTicketNum());
		ticket.setProperty("licensePlateNumber", t.getPlateNum());
		ticket.setProperty("licensePlateState", t.getPlateState());
		ticket.setProperty("time", t.getTime());
		ticket.setProperty("date", t.getDate());
		ticket.setProperty("reason", t.getReason());
		ticket.setProperty("towCompanyAddress", t.getTowAddress());
		datastore.put(ticket);
		return ticket;
	}
	
	@ApiMethod(name = "ticket.get", httpMethod = "get")
	public ArrayList<Ticket> getTickets(@Named("username") String username){
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		ArrayList<Entity> vehicles = getVehicles(username);

		for(Entity v : vehicles){
			Filter plateNum = new FilterPredicate("licensePlateNumber", FilterOperator.EQUAL, v.getProperty("licensePlateNumber"));
			Filter plateState = new FilterPredicate("licensePlateState", FilterOperator.EQUAL, v.getProperty("licensePlateState"));
			Filter plateFilter = CompositeFilterOperator.and(plateNum, plateState);
			Query ticketsForOwner = new Query("Ticket").setFilter(plateFilter);
			PreparedQuery ticketQuery = datastore.prepare(ticketsForOwner);
			for(Entity e : ticketQuery.asIterable()){
				Ticket t = new Ticket();
				t.setTicketNum((String) e.getProperty("ticketNumber"));
				t.setPlateNum((String) e.getProperty("licensePlateNumber"));
				t.setPlateState((String) e.getProperty("licensePlateState"));
				t.setTime((String) e.getProperty("time"));
				t.setDate((String) e.getProperty("date"));
				t.setReason((String) e.getProperty("reason"));
				t.setTowAddress((String) e.getProperty("towCompanyAddress"));
				tickets.add(t);
			}
		}		
		return tickets;
	}
	
	@ApiMethod(name = "ticket.delete")
	public void deleteTicket(@Named("ticketNumber") String ticketNumber){
		Key ticketKey = KeyFactory.stringToKey(ticketNumber);
		datastore.delete(ticketKey);
	}

	@ApiMethod(name = "vehicle.get", httpMethod = "get")
	public ArrayList<Entity> getVehicles(@Named("username") String username){
		Filter owner = new FilterPredicate("owner",
                FilterOperator.EQUAL,
                username);
		
		Query q = new Query("Vehicle").setFilter(owner);
		PreparedQuery pq = datastore.prepare(q);
		ArrayList<Entity> vehicles = new ArrayList<Entity>();
		for(Entity v : pq.asIterable()){
			vehicles.add(v);
		}
		return vehicles;
	}
	
	@ApiMethod(name = "vehicle.add", httpMethod = "post")
	public Entity addVehicle(Vehicle v){
		Entity vehicle = new Entity("Vehicle", v.getCarID());
		vehicle.setProperty("carID", v.getCarID());
		vehicle.setProperty("owner", v.getOwner());
		vehicle.setProperty("licensePlateNumber", v.getPlateNum());
		vehicle.setProperty("licensePlateState", v.getPlateState());
		vehicle.setProperty("make", v.getMake());
		vehicle.setProperty("model", v.getModel());
		vehicle.setProperty("year", v.getYear());
		vehicle.setProperty("color", v.getColor());
		datastore.put(vehicle);
		return vehicle;
	}

	@ApiMethod(name = "vehicle.delete")
	public void deleteVehicle(@Named("carID") String carID){
		Key vehicleKey = KeyFactory.stringToKey(carID);
		datastore.delete(vehicleKey);
	}

	@ApiMethod(name = "vehicle.edit", httpMethod = "post")
	public void editVehicle(Vehicle v){
		deleteVehicle(v.getCarID());
		addVehicle(v);
	}

	@ApiMethod(name = "account.add", httpMethod = "post")
	public Entity addAccount(Account a){
		Entity account = new Entity("Account", a.getUsername());
		account.setProperty("username", a.getUsername());
		account.setProperty("name", a.getName());
		account.setProperty("email", a.getEmail());
		account.setProperty("phoneNumber", a.getPhoneNumber());
		account.setProperty("accountType", a.getAcctType());
		datastore.put(account);
		return account;
	}

	@ApiMethod(name = "account.delete")
	public void deleteAccount(@Named("username") String username){
		ArrayList<Entity> vehicles = getVehicles(username);
		for(Entity v : vehicles){
			deleteVehicle((String) v.getProperty("carID"));
		}
		Key userKey = KeyFactory.stringToKey(username);
		datastore.delete(userKey);
	}

	@ApiMethod(name = "account.edit", httpMethod = "post")
	public void editAccount(Account a){
		Key userKey = KeyFactory.stringToKey(a.getUsername());
		datastore.delete(userKey);
		addAccount(a);
	}
}
