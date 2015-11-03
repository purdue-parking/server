 ## Class Setup
 
        Account
		String username; - Used as the unique identifier for all accounts
		String name;
		String email;
		String phoneNumber; - Formatted 'XXX-XXX-XXXX'
		String acctType; - Three types are 'CITIZEN', 'POLICE', or 'TOW'
	
	
	Ticket
		String ticketNumber;  - Used as the unique identifier for all tickets
		String plateNumber;
		String plateState; - Use state abreviations such as NY or IN
		String time; - Formatted time style 'hh:mm'
		String date; - Formatted 'mm/dd/yyyy'
		String reason;
		String towAddress; - Use 'N/A' if the car is not being towed
	
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
		long[2] coordinate1; - coordinate[0] = x : coordinate[1] = y
		long[2] coordinate2;
		String parkingPasses; - Formatted as a comma separated list of valid parking passes, ex. "A,B"
		String timeRestrictions; - Formatted as "XX:XX(a/p).m.-XX:XX(a/p).m."
	
	Message
		long messageID; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, auto generated
		String username; - Username of the person that posts the message
		String message;
		boolean helpNeeded;
		long votes; - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, auto set to 0 at start
		boolean resolved;-set to true if helpNeeded is false
		Date timePosted - *NOT PASSED AS AN ARGUMENT WHEN ADDING A NEW MESSAGE*, automatically set
	
	Comment
		String username; - Username of the person that posts the comment
		String message;
		long parent; - Should be the ID of the message it is a comment under
	
## API Functions and Call Paths

	Tickets:
	
      	   Entity addTicket(Ticket t) - Adds a new ticket to the datastore (See the ticket class for names of fields)
      	   	POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addTicket
      	   	
      	   ArrayList<Ticket> getTickets(String username) - Get the tickets for the current user based off username
      	   	GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/ticketcollection/USERNAME
      	   	
      	   void deleteTicket(String ticketNumber) - Deletes the given ticket
      	   	DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/ticket/TICKETNUMBER
      
        Vehicles:
         
         	Entity addVehicle(Vehicle v) - Adds a new vehicle to the datastore (See the Vehicle class for field names)
         		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addVehicle
         		
         	ArrayList<Vehicle> getVehicles(String username) - Gets a list of the vehicles for the given username
         		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/vehiclecollection/USERNAME
         		
         	void deleteVehicle(long carID) - Deletes the vehicle with the given ID
         		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/vehicle/CARID
         		
         	void editVehicle(Vehicle v) - Edits the information attached to the vehicle *VehicleID Must Be Given*
         		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/editVehicle
         		
        Accounts:
         
         	Entity addAccount(Account a) - Adds a new account to the datastore (See account class for field names)
         		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addAccount
         	
         	void deleteAccount(String username) - Deletes the account with the given username and all vehicles associated
         		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/account/USERNAME
         	
         	void editAccount(Account a) - Edits the information attached to the account *Account must already exist*
         		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/editAccount
         	
         	Entity getAccount(String username) - Returns the account entity for the given username
         		GET Call Path: ??
         		
        Messages:
        	Entity addMessage(Message m) - Adds a new message to the datastore (See message class for field names)
        		POST Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/addMessage
        		
        	ArrayList<Message> getMessagePage(long page) - Gets the given page of messages
        		GET Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/messagecollection/PAGE
        	
        	void deleteMessage(long messageID) - Deletes the messages and all associated comments
        		DELETE Call Path: https://purdue-parking.appspot.com/_ah/api/purdueParking/1/message/MESSAGEID
  	
