package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class server
 * @author Karan Bengali and Naveed Kawsar
 * @since April 26, 2017
 * @version 2.0
 *
 */
public class Server {
	
	private FlightCatalouge catalouge;
	private ServerSocket serverSocket;
	private ExecutorService pool;
	
	/**
	 * Default constructor
	 * @param portNumber Port number of the server
	 */
	public Server(int portNumber){
		catalouge = new FlightCatalouge ();
		try{
			serverSocket = new ServerSocket(portNumber);
			System.out.println("Server running on localhost:" + portNumber);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
		pool = Executors.newCachedThreadPool();
	}
	
	/**
	 * WorkerThread to handle communication between the sever and multiple clients
	 * @author Karan Bengali and Naveed Kawsar
	 *
	 */
	public class WorkerThread implements Runnable{
		private Socket aSocket;
		private BufferedReader socketIn;
		private PrintWriter socketOut;
		private ObjectOutputStream objectOut;
		
		private String firstname, lastname, username, password;
		private String src, dest, date, time, duration;
		private int flightID, ticketID, totalSeats, remainingSeats;
		private double price;
		
		/**
		 * Default constructor
		 * @param s Socket
		 */
		public WorkerThread(Socket s){
			aSocket = s;
			try{
				socketIn	= 	new	BufferedReader(new InputStreamReader(aSocket.getInputStream()));
				socketOut	=	new PrintWriter((aSocket.getOutputStream()),true);
				objectOut	=	new ObjectOutputStream(aSocket.getOutputStream());
			}catch (IOException e){
				e.printStackTrace();
				System.exit(1);
			}
		}
		
		@Override
		public void run() {
			String line = null;
			boolean running = true;
			while(running){
				try {
					line = socketIn.readLine();
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(1);
				}
				if(line.equalsIgnoreCase("QUIT")){
					running = false;
				}else if(line.equalsIgnoreCase("LOGIN")){
					readLoginCredentials();
					if(catalouge.validateUser(username, password)){
						socketOut.println("PASS");
						socketOut.println(catalouge.selectUserFirstName(username));
						socketOut.println(catalouge.selectUserLastName(username));
					}else
						socketOut.println("FAIL");
					
				}else if(line.equalsIgnoreCase("NEWUSER")){
					readNewUserCredentials();
					if(catalouge.insertUser(firstname, lastname, username, password))
						socketOut.println("USER_ADDED");
					else
						socketOut.println("FAIL");
					
				}else if(line.equalsIgnoreCase("SEARCH_FLIGHT")){
					readFlightSearchParams();
					try {
						objectOut.writeObject(catalouge.searchFlight(src, dest, date));
						objectOut.reset();
					} catch (IOException e) {
						e.printStackTrace();
						closeConnection();
					}
					
				}else if(line.equalsIgnoreCase("BOOK_TICKET")){
					readNewTicketParams();
					if(catalouge.bookTicket(flightID, firstname, lastname))
						socketOut.println("BOOKED");
					else
						socketOut.println("FULL");
					
				}else if(line.equalsIgnoreCase("SEARCH_TICKET")){
					try{
						objectOut.writeObject(catalouge.searchTickets());
						objectOut.reset();
					}catch(IOException e){
						e.printStackTrace();
						closeConnection();
					}
					
				}else if(line.equalsIgnoreCase("CANCEL_TICKET")){
					try{
						ticketID = Integer.parseInt(socketIn.readLine());
						catalouge.cancelTicket(ticketID);
					}catch(IOException e){
						e.printStackTrace();
						closeConnection();
					}
					
				}else if(line.equalsIgnoreCase("ADD_FLIGHT")){
					readNewFlightParams();
					catalouge.insertFlight(src, dest, date, time, duration, totalSeats, remainingSeats, price);
					
				}else if(line.equalsIgnoreCase("LOAD_FLIGHTS")){
					try{
						String filename = socketIn.readLine();
						catalouge.loadFlights(filename);
					}catch(IOException e){
						e.printStackTrace();
						closeConnection();
					}
					
				}
				System.out.println(line);
			}
			closeConnection();
		}
		
		/**
		 * Read username and password from input stream
		 */
		private void readLoginCredentials(){
			try{
				username = socketIn.readLine();
				password = socketIn.readLine();
			}catch(IOException e){
				System.out.println("Could not read login credentials.");
				e.printStackTrace();
				closeConnection();
			}
		}
		
		/**
		 * Read firstname, lastname, username, and password from input stream
		 */
		private void readNewUserCredentials(){
			try{
				firstname = socketIn.readLine();
				lastname = socketIn.readLine();
				username = socketIn.readLine();
				password = socketIn.readLine();
			}catch(IOException e){
				System.out.println("Could not read new user information");
				e.printStackTrace();
				closeConnection();
			}
		}
		
		/**
		 * Read source, destination, and date from input stream
		 */
		private void readFlightSearchParams(){
			try{
				src = socketIn.readLine();
				dest = socketIn.readLine();
				date = socketIn.readLine();
			}catch(IOException e){
				System.out.println("Could not read flight search parameters");
				e.printStackTrace();
				closeConnection();
			}
		}
		
		/**
		 * Read flight number, firstname, and lastname from input stream 
		 */
		private void readNewTicketParams(){
			try{
				firstname = socketIn.readLine();
				lastname = socketIn.readLine();
				flightID = Integer.parseInt(socketIn.readLine());
			}catch(IOException e){
				System.out.println("Could not read new ticket parameters");
				e.printStackTrace();
				closeConnection();
			}
		}
		
		/**
		 * Read source, destination, date, time, duration, total seats, 
		 * remaining seats and ticket price from input stream 
		 */
		private void readNewFlightParams(){
			try{
				src = socketIn.readLine();
				dest = socketIn.readLine();
				date = socketIn.readLine();
				time = socketIn.readLine();
				duration = socketIn.readLine();
				totalSeats = Integer.parseInt(socketIn.readLine());
				remainingSeats = Integer.parseInt(socketIn.readLine());
				price = Double.parseDouble(socketIn.readLine());
			}catch(IOException e){
				e.printStackTrace();
				closeConnection();
			}
		}
		
		/**
		 * Closes socket connections 
		 */
		private void closeConnection(){
			socketOut.println("QUIT");
			try{
				socketIn.close();
				socketOut.close();
				objectOut.close();
				aSocket.close();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		Server server = new Server(9898);
		try{
			for(;;){
				server.pool.execute(server.new WorkerThread(server.serverSocket.accept()));
			}
		}catch(IOException e){
			server.pool.shutdown();
		}

	}

}
