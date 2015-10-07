 ## Class Setup
 
        Account
		String username; - Used as the unique identifier for all accounts
		String name;
		String email;
		String phoneNumber;
		String acctType; - Three types are 'CITIZEN', 'POLICE', or 'TOW'
	
	
	Ticket
		String ticketNum;  - Used as the unique identifier for all tickets
		String plateNum;
		String plateState;
		String time; - Formatted military time style 'hh:mm'
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
	
      	   createTicket(Ticket t) - Adds a new ticket to the datastore
      	   	Call Path: Gapi.client.purdueParking.ticket.add
      	   	
      	   getTickets(String username) - Get the tickets for the current user based off username
      	   	Call Path: Gapi.client.purdueParking.ticket.get
      	   	
      	   deleteTicket(String ticketNumber) - Deletes the given ticket
      	   	Call Path: Gapi.client.purdueParking.ticket.delete
      
        Vehicles:
         
         	addVehicle(Vehicle v) - Adds a new vehicle to the datastore
         		Call Path: Gapi.client.purdueParking.vehicle.add
         		
         	getVehicles(String username) - Gets a list of the vehicles for the given username
         		Call Path: Gapi.client.purdueParking.vehicle.get
         		
         	deleteVehicle(String carID) - Deletes the vehicle with the given ID
         		Call Path: Gapi.client.purdueParking.vehicle.delete
         		
         	editVehicle(Vehicle v) - Edits the information attached to the vehicle *Vehicle must already exist*
         		Call Path: Gapi.client.purdueParking.vehicle.edit
         		
        Accounts:
         
         	addAccount(Account a) - Adds a new account to the datastore
         		Call Path: Gapi.client.purdueParking.account.add
         	
         	deleteAccount(String username) - Deletes the account with the given username and all vehicles associated
         		Call Path: Gapi.client.purdueParking.account.delete
         	
         	editAccount(Account a) - Edits the information attached to the account *Account must already exist*
         		Call Path: Gapi.client.purdueParking.account.edit
  	
