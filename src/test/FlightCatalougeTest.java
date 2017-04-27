package test;

import static org.junit.Assert.*;

import javax.swing.DefaultListModel;

import org.junit.Test;

import server.FlightCatalouge;

public class FlightCatalougeTest {

	private FlightCatalouge catalouge = new FlightCatalouge ();
	DefaultListModel<String> flightList;
	DefaultListModel<String> ticketList;
	
	int numUser = 2;
	int numFlight = 2;
	int numTicket = 2;
	int numSeats = 35;
	
	@Test
	public void MOAT(){
		testInsertUser();
		testValidateUser();
		testInsertFlight();
		testBookTicket();
		testSearchFlight();
		testSearchTickets();
		testCancelTicket();
	}
	
	public void testValidateUser() {
		assertTrue(catalouge.validateUser("default", "default"));
	}

	public void testInsertUser() {
		catalouge.insertUser("First", "User", "default", "default");
		numUser++;
		assertEquals(numUser, catalouge.getDatabase().getUserId());
	}

	public void testSearchFlight() {
		flightList = catalouge.searchFlight("", "", "");
		assertEquals(numFlight, flightList.size());
		
		flightList = catalouge.searchFlight("Calgary", "", "");
		assertEquals(numFlight, flightList.size());
		
		flightList = catalouge.searchFlight("Calgary", "", "4/24/2017");
		assertEquals(numFlight, flightList.size());
		
		flightList = catalouge.searchFlight("", "Edmonton", "");
		assertEquals(numFlight, flightList.size());
		
		flightList = catalouge.searchFlight("", "Edmonton", "4/24/2017");
		assertEquals(numFlight, flightList.size());
		
		flightList = catalouge.searchFlight("Calgary", "Edmonton", "4/24/2017");
		assertEquals(numFlight, flightList.size());
	}

	public void testBookTicket() {
		catalouge.bookTicket(numFlight, "First", "User");
		numSeats--;
		numTicket++;
		assertEquals(numTicket, catalouge.getDatabase().getTicketId());
		assertEquals(numSeats, catalouge.getDatabase().getRemainingSeats());
	}

	public void testSearchTickets() {
		ticketList = catalouge.searchTickets();
		assertEquals(numTicket, ticketList.size());
	}

	public void testCancelTicket() {
		catalouge.cancelTicket(numTicket);
		numSeats++;
		assertEquals(numSeats, catalouge.getDatabase().getRemainingSeats());
		
		numTicket--;
		ticketList = catalouge.searchTickets();
		assertEquals(numTicket, ticketList.size());
	}

	public void testInsertFlight() {
		catalouge.insertFlight("Calgary", "Edmonton", 
				"4/24/2017", "8:25", "0:40", 40, 35, 
				100.0);
		numFlight++;
		assertEquals(numFlight, catalouge.getDatabase().getFlightId());
	}

}
