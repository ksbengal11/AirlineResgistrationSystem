package client;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class Admin extends Client{
	private JFrame mainFrame;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JList<String> listArea = new JList<String>(listModel);

	public Admin(String hostName, int portNumber) {
		super(hostName, portNumber);
		communicate();
		initializeAdminGUI();
	}
	
	public void initializeAdminGUI(){
		mainFrame = new JFrame();
		mainFrame.setBounds(100, 100, 500, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JLabel lblArsPassenger = new JLabel("ARS Admin");
		lblArsPassenger.setFont(new Font("Arial", Font.PLAIN, 24));
		lblArsPassenger.setBounds(180, 11, 123, 47);
		mainFrame.getContentPane().add(lblArsPassenger);
		
		listArea.setVisibleRowCount(8);
		listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listPanel = new JScrollPane(listArea);
		listPanel.setBounds(10, 134, 464, 216);
		mainFrame.getContentPane().add(listPanel);
		
		// Display tickets from database
		JButton btnBrowsetickets = new JButton("Browse");
		btnBrowsetickets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModel.clear();
				listArea.setModel(listModel);
				listTickets.clear();
				displayTickets.clear();
				updateListArea();
				
				// Send request to server
				socketOut.flush();
				socketOut.println("SEARCH_TICKET");
				// Read listModel<String> from server
				deserializeTicket();
				// Update listArea
				updateListArea();
			}
		});
		btnBrowsetickets.setBounds(10, 100, 104, 23);
		mainFrame.getContentPane().add(btnBrowsetickets);
		
		// Cancel ticket
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Read ticketId
				int index = listArea.getSelectedIndex();
				String [] ticketInfo = listTickets.getElementAt(index).split(" ");
				
				// Print ticketId to the output stream
				socketOut.println("CANCEL_TICKET");
				socketOut.println(ticketInfo[0]);
				socketOut.println(ticketInfo[1]);
				
				// Update listArea
				updateListArea();
			}
		});
		btnCancel.setBounds(124, 100, 104, 23);
		mainFrame.getContentPane().add(btnCancel);
		
		// Add Flight
		JButton btnAddFlight = new JButton("Add Flight");
		btnAddFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField [] flightParams = new JTextField[9];
				for(int i = 0; i < 8; i++){
					flightParams[i] = new JTextField();
				}
				String origin, destination, departureDate, departureTime, duration;
				int totalSeats, remainingSeats;
				double ticketPrice;
				Object [] message = {
						"Flight Origin: ", flightParams[0],
						"Flight Destination: ", flightParams[1],
						"Flight Departure Date: ", flightParams[2],
						"Flight Departure Time: ", flightParams[3],
						"Flight Duration: ", flightParams[4],
						"Total Seats: ", flightParams[5],
						"Remaining Seats: ", flightParams[6],
						"Ticket Price: ", flightParams[7]
						
				};
				int result = JOptionPane.showConfirmDialog(null, message, "New Flight", JOptionPane.OK_CANCEL_OPTION);
				// Read flight parameters
				if(result == JOptionPane.OK_OPTION){
					origin = flightParams[0].getText();
					destination = flightParams[1].getText();
					departureDate = flightParams[2].getText();
					departureTime = flightParams[3].getText();
					duration = flightParams[4].getText();
					
					if(flightParams[5].getText().isEmpty() || flightParams[6].getText().isEmpty() || flightParams[7].getText().isEmpty()){
						totalSeats = -1;
						remainingSeats = -1;
						ticketPrice = -1;
					}else {
						totalSeats = Integer.parseInt(flightParams[5].getText());
						remainingSeats = Integer.parseInt(flightParams[6].getText());
						ticketPrice = Double.parseDouble(flightParams[7].getText());	
					}
					
					// Check for errors
					if(!entryError(origin, destination, departureDate, departureTime, duration, totalSeats, remainingSeats, ticketPrice)){
						// Print flight parameters to the output stream
						socketOut.println("ADD_FLIGHT");
						socketOut.flush();
						socketOut.println(origin);
						socketOut.println(destination);
						socketOut.println(departureDate);
						socketOut.println(departureTime);
						socketOut.println(duration);
						socketOut.println(totalSeats);
						socketOut.println(remainingSeats);
						socketOut.println(ticketPrice);
					}
				}
			}
		});
		btnAddFlight.setBounds(255, 100, 103, 23);
		mainFrame.getContentPane().add(btnAddFlight);
		
		// Load flights from a file		
		JButton btnLoadFlight = new JButton("Load Flight");
		btnLoadFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField filename = new JTextField();
				Object [] message = {"Filename", filename};
				// Read file name
				int result = JOptionPane.showConfirmDialog(null, message, "Load Flights", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					// Print file name to output stream
					socketOut.println("LOAD_FLIGHTS");
					try{
						// Check for errors
						String response = socketIn.readLine();
						if(response.equalsIgnoreCase("FAIL")){
							JOptionPane.showConfirmDialog(null, "File not found", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}catch(IOException e1){
						e1.printStackTrace();
					}
				}
					
			}
		});
		btnLoadFlight.setBounds(368, 100, 106, 23);
		mainFrame.getContentPane().add(btnLoadFlight);
		
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				closeConnection();
			}
		});
	}
	
	public boolean entryError(String origin, String dest, 
			String date, String time, String duration, 
			int totalSeats, int remainingSeats, double price){
		if(origin.isEmpty() || dest.isEmpty() || date.isEmpty() || time.isEmpty() || duration.isEmpty()){
			JOptionPane.showMessageDialog(null, "Cannot have empty fields","Error",JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(origin.equalsIgnoreCase(dest)){
			JOptionPane.showMessageDialog(null, "Source and Destination cannot be the same","Error",JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(totalSeats <= 0){
			JOptionPane.showMessageDialog(null, "Total seats cannot be less than or equal to zero","Error",JOptionPane.ERROR_MESSAGE);
			return true;			
		}else if(remainingSeats < 0){
			JOptionPane.showMessageDialog(null, "Remaining seats cannot be less than zero","Error",JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(remainingSeats > totalSeats){
			JOptionPane.showMessageDialog(null, "Remaining seats cannot be greater than total seats","Error",JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(price <=0 ){
			JOptionPane.showMessageDialog(null, "Price cannot be less than or equal to 0","Error",JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(!date.matches("^([0-9]||1[0-2])/([0-2][0-9]||3[0-1])/([0-9][0-9])?[0-9][0-9]$")){
			JOptionPane.showMessageDialog(null, "Invalid date format","Error",JOptionPane.ERROR_MESSAGE);
			return true;			
		}else if(!time.matches("^([0-9]||1[0-9]||2[0-9]):([0-5][0-9])$")){
			JOptionPane.showMessageDialog(null, "Invalid departure time format","Error",JOptionPane.ERROR_MESSAGE);
			return true;			
		}else if(!duration.matches("^([0-9]||1[0-9]):([0-5][0-9])$")){
			JOptionPane.showMessageDialog(null, "Invalid duration format","Error",JOptionPane.ERROR_MESSAGE);
			return true;			
		}
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try{
			Date today = formatter.parse(formatter.format(new Date()));
			Date searchedDate = formatter.parse(date);
			if(searchedDate.before(today)){
				JOptionPane.showMessageDialog(null, "Date has passsed!","Error", JOptionPane.ERROR_MESSAGE);
				return true;
			}
		}catch(ParseException e){
			e.printStackTrace();
		}
		return false;
	}
	
	public void updateListArea(){
		listModel.clear();
		for(int i = 0; i < displayTickets.size(); i++){
			listModel.addElement(displayTickets.getElementAt(i));
		}
		listArea.setModel(listModel);
	}
	
	
	
	public static void main(String[] args) {
		Admin window = new Admin("localhost", 9898);
		window.mainFrame.setVisible(true);

	}

}
