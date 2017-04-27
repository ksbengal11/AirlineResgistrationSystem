package server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Ticket {
	/**
	 * A ticket contains information such as 
		a.  Passenger first and last name 
		b.  Flight number 
		c.  Flight source and destination 
		d.  Flight date, time and duration 
		e.  Price of the ticket
	 */
	private PassengerInfo passenger;
	private int ticketId;
	private int flightNum;
	private String flightSrc;
	private String flightDest;
	private String flightDepartureDate;
	private String flightDepartureTime;
	private String flightDuration;
	private double ticketPrice;
	private static final String FILE_PATH = "tickets.txt";
	
	private FileWriter write;
	private PrintWriter print_line;
	
	Ticket() {
		this.passenger = new PassengerInfo(null, null);
		
	}
	Ticket(String firstname, String lastname,int flightNum, String source, 
			String destination, String departureDate, String departureTime, 
			String duration, double price, int ticketId) {
		
		this.passenger = new PassengerInfo(firstname, lastname);
		this.ticketId = ticketId;
		this.flightNum = flightNum;
		this.flightSrc = source;
		this.flightDest = destination;
		this.flightDepartureDate = departureDate;
		this.flightDepartureTime = departureTime;
		this.flightDuration = duration;
		this.ticketPrice = price;
		
		try{
			write = new FileWriter(FILE_PATH, false);
			print_line = new PrintWriter(write);
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}

	public PassengerInfo getPassenger() {
		return passenger;
	}

	public int getTicketId(){
		return ticketId; 
	}
	
	public int getFlightNum() {
		return flightNum;
	}

	public String getFlightSrc() {
		return flightSrc;
	}

	public String getFlightDest() {
		return flightDest;
	}
	
	public String getFlightDepartureDate(){
		return this.flightDepartureDate;
	}
	
	public String getFlightDepartureTime(){
		return this.flightDepartureTime;
	}
	
	public String getFlightDuration(){
		return this.flightDuration;
	}
	
	public double getTicketPrice() {
		return this.ticketPrice;
	}

	public void setPassenger(PassengerInfo passenger) {
		this.passenger = passenger;
	}

	public void setTicketId(int id){
		this.ticketId = id;
	}
	
	public void setFlightNum(int flightNum) {
		this.flightNum = flightNum;
	}

	public void setFlightSrc(String flightSrc) {
		this.flightSrc = flightSrc;
	}

	public void setFlightDest(String flightDest) {
		this.flightDest = flightDest;
	}
	
	public void setFlightDepartureDate(String departureDate){
		this.flightDepartureDate = departureDate;
	}
	
	public void setFlightDepartureTime(String departureTime){
		this.flightDepartureTime = departureTime;
	}
	
	public void setFlightDuration(String duration){
		this.flightDuration = duration;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

	public void printTicket() {
		print_line.printf("---------------------------------------------------------\n");
		print_line.printf("PASSENGER: " + this.getPassenger().toString() + "\n");
		print_line.printf("\nFlight Number: " + this.getFlightNum() + "\n");
		print_line.printf("Origin: " + this.getFlightSrc() + " Destination: " + this.getFlightDest() + "\n");
		print_line.printf("Cost: $" + this.getTicketPrice() + " (CAD)\n");
		print_line.printf("\nFlight Information:\n");
		print_line.printf("Departure Date: " + this.getFlightDepartureDate());
		print_line.printf("\nDeparture Time: " + this.getFlightDepartureDate());
		print_line.printf("\nFlight Duration: " + this.getFlightDepartureDate());
		print_line.close();
	}
	
	public String toString() {
		return this.getTicketId() + " "
				+ this.getFlightNum() + " "
				+ this.getFlightSrc() + " "
				+ this.getFlightDest() + " "
				+ this.getFlightDepartureDate() + " "
				+ this.getFlightDepartureTime() + " "
				+ this.getFlightDuration() + " "
				+ this.getTicketPrice();
	}

}
