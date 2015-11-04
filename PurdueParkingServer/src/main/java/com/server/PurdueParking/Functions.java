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
import com.google.appengine.api.datastore.Query.SortDirection;

import java.util.ArrayList;
import java.util.Date;

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
	public Entity addTicket(Ticket t){
			Entity ticket = new Entity("Ticket", t.getTicketNumber());
			ticket.setProperty("ticketNumber", t.getTicketNumber());
			ticket.setProperty("plateNumber", t.getPlateNumber());
			ticket.setProperty("plateState", t.getPlateState());
			ticket.setProperty("time", t.getTime());
			ticket.setProperty("date", t.getDate());
			ticket.setProperty("reason", t.getReason());
			ticket.setProperty("towAddress", t.getTowAddress());
			datastore.put(ticket);
			return ticket;
	}
	
	@ApiMethod(name = "ticket.getAll", httpMethod = "get")
	public ArrayList<Ticket> getAllTickets(){
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		Query ticks = new Query("Ticket");
		PreparedQuery ticketQuery = datastore.prepare(ticks);
		for(Entity e : ticketQuery.asIterable()){
			Ticket t = new Ticket();
			t.setTicketNumber((String) e.getProperty("ticketNumber"));
			t.setPlateNumber((String) e.getProperty("plateNumber"));
			t.setPlateState((String) e.getProperty("plateState"));
			t.setTime((String) e.getProperty("time"));
			t.setDate((String) e.getProperty("date"));
			t.setReason((String) e.getProperty("reason"));
			t.setTowAddress((String) e.getProperty("towAddress"));
			tickets.add(t);
		}
		return tickets;
	}
	
	@ApiMethod(name = "ticket.getUser", httpMethod = "get")
	public ArrayList<Ticket> getTickets(@Named("username") String username){
		ArrayList<Ticket> tickets = new ArrayList<Ticket>();
		ArrayList<Entity> vehicles = getVehiclesEntity(username);

		for(Entity v : vehicles){
			Filter plateNum = new FilterPredicate("plateNumber", FilterOperator.EQUAL, v.getProperty("plateNumber"));
			Filter plateState = new FilterPredicate("plateState", FilterOperator.EQUAL, v.getProperty("plateState"));
			Filter plateFilter = CompositeFilterOperator.and(plateNum, plateState);
			Query ticketsForOwner = new Query("Ticket").setFilter(plateFilter);
			PreparedQuery ticketQuery = datastore.prepare(ticketsForOwner);
			for(Entity e : ticketQuery.asIterable()){
				Ticket t = new Ticket();
				t.setTicketNumber((String) e.getProperty("ticketNumber"));
				t.setPlateNumber((String) e.getProperty("plateNumber"));
				t.setPlateState((String) e.getProperty("plateState"));
				t.setTime((String) e.getProperty("time"));
				t.setDate((String) e.getProperty("date"));
				t.setReason((String) e.getProperty("reason"));
				t.setTowAddress((String) e.getProperty("towAddress"));
				tickets.add(t);
			}
		}		
		return tickets;
	}
	
	@ApiMethod(name = "ticket.edit", httpMethod = "post")
	public void editTicket(Ticket t){
		deleteTicket(t.getTicketNumber());
		addTicket(t);
	}
	
	@ApiMethod(name = "ticket.delete", httpMethod = "delete")
	public void deleteTicket(@Named("ticketNumber") String ticketNumber){
		Filter ticket = new FilterPredicate("ticketNumber",
                FilterOperator.EQUAL,
                ticketNumber);
		
		Query q = new Query("Ticket").setFilter(ticket);
		PreparedQuery pq = datastore.prepare(q);
		datastore.delete(pq.asSingleEntity().getKey());
	}

	@ApiMethod(name = "vehicle.get", httpMethod = "get")
	public ArrayList<Vehicle> getVehicles(@Named("username") String username){
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		ArrayList<Entity> entities = getVehiclesEntity(username);
		for(Entity e : entities){
			Vehicle v = new Vehicle();
			v.setCarID((long) e.getProperty("carID"));
			v.setColor((String) e.getProperty("color"));
			v.setMake((String) e.getProperty("make"));
			v.setModel((String) e.getProperty("model"));
			v.setUsername((String) e.getProperty("username"));
			v.setPlateNumber((String) e.getProperty("plateNumber"));
			v.setPlateState((String) e.getProperty("plateState"));
			v.setYear((String) e.getProperty("year"));
			vehicles.add(v);
		}
		return vehicles;
	}
	
	private ArrayList<Entity> getVehiclesEntity(String username){
		Filter owner = new FilterPredicate("username",
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
		Query query = new Query("Vehicle").addSort("carID", SortDirection.DESCENDING);
		long count = 0;
		for(Entity e : datastore.prepare(query).asIterable()){
			count = (long) e.getProperty("carID");
			break;
		}
		Entity vehicle = new Entity("Vehicle", count+1);
		vehicle.setProperty("carID", count+1);
		vehicle.setProperty("username", v.getUsername());
		vehicle.setProperty("plateNumber", v.getPlateNumber());
		vehicle.setProperty("plateState", v.getPlateState());
		vehicle.setProperty("make", v.getMake());
		vehicle.setProperty("model", v.getModel());
		vehicle.setProperty("year", v.getYear());
		vehicle.setProperty("color", v.getColor());
		datastore.put(vehicle);	
		return vehicle;
	}

	@ApiMethod(name = "vehicle.delete", httpMethod = "delete")
	public void deleteVehicle(@Named("carID") long carID){
		Filter vehicle = new FilterPredicate("carID",
                FilterOperator.EQUAL,
                carID);
		
		Query q = new Query("Vehicle").setFilter(vehicle);
		PreparedQuery pq = datastore.prepare(q);
		Entity v = pq.asSingleEntity();
		datastore.delete(v.getKey());
	}

	@ApiMethod(name = "vehicle.edit", httpMethod = "post")
	public void editVehicle(Vehicle v){
		Filter owner = new FilterPredicate("carID",
                FilterOperator.EQUAL,
                v.getCarID());
		
		Query q = new Query("Vehicle").setFilter(owner);
		PreparedQuery pq = datastore.prepare(q);
		Entity vehicle = pq.asSingleEntity();
		vehicle.setProperty("carID", v.getCarID());
		vehicle.setProperty("username", v.getUsername());
		vehicle.setProperty("plateNumber", v.getPlateNumber());
		vehicle.setProperty("plateState", v.getPlateState());
		vehicle.setProperty("make", v.getMake());
		vehicle.setProperty("model", v.getModel());
		vehicle.setProperty("year", v.getYear());
		vehicle.setProperty("color", v.getColor());
		datastore.put(vehicle);
	}

	@ApiMethod(name = "account.add", httpMethod = "post")
	public Entity addAccount(Account a){
		Entity account = new Entity("Account", a.getUsername());
		account.setProperty("username", a.getUsername());
		account.setProperty("password", a.getPassword());
		account.setProperty("name", a.getName());
		account.setProperty("email", a.getEmail());
		account.setProperty("phoneNumber", a.getPhoneNumber());
		account.setProperty("accountType", a.getAcctType());
		datastore.put(account);
		return account;
	}
	
	@ApiMethod(name = "account.get", httpMethod = "get")
	public Entity getAccount(@Named("username") String username){	
		Filter owner = new FilterPredicate("username",
                FilterOperator.EQUAL,
                username);
		
		Query q = new Query("Account").setFilter(owner);
		PreparedQuery pq = datastore.prepare(q);
		return pq.asSingleEntity();
	}

	@ApiMethod(name = "account.delete", httpMethod = "delete")
	public void deleteAccount(@Named("username") String username){
		ArrayList<Entity> vehicles = getVehiclesEntity(username);
		for(Entity v : vehicles){
			datastore.delete(v.getKey());
		}
		datastore.delete(getAccount(username).getKey());
	}

	@ApiMethod(name = "account.edit", httpMethod = "post")
	public void editAccount(Account a){		
		datastore.delete(getAccount(a.getUsername()).getKey());
		addAccount(a);
	}
	
	@ApiMethod(name = "account.login", httpMethod = "get")
	public Entity login(@Named("username") String username, @Named("password") String password){	
		Entity account = getAccount(username);
		String pass = (String) account.getProperty("password");
		if(pass.equals(password)){
			return account;
		} else {
			throw new IllegalArgumentException("Invalid password for username: " + username);
		}
	}
	
	@ApiMethod(name = "message.add", httpMethod = "post")
	public Entity addMessage(Message m){
		Query query = new Query("Message").addSort("messageID", SortDirection.DESCENDING);
		long count = 0;
		for(Entity e : datastore.prepare(query).asIterable()){
			count = (long) e.getProperty("messageID");
			break;
		}
		Entity message = new Entity("Message", count+1);
		message.setProperty("messageID", count+1);
		message.setProperty("username", m.getUsername());
		message.setProperty("message", m.getMessage());
		message.setProperty("votes", m.getVotes());
		message.setProperty("helpNeeded", m.isHelpNeeded());
		message.setProperty("resolved", m.isResolved());
		message.setProperty("timePosted", m.getTimePosted());
		datastore.put(message);
		return message;
	}
	
	@ApiMethod(name = "message.delete", httpMethod = "delete")
	public void deleteMessage(@Named("messageID") long messageID){
		Filter id = new FilterPredicate("messageID",
                FilterOperator.EQUAL,
                messageID);		
		Query q = new Query("Message").setFilter(id);
		PreparedQuery pq = datastore.prepare(q);
		datastore.delete(pq.asSingleEntity().getKey());
		deleteComments(messageID);
	}
	
	@ApiMethod(name = "message.getPage", httpMethod = "get")
	public ArrayList<Message> getMessagePage(@Named("page") long page){	
		ArrayList<Message> messages = new ArrayList<Message>();		
		Query q = new Query("Message").addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				Message m = new Message();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setResolved((boolean) e.getProperty("resolved"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}
	
	@ApiMethod(name = "message.getVotePage", httpMethod = "put")
	public ArrayList<Message> getMessageVotePage(@Named("page") long page, @Named("descending") boolean descending){	
		ArrayList<Message> messages = new ArrayList<Message>();
		Query q;
		if(descending){
			q = new Query("Message").addSort("votes", SortDirection.DESCENDING);
		} else {
			q = new Query("Message").addSort("votes", SortDirection.ASCENDING);
		}
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				Message m = new Message();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setResolved((boolean) e.getProperty("resolved"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}
	
	@ApiMethod(name = "message.getMine", httpMethod = "get")
	public ArrayList<Message> getMyMessages(@Named("page") long page, @Named("username") String username){	
		ArrayList<Message> messages = new ArrayList<Message>();
		Filter owner = new FilterPredicate("username",
                FilterOperator.EQUAL,
                username);
		
		Query q = new Query("Message").setFilter(owner).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				Message m = new Message();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setResolved((boolean) e.getProperty("resolved"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}
	
	@ApiMethod(name = "message.getHelpNeeded", httpMethod = "post")
	public ArrayList<Message> getHelpNeeded(@Named("page") long page){	
		ArrayList<Message> messages = new ArrayList<Message>();	
		Filter help = new FilterPredicate("helpNeeded",
                FilterOperator.EQUAL,
                true);
		Query q = new Query("Message").setFilter(help).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				Message m = new Message();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setResolved((boolean) e.getProperty("resolved"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}
	
	@ApiMethod(name = "message.getResolved", httpMethod = "post")
	public ArrayList<Message> getResolved(@Named("page") long page, @Named("resolved") boolean resolved){	
		ArrayList<Message> messages = new ArrayList<Message>();	
		Filter help = new FilterPredicate("helpNeeded",
                FilterOperator.EQUAL,
                true);
		Filter res = new FilterPredicate("resolved",
                FilterOperator.EQUAL,
                resolved);
		Filter resolvedFilter = CompositeFilterOperator.and(help, res);
		Query q = new Query("Message").setFilter(resolvedFilter).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				Message m = new Message();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setResolved((boolean) e.getProperty("resolved"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}
		
	@ApiMethod(name = "message.upvote", httpMethod = "post")
	public Entity upvote(@Named("messageID") long messageID){
		Filter id = new FilterPredicate("messageID",
                FilterOperator.EQUAL,
                messageID);
		
		Query q = new Query("Message").setFilter(id);
		PreparedQuery pq = datastore.prepare(q);
		Entity e = pq.asSingleEntity();
		long votes = (long) e.getProperty("votes");
		votes++;
		e.setProperty("votes", votes);
		datastore.put(e);
		return e;
	}
	
	@ApiMethod(name = "message.downvote", httpMethod = "post")
	public Entity downvote(@Named("messageID") long messageID){
		Filter id = new FilterPredicate("messageID",
                FilterOperator.EQUAL,
                messageID);
		
		Query q = new Query("Message").setFilter(id);
		PreparedQuery pq = datastore.prepare(q);
		Entity e = pq.asSingleEntity();
		long votes = (long) e.getProperty("votes");
		votes--;
		if(votes <= -30){
			deleteMessage((long) e.getProperty("messageID"));
			return null;
		} else {
			e.setProperty("votes", votes);
			datastore.put(e);
			return e;
		}
	}

	@ApiMethod(name = "comment.add", httpMethod = "post")
	public Entity addComment(Comment c){
		Entity comment = new Entity("Comment");
		comment.setProperty("username", c.getUsername());
		comment.setProperty("comment", c.getComment());
		comment.setProperty("parent", c.getParent());
		datastore.put(comment);
		return comment;
	}
	
	@ApiMethod(name = "comment.get", httpMethod = "get")
	public ArrayList<Comment> getComments(@Named("messageID") long messageID){
		ArrayList<Comment> comments = new ArrayList<Comment>();
		Filter parent = new FilterPredicate("parent",
                FilterOperator.EQUAL,
                messageID);		
		Query q = new Query("Comment").setFilter(parent);
		PreparedQuery pq = datastore.prepare(q);
		for(Entity e : pq.asIterable()){
			Comment c = new Comment();
			c.setComment((String) e.getProperty("comment"));
			c.setParent((long) e.getProperty("parent")); 
			c.setUsername((String) e.getProperty("username")); 
			comments.add(c);
		}
		return comments;
	}
	
	public void deleteComments(@Named("messageID") long messageID){
		Filter id = new FilterPredicate("parent",
                FilterOperator.EQUAL,
                messageID);		
		Query q = new Query("Comment").setFilter(id);
		PreparedQuery pq = datastore.prepare(q);
		for(Entity c : pq.asIterable()){
			datastore.delete(c.getKey());
		}
	}
}
