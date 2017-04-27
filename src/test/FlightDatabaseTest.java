package test;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import server.FlightDatabase;

/**
 * @author Karan Bengali
 * 
 * Before running the test, drop Flight, Ticket, and Login tables
 * from the AirlineRegistration Database
 */
public class FlightDatabaseTest {
	private FlightDatabase test = new FlightDatabase();
	private ResultSet rs;
	
	@Test
	public void testMOAT(){
		testInsertFlight();
		testInsertTicket();
		testInsertUser();
		testValidateLogin();
		testSelectFlight();
		testSelectTicket();
		testCancelTicket();
	}
	
	public void testInsertFlight() {
		test.insertFlight("Calgary", "Edmonton", 
				"4/24/2017", "8:25", "0:40", 40, 35, 
				100.0);
		assertEquals(test.getFlightId(), 1, 0);
	}

	public void testInsertTicket() {
		test.insertTicket("First", "User", 1, 
				"Calgary", "Edmonton", "4/24/2017", "7:50", 
				"0:40", 100.0);
		assertEquals(1, test.getTicketId());
		assertEquals(34, test.getRemainingSeats());
	}
	
	public void testInsertUser() {
		test.insertUser("First", "User", "default", "default");
		assertEquals(1, test.getUserId());
	}
	
	public void testValidateLogin(){
		assertTrue(test.validateLogin("default", "default"));
	}
	
	public void testSelectFlight(){
		rs = test.selectFlight("", "", "");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("Calgary", "", "");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("", "Edmonton", "");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("", "", "4/24/2017");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("", "Edmonton", "4/24/2017");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("Calgary", "", "4/24/2017");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("Calgary", "Edmonton", "");
		assertTrue(successfulSearch(rs));
		
		rs = test.selectFlight("Calgary", "Edmonton", "4/24/2017");
		assertTrue(successfulSearch(rs));
	}
	
	private boolean successfulSearch(ResultSet rs){
		try {
			if(rs.first()){
				if(rs.getInt("FlightNum") != 1) 
					fail();
				else if(!rs.getString("Source").equalsIgnoreCase("Calgary")) 
					fail();
				else if(!rs.getString("Destination").equalsIgnoreCase("Edmonton")) 
					fail();
				else if(!rs.getString("Date").equalsIgnoreCase("4/24/2017")) 
					fail();
			}else{
				fail();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
		return true;
	}
	
	public void testSelectTicket(){
		rs = test.selectTicket();
		try {
			if(rs.next()){
				assertTrue(rs.getInt("TicketID") == 1);
				assertTrue(rs.getString("Firstname").equalsIgnoreCase("First"));
				assertTrue(rs.getString("Lastname").equalsIgnoreCase("User"));
				assertTrue(rs.getInt("FlightNum") == 1);
				assertTrue(rs.getString("Source").equalsIgnoreCase("Calgary"));
				assertTrue(rs.getString("Destination").equalsIgnoreCase("Edmonton"));
				
			}else 
				fail();
		} catch (SQLException e) {
			fail();
		}
	}
	
	public void testCancelTicket(){
		test.cancelTicket(1);
		rs = test.selectTicket();
		try{
			if(rs.next()){
				fail();
			}else{
				assert(true);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		assertEquals(35, remainingSeats(1));
	}
	
	public int remainingSeats(int id){
		test.setQuery("SELECT * FROM " + FlightDatabase.getFlightTable() 
		+ " where FlightNum = '" + id + "'");
		try{
			test.setpStat(test.getConnect().prepareStatement(test.getQuery()));
			test.setResultSet(test.getpStat().executeQuery());
			
			if(test.getResultSet().next())
				return test.getResultSet().getInt("RemainingSeats");

		}catch(SQLException e){
			e.printStackTrace();
			fail();
		}
 		return -1;
	}

}