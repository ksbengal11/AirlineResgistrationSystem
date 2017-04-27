package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.DefaultListModel;

public class Client {
	private		Socket 				aSocket;
	protected 	BufferedReader 		socketIn;
	protected 	PrintWriter 		socketOut;
	protected 	ObjectOutputStream 	objectOut;
	protected 	ObjectInputStream 	objectIn;
	
	private String hostName;
	private int portNumber;
	
	protected DefaultListModel <String> listFlights;
	protected DefaultListModel <String> displayFlights;
	protected DefaultListModel <String> listTickets;
	protected DefaultListModel <String> displayTickets;
	
	public Client(String hostName, int portNumber){
		this.hostName = hostName;
		this.portNumber = portNumber;
		listFlights = new DefaultListModel<String> ();
		displayFlights = new DefaultListModel<String> ();
		listTickets = new DefaultListModel<String> ();
		displayTickets = new DefaultListModel<String> ();		
	}
	
	public void communicate(){
		this.connect();
		try{
			socketIn	=new BufferedReader(new InputStreamReader(aSocket.getInputStream()));
			socketOut	=new PrintWriter((aSocket.getOutputStream()),true);
			objectOut	=new ObjectOutputStream(aSocket.getOutputStream());
			objectIn	=new ObjectInputStream(aSocket.getInputStream());
			
			System.out.println("Client ready to communicate");
			socketOut.println();
			
		}catch(IOException e){
			System.out.println("Client communication error" + e.getMessage());
		}
		
	}
	
	protected void connect(){
		try{
			aSocket = new Socket(hostName, portNumber);
		}catch(IOException e){
			System.out.println("Could not connnect to the socket " + e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void deserializeTicket(){
		// TODO Implement method to read retured ticket list from socket
		listTickets.clear();
		try{
			listTickets = (DefaultListModel<String>) objectIn.readObject();
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		updateDisplayTickets();
	}
	
	protected void updateDisplayTickets(){
		String displayMessage;
		displayTickets.clear();
		String [] record;
		for(int i = 0; i < listTickets.size(); i++){
			record = listTickets.get(i).split(" ");
			displayMessage = record[0] + "        "
					+ record[1] + "        "
					+ record[2] + "        " 
					+ record[3] + "        "
					+ record[4] + "        "
					+ record[5] + "        "
					+ record[6] + "        "
					+ record[7];
			displayTickets.addElement(displayMessage);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void deserializeFlights(){
		// Implement method to read returned flight list from socket
		//System.out.println("Before Reading: " + listFlights.size());
		listFlights.clear();
		try{
			listFlights = (DefaultListModel<String>) objectIn.readObject();
			//System.out.println("After Reading: "+listFlights.size());
		}catch(IOException | ClassNotFoundException e){
			e.printStackTrace();
		}
		updateDisplayFlights();
	}
	
	protected void updateDisplayFlights(){
		String displayMessage;
		displayFlights.clear();
		String [] record;
		for(int i = 0; i < listFlights.size(); i++){
			record = listFlights.get(i).split(";");
			displayMessage = record[1] + "     "
					+ record[2] + "     "
					+ record[3] + "     "
					+ record[4] + "     "
					+ record[5] + "     "
					+ record[8];
			displayFlights.addElement(displayMessage);
		}
	}
	
	protected void closeConnection(){
		try{
			socketOut.println("QUIT");
			socketIn.close();
			socketOut.close();
			objectOut.close();
			objectIn.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
