 ## Class Setup
 
        Account
		String username; - Used as the unique identifier for all accounts
		String name;
		String email;
		String phoneNumber; - Formatted 'XXX-XXX-XXXX'
		String acctType; - Three types are 'CITIZEN', 'POLICE', or 'TOW'
		boolean ticketEmail; - account setting to receive emails for new tickets
		boolean helpEmail; - account setting to receive emails for new help neededs
		boolean responseEmail; - account setting to receive emails for responses to your messages
	
	
	Ticket
		String ticketNumber;  - Used as the unique identifier for all tickets
		String plateNumber;
		String plateState; - Use state abreviations such as NY or IN
		String time; - Formatted time style 'hh:mm'
		String date; - Formatted 'mm/dd/yyyy'
		String reason;
		String towAddress; - Use null if the car is not being towed
	
	Vehicle
		String username; - Should be the username of the vehicle's owner
		String carID; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE* Unique identifier used for all vehicles
		String plateNumber;
		String plateState;
		String make;
		String model;
		String year;
		String color;
	
	LotInfo
		long x1;
		long y1;
		long x2;
		long y2;
		String parkingPasses; - Formatted as a comma separated list of valid parking passes, ex. "A,B"
		String timeRestrictions; - Formatted as "XX:XX(a/p).m.-XX:XX(a/p).m."
		String color; - If there is no color, should be set to "none"
	
	Message
		long messageID; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, auto generated
		String username; - Username of the person that posts the message
		String message;
		boolean helpNeeded;
		long votes; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, auto set to 0 at start
		Date timePosted - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, automatically set
		boolean special; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, automatically set
	
	Comment
		String username; - Username of the person that posts the comment
		String comment;
		long parent; - Should be the ID of the message it is a comment under
	
## API Functions and Call Paths

Tickets:
	Entity addTicket(Ticket t, String username) - Adds a new ticket to the datastore (See the ticket class for names of fields)
     	   POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addTicket/USERNAME
      	   	
      	ArrayList<Ticket> getTickets(String username) - Get the tickets for the current user based off username
      	   GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/ticketcollection/USERNAME
      	   	
      	
      	ArrayList<Ticket> getAllTickets(String Username) - Returns all tickets in the datastore
      	   POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/ticketcollection/USERNAME
      	   	
      	void deleteTicket(String ticketNumber) - Deletes the given ticket
      	   DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/ticket/TICKETNUMBER
      	   	
      	void editTicket(Ticket t) - Edits a ticket in the datastore (Ticket number must exist!)
      	   POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/editTicket
      
Vehicles:
         
       	Entity addVehicle(Vehicle v) - Adds a new vehicle to the datastore (See the Vehicle class for field names)
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addVehicle
         		
       	ArrayList<Vehicle> getVehicles(String username) - Gets a list of the vehicles for the given username
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/vehiclecollection/USERNAME
         		
       	void deleteVehicle(long carID) - Deletes the vehicle with the given ID
       		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/vehicle/CARID
         		
       	void editVehicle(Vehicle v, String username) - Edits the information attached to the vehicle *VehicleID Must Be Given*
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/editVehicle
         		
Accounts:
         
       	Entity addAccount(Account a) - Adds a new account to the datastore (See account class for field names)
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addAccount
       	
       	void deleteAccount(String username) - Deletes the account with the given username and all vehicles associated
       		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/account/USERNAME
         	
       	void editAccount(Account a) - Edits the information attached to the account *Account must already exist*
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/editAccount
         	
       	Entity getAccount(String username) - Returns the account entity for the given username
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/entity/USERNAME
         		
Messages:
      	Entity addMessage(Message m) - Adds a new message to the datastore (See message class for field names)
      		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addMessage
        		
       	ArrayList<Message> getMessagePage(long page) - Gets the given page of messages
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/messagecollection/PAGE
        		
       	ArrayList<Message> getMyMessages(long page, String username) - Gets the given page of messages for the user
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/messagecollection/PAGE/USERNAME
        		
       	ArrayList<Message> getMessageVotePage(long page, boolean descending) - Returns given page of messages sorted by votes
       									     - Returns descending order if descending=true
       		PUT Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/messagecollection/PAGE/DESCENDING
        		
       	ArrayList<Message> getHelpNeeded(long page) - Gets the given page of helpNeeded messages
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/messagecollection/PAGE
       		
        Entity upvote(long messageID) - Adds one to the vote count of the given messageID
        	POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/upvote/MESSAGEID
        	
        Entity downvote(long messageID) - Subtracts one from the vote count of the given messageID
        				- Message is deleted if vote count reaches -30
        	POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/downvote/MESSAGEID
        	
       	void deleteMessage(long messageID) - Deletes the messages and all associated comments
       		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/message/MESSAGEID
       		
Comments:
       	Entity addComment(Comment c) - Adds a new comment to the datastore (See the comment class for field names)
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addComment
        	
       	ArrayList<Comment> getComments(long messageID) - Gets all comments for a given message
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/commentcollection/MESSAGEID
       		
LotInfo:
	Entity addLotInfo(LotInfo l) - Adds a new lot info to the datastore (See the lotInfo class for field names)
       		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addLotInfo
       		
       	ArrayList<LotInfo> getLotInfo() - Gets all lotInfo
       		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/lotinfocollection
       	
       	void deleteColoredLotInfo() - Deletes all colored lotInfo	
       		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/coloredlotinfo
