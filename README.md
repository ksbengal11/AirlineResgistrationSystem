# AirlineResgistrationSystem

**Requirement Description:** <br />
Develop a client-server application for an Airline Registration System (ARS). This system contins several flights which are saved in the system's database. <br /><br />
Each Flight contains the following information: <br />
- Flight number (which is unique) <br />
- Source and destination location <br />
- Date, time, and duration of the flight <br />
- Total number of seats available <br />
- Remaining number of seats <br />
- Price of flight <br /><br />

There are two types of users in this system: passengers and administrators.<br /><br />
**Functional Requriements:** <br />
Through the passenger client GUI, the user will be able to: 
1. Search for flights based on date, source, or destination of flights and browse through the search results<br />
2. Select a flight to see more information about the flight <br />
3. Book a flight and create a ticket <br />
4. Once a ticket has been booked, print ticket to a file on the client's computer. A ticket continas: <br />
..* Passenger first and last name <br />
..* Flight number <br />
..* Flight source and destination <br />
..* Flight date, time, and duration <br />
..* Price of ticket <br /><br />

Through the administrator GUI, the user will be able to:
1. Browse through the booked tickets <br />
2. Cancel booked ticket <br />
3. Add flights: <br />
.. Add one flight at a time <br />
.. Add a group of flights <br /><br />

**Constraints:**
1. The system must be developed as a client server application<br />
2. The client side of this application must have GUIs<br />
3. The server must be able to support multiple clinets using it at one time<br />
4. The system must include a database which is populated with records<br /><br />

**Improvements:**
- More JUNIT test for classes <br />
- Deploy the system on multiple machines<br />

