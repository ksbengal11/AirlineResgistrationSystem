package server;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultListModel;

/**
 * Class Flight Catalouge has a flight database class which it can use to
 * interact with the database. 
 * @author Karan Bengali and Naveed Kawsar
 *
 */
public class FlightCatalouge {

	private FlightDatabase database;
	private DefaultListModel<String> flightList;
	private DefaultListModel<String> ticketList;
	
	private Flight flight;
	private Ticket ticket;
	
	private ResultSet rs;
	
	public FlightCatalouge(){
		setDatabase(new FlightDatabase());
		setFlightList(new DefaultListModel<String> ());
		ticketList = new DefaultListModel<String> ();
	}
	
	/**
	 * Validates login credentials
	 * @param username Client's username
	 * @param password Client's password
	 * @return True if the entered credentials match an entry in the database
	 */
	public boolean validateUser(String username, String password){
		if(getDatabase().validateLogin(username, password)) 
			return true;
		return false;
	}
	
	/**
	 * Insert a new user into the database
	 * @param firstname Client's firstname
	 * @param lastname Client's lastname
	 * @param username Client's username
	 * @param password Client's password
	 */
	public boolean insertUser(String firstname, String lastname, String username, String password){
		return getDatabase().insertUser(firstname, lastname, username, password);
	}
	
	/**
	 * Method to search for flights
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param date Flight date
	 * @return A list of flights
	 */
	public DefaultListModel<String> searchFlight(String src, String dest, 
			String date){
		rs = getDatabase().selectFlight(src, dest, date);
		getFlightList().clear();
		try {
			while(rs.next()){
				flight = new Flight(
						rs.getInt("FlightNum"),
						rs.getString("Source"),
						rs.getString("Destination"),
						rs.getString("Date"),
						rs.getString("DepartureTime"),
						rs.getString("Duration"),
						rs.getInt("TotalSeats"),
						rs.getInt("RemainingSeats"),
						rs.getDouble("Price")
				);
				getFlightList().addElement(flight.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return getFlightList();
	}
	
	/**
	 * Method to book a flight ticket
	 * @param flightId Flight Id
	 * @param firstname Client's firstname
	 * @param lastname Client's lastname
	 * @return True if booking was successful
	 */
	public synchronized boolean bookTicket(int flightId, String firstname, String lastname){
		getDatabase().setFlightId(flightId);
		getDatabase().selectFlight();
		try{
			if(getDatabase().getResultSet().next()){
				if(getDatabase().getResultSet().getInt("RemainingSeats") == 0) 
					return false;
				getDatabase().insertTicket(firstname, lastname, 
						getDatabase().getResultSet().getInt("FlightNum"), 
						getDatabase().getResultSet().getString("Source"), 
						getDatabase().getResultSet().getString("Destination"), 
						getDatabase().getResultSet().getString("Date"), 
						getDatabase().getResultSet().getString("DepartureTime"), 
						getDatabase().getResultSet().getString("Duration"), 
						getDatabase().getResultSet().getDouble("Price")
				);
			}
			selectTicket(getDatabase().getTicketId()).printTicket();
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * Method to search for flight tickets
	 * @return List of booked tickets
	 */
	public DefaultListModel <String> searchTickets(){
		rs = getDatabase().selectTicket();
		ticketList.clear();
		try{
			while(rs.next()){
				ticket = new Ticket(
						rs.getString("Firstname"),
						rs.getString("Lastname"),
						rs.getInt("FlightNum"),
						rs.getString("Source"),
						rs.getString("Destination"),
						rs.getString("Date"),
						rs.getString("DepartureTime"),
						rs.getString("Duration"),
						rs.getDouble("Price"),
						rs.getInt("TicketID")
				);
				ticketList.addElement(ticket.toString());
			}
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
		return ticketList;
	}
	
	public Ticket selectTicket(int ticketId){
		rs = getDatabase().selectTicket(ticketId);
		try {
			if(rs.next()){
				ticket = new Ticket(
						rs.getString("Firstname"),
						rs.getString("Lastname"),
						rs.getInt("FlightNum"),
						rs.getString("Source"),
						rs.getString("Destination"),
						rs.getString("Date"),
						rs.getString("DepartureTime"),
						rs.getString("Duration"),
						rs.getDouble("Price"),
						rs.getInt("TicketID")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return ticket;
	}
	
	/**
	 * Method to cancel tickets
	 * @param ticketId Id of ticket to be cancelled
	 */
	public void cancelTicket(int ticketId){
		getDatabase().cancelTicket(ticketId);
	}
	
	/**
	 * Method to insert new flights into the database
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param departureDate Flight departure date
	 * @param departureTime Flight departure time
	 * @param duration Flight duration
	 * @param totalSeats Total seats on the flight
	 * @param remainingSeats Remaining seats on the flight
	 * @param price Ticket price
	 */
	public void insertFlight(String src, String dest, String departureDate, 
			String departureTime, 
			String duration, int totalSeats, 
			int remainingSeats, double price){
		getDatabase().insertFlight(src, dest, departureDate, departureTime, 
				duration, totalSeats, remainingSeats, price);
	}
	
	/**
	 * Method to load flights from a file
	 */
	public void loadFlights(String filename){
		// TODO load flights from a file
	}
	
	public String selectUserFirstName(String username){
		getDatabase().selectUser(username);
		try {
			if(getDatabase().getResultSet().next()){
				return getDatabase().getResultSet().getString("firstname");
			}else 
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String selectUserLastName(String username){
		getDatabase().selectUser(username);
		try {
			if(getDatabase().getResultSet().next()){
				return getDatabase().getResultSet().getString("lastname");
			}else 
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public FlightDatabase getDatabase() {
		return database;
	}

	public void setDatabase(FlightDatabase database) {
		this.database = database;
	}

	public DefaultListModel<String> getFlightList() {
		return flightList;
	}

	public void setFlightList(DefaultListModel<String> flightList) {
		this.flightList = flightList;
	}
}
