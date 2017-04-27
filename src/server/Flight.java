package server;

public class Flight {
	/**
	 * Each Flight contains the following information  
		-  Flight number (which is unique) 
		-  Source and destination locations  
		-  Date, time and duration of the flight 
		-  Total number of seats available 
		-  Remaining number of seats 
		-  Price of the flight
	 */
	private int flightNum;
	private String source;
	private String destination;
	private String departureDate;
	private String departureTime;
	private String duration;
	private int totalSeats;
	private int remainingSeats;
	private double price;
	
	Flight() {
		flightNum = 0;
		source = destination = null;
		departureTime = null;
		totalSeats  = remainingSeats = 0;
		duration = null;
		price = 0;
	}
	
	Flight(int flightNum, String source, String destination, String departureDate,
			String departureTime, String duration, int totalSeats, int remainingSeats, double price) {
		this.flightNum = flightNum;
		this.source = source;
		this.destination = destination;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.duration = duration;
		this.totalSeats = totalSeats;
		this.remainingSeats = remainingSeats;
		this.price = price;
		
	}
	
	public int getFlightNum() {
		return flightNum;
	}

	public String getSource() {
		return source;
	}

	public String getDestination() {
		return destination;
	}
	
	public String getDepartureDate(){
		return departureDate;
	}
	
	public String getDepartureTime() {
		return departureTime;
	}

	public String getDuration() {
		return duration;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public int getRemainingSeats() {
		return remainingSeats;
	}

	public double getPrice() {
		return price;
	}

	public void setFlightNum(int flightNum) {
		this.flightNum = flightNum;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}
	
	public void setDepartureDate(String date){
		this.departureDate = date;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public void setRemainingSeats(int remainingSeats) {
		this.remainingSeats = remainingSeats;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public String toString(){
		return 
				this.getFlightNum() + ";" 
				+ this.getSource() + ";" 
				+ this.getDestination() + ";"
				+ this.getDepartureDate() + ";"
				+ this.getDepartureTime() + ";"
				+ this.getDuration() + ";"
				+ this.getTotalSeats() + ";"
				+ this.getRemainingSeats() + ";"
				+ this.getPrice();
	}
}

