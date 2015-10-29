 ## Class Setup
 
        Account
		String username; - Used as the unique identifier for all accounts
		String name;
		String email;
		String phoneNumber; - Formatted 'XXX-XXX-XXXX'
		String acctType; - Three types are 'CITIZEN', 'POLICE', or 'TOW'
	
	
	Ticket
		String ticketNum;  - Used as the unique identifier for all tickets
		String plateNum;
		String plateState; - Use state abreviations such as NY or IN
		String time; - Formatted time style 'hh:mm'
		String date; - Formatted 'mm/dd/yyyy'
		String reason;
		String towAddress; - Use 'N/A' if the car is not being towed
	
	Vehicle
		String owner; - Should be the username of the vehicles owner
		String carID; - Unique identifier used for all vehicles
		String plateNum;
		String plateState;
		String make;
		String model;
		String year;
		String color;
	
## API Functions and Call Paths

	Tickets:
	
      	   Entity createTicket(Ticket t) - Adds a new ticket to the datastore (See the ticket class for names of fields)
      	                                 - Must be called by a "TOW" or "POLICE" account type
      	   	Call Path: Gapi.client.purdueParking.ticket.add
      	   	
      	   ArrayList<Ticket> getTickets(String username) - Get the tickets for the current user based off username
      	   	Call Path: Gapi.client.purdueParking.ticket.get
      	   	
      	   void deleteTicket(String ticketNumber) - Deletes the given ticket
      	   	Call Path: Gapi.client.purdueParking.ticket.delete
      
        Vehicles:
         
         	Entity addVehicle(Vehicle v) - Adds a new vehicle to the datastore (See the Vehicle class for field names)
         	                             - Must be called by "CITIZEN" account type
         		Call Path: Gapi.client.purdueParking.vehicle.add
         		
         	ArrayList<Vehicle> getVehicles(String username) - Gets a list of the vehicles for the given username
         		Call Path: Gapi.client.purdueParking.vehicle.get
         		
         	void deleteVehicle(String carID) - Deletes the vehicle with the given ID
         		Call Path: Gapi.client.purdueParking.vehicle.delete
         		
         	void editVehicle(Vehicle v) - Edits the information attached to the vehicle *Vehicle must already exist*
         		Call Path: Gapi.client.purdueParking.vehicle.edit
         		
        Accounts:
         
         	Entity addAccount(Account a) - Adds a new account to the datastore (See account class for field names)
         	                             - Must be called by "CITIZEN" account type
         		Call Path: Gapi.client.purdueParking.account.add
         	
         	void deleteAccount(String username) - Deletes the account with the given username and all vehicles associated
         		Call Path: Gapi.client.purdueParking.account.delete
         	
         	void editAccount(Account a) - Edits the information attached to the account *Account must already exist*
         		Call Path: Gapi.client.purdueParking.account.edit
  	
