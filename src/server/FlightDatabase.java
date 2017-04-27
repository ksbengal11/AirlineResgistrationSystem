package server;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class connects to the database and performes addition
 * and removal operations on the respective tables.
 * @author Karan Bengali and Naveed Kawsar
 *
 */
public class FlightDatabase {
	private final static String DB_NAME = "AirlineRegistration";
	private final static String FLIGHT_TABLE = "Flights";
	private final static String TICKET_TABLE = "Tickets";
	private final static String LOGIN_TABLE = "Login";
	
	private final static String USER = "root"; 
	private final static String PASSWORD = "root";

	// Attributes 
	private Connection connect; 
	private Statement statement; 
	private ResultSet resultSet;
	
	private String query;
	private PreparedStatement pStat;
	
	private int flightId;
	private int ticketId;
	private int userId;
	private int remainingSeats;
	private int totalSeats;
	
	public FlightDatabase(){
		// Register JDBC driver
		Driver driver;
		try {
			driver = new com.mysql.jdbc.Driver();
			DriverManager.registerDriver(driver);
			
			// Open a connection
			String url = "jdbc:mysql://localhost:3306/AirlineRegistration?"
					+ "verifyServerCertificate=false&useSSL=true";
			setConnect(DriverManager.getConnection(url, USER, PASSWORD));
			statement = getConnect().createStatement();
			
			// Create database
			statement.executeUpdate("CREATE DATABASE IF NOT EXISTS " + 
			DB_NAME + ";");
		} catch (SQLException e) {
			System.out.println("Could not create database. Quitting now.");
			System.exit(1);
		} 
		initializeFlightsTable();
		initializeTicketsTable();
		initializeUsersTable();
	}
	
