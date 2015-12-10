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

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;




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
	public Entity addTicket(Ticket t, @Named("username") String username){
		Entity a = getAccount(username);
		String accountType = (String) a.getProperty("accountType");
		if(accountType.equals("CITIZEN")){
			throw new IllegalArgumentException("Invalid addTicket call - user doesn't have proper permissions");
		} else {
			Entity ticket = new Entity("Ticket", t.getTicketNumber());
			ticket.setProperty("ticketNumber", t.getTicketNumber());
			ticket.setProperty("plateNumber", t.getPlateNumber());
			ticket.setProperty("plateState", t.getPlateState());
			ticket.setProperty("time", t.getTime());
			ticket.setProperty("date", t.getDate());
			ticket.setProperty("reason", t.getReason());
			ticket.setProperty("towAddress", t.getTowAddress());
			datastore.put(ticket);
			sendTicketEmail(t.getPlateState(), t.getPlateNumber());
			return ticket;
		}
	}

	@ApiMethod(name = "ticket.getAll", httpMethod = "post")
	public ArrayList<Ticket> getAllTickets(@Named("username") String username){
		Entity a = getAccount(username);
		String accountType = (String) a.getProperty("accountType");
		if(accountType.equals("CITIZEN")){
			throw new IllegalArgumentException("Invalid addTicket call - user doesn't have proper permissions");
		} else {
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
		addTicket(t, null);
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
		Filter user = new FilterPredicate("username",
                FilterOperator.EQUAL,
                a.getUsername());
		
		Query q = new Query("Account").setFilter(user);
		PreparedQuery pq = datastore.prepare(q);
		
		if(pq.asSingleEntity() != null){
			throw new IllegalArgumentException("Username already taken!");
		} else {
			Entity account = new Entity("Account", a.getUsername());
			account.setProperty("username", a.getUsername());
			account.setProperty("password", a.getPassword());
			account.setProperty("name", a.getName());
			account.setProperty("email", a.getEmail());
			account.setProperty("phoneNumber", a.getPhoneNumber());
			account.setProperty("accountType", a.getAccountType());
			account.setProperty("ticketEmail", a.isTicketEmail());
			account.setProperty("helpEmail", a.isHelpEmail());
			account.setProperty("responseEmail", a.isResponseEmail());
			datastore.put(account);
			return account;
		}
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
	public Entity addMessage(UserMessage m){
		Query query = new Query("Message").addSort("messageID", SortDirection.DESCENDING);
		long count = 0;
		for(Entity e : datastore.prepare(query).asIterable()){
			count = (long) e.getProperty("messageID");
			break;
		}
		Entity message = new Entity("Message", count+1);
		m.setTimePosted(new Date());
		message.setProperty("messageID", count+1);
		message.setProperty("username", m.getUsername());
		message.setProperty("message", m.getMessage());
		message.setProperty("votes", m.getVotes());
		message.setProperty("helpNeeded", m.isHelpNeeded());
		message.setProperty("timePosted", m.getTimePosted());
		Entity user = getAccount(m.getUsername());
		String accountType = (String) user.getProperty("accountType");
		if(accountType.equals("POLICE")){
			message.setProperty("special", true);
		} else {
			message.setProperty("special", false);
		}
		datastore.put(message);
		if(m.isHelpNeeded()){
			sendHelpEmail(m);
		}
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

	public ArrayList<UserMessage> getSpecialMessage(){	
		ArrayList<UserMessage> messages = new ArrayList<UserMessage>();
		Filter special = new FilterPredicate("special",
				FilterOperator.EQUAL,
				true);
		Query q = new Query("Message").setFilter(special);
		PreparedQuery pq = datastore.prepare(q);
		for(Entity e : pq.asIterable()){
				UserMessage m = new UserMessage();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				m.setSpecial(true);
				messages.add(m);
		}
		return messages;
	}
	
	@ApiMethod(name = "message.getPage", httpMethod = "get")
	public ArrayList<UserMessage> getMessagePage(@Named("page") long page){	
		ArrayList<UserMessage> messages = new ArrayList<UserMessage>();
		Filter special = new FilterPredicate("special",
				FilterOperator.EQUAL,
				false);
		Query q = new Query("Message").setFilter(special).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		if(page == 1){
			messages = getSpecialMessage();
		} else {
			messages = new ArrayList<UserMessage>();
		}
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				UserMessage m = new UserMessage();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				m.setSpecial((boolean) e.getProperty("special"));
				messages.add(m);
				if(messages.size() == 20)
					return messages;
			}
			count++;
		}
		return messages;
	}

	@ApiMethod(name = "message.getVotePage", httpMethod = "put")
	public ArrayList<UserMessage> getMessageVotePage(@Named("page") long page, @Named("descending") boolean descending){	
		ArrayList<UserMessage> messages = new ArrayList<UserMessage>();
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
				UserMessage m = new UserMessage();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				m.setSpecial((boolean) e.getProperty("special"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}

	@ApiMethod(name = "message.getMine", httpMethod = "get")
	public ArrayList<UserMessage> getMyMessages(@Named("page") long page, @Named("username") String username){	
		ArrayList<UserMessage> messages = new ArrayList<UserMessage>();
		Filter owner = new FilterPredicate("username",
				FilterOperator.EQUAL,
				username);

		Query q = new Query("Message").setFilter(owner).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				UserMessage m = new UserMessage();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				m.setSpecial((boolean) e.getProperty("special"));
				messages.add(m);
			}
			count++;
		}
		return messages;
	}

	@ApiMethod(name = "message.getHelpNeeded", httpMethod = "post")
	public ArrayList<UserMessage> getHelpNeeded(@Named("page") long page){	
		ArrayList<UserMessage> messages = new ArrayList<UserMessage>();	
		Filter help = new FilterPredicate("helpNeeded",
				FilterOperator.EQUAL,
				true);
		Query q = new Query("Message").setFilter(help).addSort("messageID", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		int count = 1;
		for(Entity e : pq.asIterable()){
			if(count > ((page-1) * 20) && count <= (page * 20)){
				UserMessage m = new UserMessage();
				m.setMessageID((long) e.getProperty("messageID"));
				m.setUsername((String) e.getProperty("username"));
				m.setMessage((String) e.getProperty("message")); 
				m.setVotes((long) e.getProperty("votes"));
				m.setHelpNeeded((boolean) e.getProperty("helpNeeded"));
				m.setTimePosted((Date) e.getProperty("timePosted"));
				m.setSpecial((boolean) e.getProperty("special"));
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
		sendResponseEmail(c);
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
	
	@ApiMethod(name = "lotInfo.add", httpMethod = "post")
	public Entity addLotInfo(LotInfo l){
		Entity lotInfo = new Entity("LotInfo");
		lotInfo.setProperty("x1", l.getX1());
		lotInfo.setProperty("y1", l.getY1());
		lotInfo.setProperty("x2", l.getX2());
		lotInfo.setProperty("y2", l.getY2());
		lotInfo.setProperty("parkingPasses", l.getParkingPasses());
		lotInfo.setProperty("timeRestrictions", l.getTimeRestrictions());
		lotInfo.setProperty("color", l.getColor());
		datastore.put(lotInfo);
		return lotInfo;
	}
	
	@ApiMethod(name = "lotInfo.get", httpMethod = "get")
	public ArrayList<LotInfo> getLotInfo(){
		ArrayList<LotInfo> lotInfo = new ArrayList<LotInfo>();	
		Query q = new Query("LotInfo");
		PreparedQuery pq = datastore.prepare(q);
		for(Entity e : pq.asIterable()){
			LotInfo l = new LotInfo();
			l.setX1((long) e.getProperty("x1"));
			l.setY1((long) e.getProperty("y1"));
			l.setX2((long) e.getProperty("x2"));
			l.setY2((long) e.getProperty("y2"));
			l.setParkingPasses((String) e.getProperty("parkingPasses"));
			l.setTimeRestrictions((String) e.getProperty("timeRestrictions"));
			l.setColor((String) e.getProperty("color")); 
			lotInfo.add(l);
		}
		return lotInfo;
	}
	
	@ApiMethod(name = "lotInfo.delete", httpMethod = "delete")
	public void deleteLotInfo(@Named("x1") long x1, @Named("y1") long y1, @Named("x2") long x2, @Named("y2") long y2){
		Filter c1 = new FilterPredicate("x1",
				FilterOperator.EQUAL,
				x1);
		Filter c2 = new FilterPredicate("y1",
				FilterOperator.EQUAL,
				y1);
		Filter c3 = new FilterPredicate("x2",
				FilterOperator.EQUAL,
				x2);
		Filter c4 = new FilterPredicate("y2",
				FilterOperator.EQUAL,
				y2);
		Filter coordinates = CompositeFilterOperator.and(c1, c2, c3, c4);
		Query q = new Query("LotInfo").setFilter(coordinates);
		PreparedQuery pq = datastore.prepare(q);
		datastore.delete(pq.asSingleEntity().getKey());
	}
	
	public void sendTicketEmail(@Named("plateState") String plateState, @Named("plateNumber") String plateNumber){
		Filter number = new FilterPredicate("plateNumber", FilterOperator.EQUAL, plateNumber);
		Filter state = new FilterPredicate("plateState", FilterOperator.EQUAL, plateState);
		Filter plateFilter = CompositeFilterOperator.and(number, state);
		Query q = new Query("Vehicle").setFilter(plateFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity v = pq.asSingleEntity();
		Entity a = getAccount((String) v.getProperty("username"));
		boolean ticketEmail = (boolean) a.getProperty("ticketEmail");
		if(ticketEmail){
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			String msgBody = "Hello " + a.getProperty("username") + ",\n\n" + 
					"You have received a new ticket for your vehicle with the following information:\n\n" + 
					"License Plate Number: " + plateNumber + "\n" +
					"License Plate State: " + plateState + "\n\n" +
					"You may want to move your vehicle.\n\n" +
					"-Purdue Parking Assistant Team";

			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("mhoward20158@gmail.com", "Purdue Parking"));
				msg.addRecipient(Message.RecipientType.TO,
						new InternetAddress((String) a.getProperty("email"), "User"));
				msg.setSubject("You have received a new ticket!");
				msg.setText(msgBody);
				Transport.send(msg);
			} catch (MessagingException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void sendHelpEmail(UserMessage m){
		Query q = new Query("Account");
		PreparedQuery pq = datastore.prepare(q);
		for(Entity a : pq.asIterable()){
			boolean helpEmail = (boolean) a.getProperty("helpEmail");
			if(helpEmail){
				Properties props = new Properties();
				Session session = Session.getDefaultInstance(props, null);
	
				String msgBody = "Hello " + a.getProperty("username") + ",\n\n" + 
						"Somebody has posted a new message and they need help!\n\n" + 
						m.getUsername() + " said: " + m.getMessage() + "\n\n" +
						"-Purdue Parking Assistant Team";
	
				try {
					Message msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress("mhoward20158@gmail.com", "Purdue Parking"));
					msg.addRecipient(Message.RecipientType.TO,
							new InternetAddress((String) a.getProperty("email"), "User"));
					msg.setSubject("Somebody needs help!");
					msg.setText(msgBody);
					Transport.send(msg);
				} catch (MessagingException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendResponseEmail(Comment c){
		Filter id = new FilterPredicate("messageID", FilterOperator.EQUAL, c.getParent());
		Query q = new Query("Message").setFilter(id);
		PreparedQuery pq = datastore.prepare(q);
		Entity m = pq.asSingleEntity();
		Entity a = getAccount((String) m.getProperty("username"));
		boolean responseEmail = (boolean) a.getProperty("responseEmail");
		if(responseEmail){
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			String msgBody = "Hello " + a.getProperty("username") + ",\n\n" + 
					"You have received a comment on one of your messages!\n\n" + 
					"Your Message: " + m.getProperty("message") + "\n\n" +
					c.getUsername() + "'s comment: " + c.getComment() + "\n\n" +
					"-Purdue Parking Assistant Team";

			try {
				Message msg = new MimeMessage(session);
				msg.setFrom(new InternetAddress("mhoward20158@gmail.com", "Purdue Parking"));
				msg.addRecipient(Message.RecipientType.TO,
						new InternetAddress((String) a.getProperty("email"), "User"));
				msg.setSubject("Your message got a response!");
				msg.setText(msgBody);
				Transport.send(msg);
			} catch (MessagingException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}
}