	/**
	 * Creates a table containing a list of flights.
	 */
	private void initializeFlightsTable(){
		String create_flight_table = "CREATE TABLE IF NOT EXISTS " + 
				getFlightTable() + " ("+ " FlightNum int AUTO_INCREMENT NOT NULL,"
	      		+ " Source varchar(20) NOT NULL,"
		    	+ " Destination varchar(20) NOT NULL,"
		    	+ " Date varchar(10),"
		    	+ " DepartureTime varchar(5) NOT NULL,"
		    	+ " Duration varchar(5) NOT NULL,"
		    	+ " TotalSeats int(3) NOT NULL,"
		    	+ " RemainingSeats int(3) NOT NULL,"
		    	+ " Price double(6,2) NOT NULL,"
		    	+ " PRIMARY KEY(FlightNum));";
		try {
			statement.executeUpdate(create_flight_table);
		} catch (SQLException e) {
			System.out.println("Could not create flights table. Quitting now.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Creates a table contiaing a list of tickets.
	 */
	private void initializeTicketsTable(){
	     // Create ticket table
	     String create_ticket_table = "CREATE TABLE IF NOT EXISTS " + 
	     TICKET_TABLE + " ("
	    		    + " TicketID int NOT NULL AUTO_INCREMENT,"
	    		  	+ " Firstname varchar(20) NOT NULL,"
	    		  	+ " Lastname varchar(20) NOT NULL,"
		      		+ " FlightNum int(4) NOT NULL,"
		      		+ " Source varchar(20) NOT NULL,"
			    	+ " Destination varchar(20) NOT NULL,"
			    	+ " Date varchar(10),"
			    	+ " DepartureTime varchar(5) NOT NULL,"
			    	+ " Duration varchar(5) NOT NULL,"
			    	+ " Price double(6,2) NOT NULL,"
			    	+ " PRIMARY KEY(TicketID));";
	      try {
			statement.executeUpdate(create_ticket_table);
		} catch (SQLException e) {
			System.out.println("Could not create tickets table. Quitting now");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Creates a table containing a list of users. 
	 */
	private void initializeUsersTable(){
	      // Create user table
	      String create_user_table = "CREATE TABLE IF NOT EXISTS " + 
	      LOGIN_TABLE + " ("
	    		  + " userId int NOT NULL AUTO_INCREMENT,"
	    		  + " firstname varchar(20) NOT NULL,"
	    		  + " lastname varchar(20) NOT NULL,"
	    		  + " username varchar(50) NOT NULL,"
	    		  + " password varchar(50) NOT NULL,"
	    		  + " PRIMARY KEY(userId));";
	      try {
			statement.executeUpdate(create_user_table);
		} catch (SQLException e) {
			System.out.println("Could not create users table. Quitting now");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Insert flight into the database
	 * @param source Flight source
	 * @param destination Flight destination
	 * @param date Flight date
	 * @param departureTime Flight departure time
	 * @param duration Flight duration
	 * @param totalSeats Total seats on the flight
	 * @param remainingSeats Remaining seats on the flight
	 * @param price Price of a ticket
	 */
	public void insertFlight(String source, String destination, String date, 
			String departureTime, String duration, int totalSeats, 
			int remainingSeats, double price){
		setQuery("INSERT INTO " + getFlightTable()
				+ " (Source, Destination, Date, "
				+ "DepartureTime, Duration, "
				+ "TotalSeats, RemainingSeats, Price) "
				+ "values (?,?,?,?,?,?,?,?)");
		try {
			setpStat(getConnect().prepareStatement(getQuery(), 
					PreparedStatement.RETURN_GENERATED_KEYS));
			getpStat().setString(1, source); 
			getpStat().setString(2, destination);
			getpStat().setString(3, date);
			getpStat().setString(4, departureTime);
			getpStat().setString(5, duration);
			getpStat().setInt(6, totalSeats);
			getpStat().setInt(7, remainingSeats);
			getpStat().setDouble(8, price);
			
			getpStat().executeUpdate();
			setResultSet(getpStat().getGeneratedKeys());
			if(getResultSet().next()) setFlightId(getResultSet().getInt(1));
			
		} catch (SQLException e) {
			System.out.println("SQL Error. Could not add flight");
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Insert ticket into database
	 * @param firstname Passenger's first name
	 * @param lastname Passenger's last name
	 * @param flightNum Flight number
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param departureDate Flight departure date
	 * @param departureTime Flight departure time
	 * @param duration Flight duration
	 * @param price Ticket price
	 */
	public void insertTicket(String firstname, String lastname, 
			int flightNum, String src, String dest, String departureDate, 
			String departureTime, String duration, double price){
		setQuery("INSERT INTO " + TICKET_TABLE
				+ " (Firstname, Lastname, FlightNum, "
				+ "Source, Destination, Date, DepartureTime, Duration, "
				+ "Price) values(?,?,?,?,?,?,?,?,?)");
		try {
			setpStat(getConnect().prepareStatement(getQuery(), 
					PreparedStatement.RETURN_GENERATED_KEYS));
			getpStat().setString(1, firstname);
			getpStat().setString(2, lastname);
			getpStat().setInt(3, flightNum);
			getpStat().setString(4, src);
			getpStat().setString(5, dest);
			getpStat().setString(6, departureDate);
			getpStat().setString(7, departureTime);
			getpStat().setString(8, duration);
			getpStat().setDouble(9, price);
			
			getpStat().executeUpdate();
			setResultSet(getpStat().getGeneratedKeys());
			if(getResultSet().next()) setTicketId(getResultSet().getInt(1));
			
		} catch (SQLException e) {
			System.out.println("SQL Error. Could not add flight");
			e.printStackTrace();
			return;
		}
		this.flightId = flightNum;
		decrementRemainingSeats();
	}
	
	/**
	 * Decrements the remaining seat number for a selected flight
	 * @param flightNum Flight Number
	 */
	private void decrementRemainingSeats(){
		updateRemainingSeats();
		if(getRemainingSeats() == -1) 
			return;
		setQuery("UPDATE " + getFlightTable()
				+ " SET RemainingSeats = ? where FlightNum = ?");
		try {
			setpStat(getConnect().prepareStatement(getQuery(),
					PreparedStatement.RETURN_GENERATED_KEYS));
			getpStat().setInt(1, getRemainingSeats());
			getpStat().setInt(2, flightId);
			getpStat().executeUpdate();
			
			setResultSet(getpStat().getGeneratedKeys());
			if(getResultSet().next()) setTicketId(getResultSet().getInt(1));
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
	}
	
	/**
	 * Updates the number of remaining seats in the Flight table
	 * @param flightNum Flight number
	 * @return Remaining seats
	 */
	public void updateRemainingSeats(){
		setQuery("SELECT * FROM " + getFlightTable() 
				+ " where FlightNum = ?");
		setRemainingSeats(-1);
		try {
			setpStat(getConnect().prepareStatement(getQuery()));
			getpStat().setString(1, String.valueOf(flightId));
			setResultSet(getpStat().executeQuery());
			
			if(getResultSet().next()) 
				setRemainingSeats(getResultSet().getInt("RemainingSeats"));
			
			if(getRemainingSeats() <= getResultSet().getInt("TotalSeats")) 
				setRemainingSeats(getRemainingSeats() - 1);
			
		} catch (SQLException e1) {
			e1.printStackTrace();
			setRemainingSeats(-1);
			return;
		}
	}
	
	/**
	 * Inserts a new user into the database
	 * @param firstname Clients's firstname
	 * @param lastname Clients's lastname
	 * @param username Client username
	 * @param password Client's password
	 */
	public boolean insertUser(String firstname, String lastname, 
			String username, String password){
		
		if(duplicateUser(username)) 
			return false;
		
		setQuery("INSERT INTO " + LOGIN_TABLE
				+ " (firstname,lastname,username,password)"
				+ " values(?,?,?,?)");
		try {
			setpStat(getConnect().prepareStatement(getQuery(), 
					PreparedStatement.RETURN_GENERATED_KEYS));
			getpStat().setString(1, firstname);
			getpStat().setString(2, lastname);
			getpStat().setString(3, username);
			getpStat().setString(4, password);
			
			getpStat().executeUpdate();
			setResultSet(getpStat().getGeneratedKeys());
			
			if(getResultSet().next()) 
				setUserId(getResultSet().getInt(1));
			
		} catch (SQLException e) {
			System.out.println("Cannot insert user into database");
			e.printStackTrace();
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * Checks if client can login in
	 * @param username Client's username
	 * @param password Client's password
	 * @return True if username and password exist in database
	 */
	public boolean validateLogin(String username, String password){
		try{
			setQuery("SELECT * FROM login where username = ? "
					+ "and password = ?");
			setpStat(getConnect().prepareStatement(getQuery()));
			getpStat().setString(1, username);
			getpStat().setString(2, password);
			
			setResultSet(getpStat().executeQuery());
			if(getResultSet().next()) return true;
			else return false;
		}catch(SQLException e){
			System.out.println("Cannot validate user");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Selects a flight from the database based on its source, destination, or date
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param date Flight departure date
	 * @return Result set of selected flights
	 */
	public ResultSet selectFlight(String src, String dest, String date){
		try{
			switch(emptySearchParams(src,dest,date)){
			case "000":
				setQuery("SELECT * FROM flights");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "001":
				setQuery("SELECT * FROM flights where Date = '" 
						+ date + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "010":
				setQuery("SELECT * FROM flights where Destination = '" 
						+ dest + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "011":
				setQuery("SELECT * FROM flights where Destination = '" 
						+ dest + "' and Date = '" + date + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "100":
				setQuery("SELECT * FROM flights where Source = '" 
						+ src + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "101":
				setQuery("SELECT * FROM flights where Source = '" 
						+ src + "' and Date = '" + date + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			case "111":
				setQuery("SELECT * FROM flights where Destination = '" 
						+ dest + "' and Date = '" + date + "' "
								+ "and Destination = '" + dest + "'");
				setpStat(getConnect().prepareStatement(getQuery()));
				break;
			}
			setResultSet(getpStat().executeQuery());
		}catch(SQLException e){
			return null;
		}
		return getResultSet();
	}
	
	/**
	 * Determines which search parameters are empty
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param date Flight departure date
	 * @return
	 */
	private String emptySearchParams(String src, String dest, String date){
		String emptySearchParams = null;
		if(src.isEmpty()) emptySearchParams = "0";
		else emptySearchParams = "1";
		
		if(dest.isEmpty()) emptySearchParams += "0";
		else emptySearchParams += "1";
		
		if(date.isEmpty()) emptySearchParams += "0";
		else emptySearchParams += "1";
		
		return emptySearchParams;
	}
	
	/**
	 * Selects all tickets in the database
	 * @return Result set of tickets in the database
	 */
	public ResultSet selectTicket(){
		setQuery("SELECT * FROM " + TICKET_TABLE);
		try {
			setpStat(getConnect().prepareStatement(getQuery()));
			setResultSet(getpStat().executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return getResultSet();
	}
	
	/**
	 * Selects a ticket based on the ticket id. 
	 * @param id Ticket id 
	 */
	public ResultSet selectTicket(int id){
		setQuery("SELECT * FROM " + TICKET_TABLE + " where TicketId = '" + id + "'");
		try {
			setpStat(getConnect().prepareStatement(getQuery()));
			setResultSet(getpStat().executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return resultSet;
	}
	
	/**
	 * Selects all flights in the database and updates the ResultSet variable
	 */
	public void selectFlight(){
		setQuery("SELECT * FROM " + getFlightTable() + " where FlightNum = '" + flightId + "'");
		try {
			setpStat(getConnect().prepareStatement(getQuery()));
			setResultSet(getpStat().executeQuery());
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}		
	}
	
	/**
	 * Deletes a specified ticket from the database
	 * @param id TicketId of the ticket being cancelled
	 */
	public void cancelTicket(int id){
		setTicketId(id);
		
		// Find the Flight Number of the target ticket
		selectTicket(ticketId);
		setFlightId(0);
		try {
			if(getResultSet().next()){
				setFlightId(getResultSet().getInt("FlightNum"));	
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			return;
		}
		
		// Delete ticket from the Tickets table
		setQuery("DELETE FROM "+ TICKET_TABLE + " where TicketId = '" + ticketId + "'");
		try{
			setpStat(getConnect().prepareStatement(getQuery()));
			getpStat().executeUpdate();
		} catch(SQLException e){
			e.printStackTrace();
			return;
		}
		updateSeatsRemaining();
	}
	
	/**
	 * Updates the number of remaining seats after  
	 */
	public void updateSeatsRemaining(){
		// Find the number of the remaining and total seats
		try {
			setQuery("SELECT * FROM " + getFlightTable() + " where FlightNum = '" + flightId + "'");
			setpStat(getConnect().prepareStatement(getQuery()));
			setResultSet(getpStat().executeQuery()); 
			
			if(getResultSet().next()){
				totalSeats = getResultSet().getInt("TotalSeats");
				remainingSeats = getResultSet().getInt("RemainingSeats");	
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		
		// If remaining seats is less than total seats, increment remaining seats
		if(remainingSeats < totalSeats) remainingSeats++;
		
		setQuery("UPDATE " + getFlightTable() + " SET RemainingSeats = '" + remainingSeats + "' where FlightNum = '" + flightId + "'");
		try{
			setpStat(getConnect().prepareStatement(getQuery()));
			getpStat().executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
			return;
		}
	}
	
	public void selectUser(String username){
		query = "SELECT * FROM " + LOGIN_TABLE + " where username = '" + username + "'";
		try {
			pStat = connect.prepareStatement(query);
			resultSet = pStat.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private boolean duplicateUser(String username){
		selectUser(username);
		try{
			if(resultSet.next()) return false;
			else return true;
		}catch(SQLException e){
			return true;
		}
	}

	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public int getTicketId() {
		return ticketId;
	}
	public void setTicketId(int ticketId) {
		this.ticketId = ticketId;
	}	
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId = userId;
	}
	public int getRemainingSeats() {
		return remainingSeats;
	}
	public void setRemainingSeats(int remainingSeats) {
		this.remainingSeats = remainingSeats;
	}
	public int getTotalSeats() {
		return totalSeats;
	}
	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public static String getFlightTable() {
		return FLIGHT_TABLE;
	}
	public PreparedStatement getpStat() {
		return pStat;
	}
	public void setpStat(PreparedStatement pStat) {
		this.pStat = pStat;
	}
	public Connection getConnect() {
		return connect;
	}
	public void setConnect(Connection connect) {
		this.connect = connect;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}