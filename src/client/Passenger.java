package client;

import java.awt.Color;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * Passenger GUI - desigined using Eclipse GUI builder
 * @author Karan Bengali and Naveed Kawsar
 *
 */
public class Passenger extends Client{
	JFrame loginFrame;
	JFrame mainFrame;
	
	private JTextField flightNumber, flightSource, flightDestination, flightDate, flightTime, flightPrice, seatsAvailable;
	private JTextField textField_username, textField_firstName, textField_lastName, textField_dateSearch, textField_originSearch, textField_destinationSearch;
	
	private String display_username, display_firstname, display_lastname;
	
    private DefaultListModel<String> listModel = new DefaultListModel<String>();
    private JList<String> listArea = new JList<String>(listModel);
	
	public Passenger(){
		super("localhost", 9898);
		communicate();
		launchLoginFrame();
	}
	
	public void launchLoginFrame(){
		loginFrame = new JFrame();
		loginFrame.setBounds(100,100,450,300);
		loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loginFrame.getContentPane().setLayout(null);
		
		JLabel lblAirlineReservationSystem = new JLabel("ARS Passenger");
		lblAirlineReservationSystem.setForeground(Color.WHITE);
		lblAirlineReservationSystem.setFont(new Font("Arial", Font.BOLD, 20));
		lblAirlineReservationSystem.setBounds(136, 11, 163, 40);
		loginFrame.getContentPane().add(lblAirlineReservationSystem);
		
		JButton btnNewUser = new JButton("New User");
		btnNewUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				socketOut.println("NEWUSER");
				JTextField [] newUser = new JTextField[3];
				for(int i = 0; i < newUser.length; i++){
					newUser[i] = new JTextField();
				}
				JPasswordField password = new JPasswordField();
				Object message [] = {
						"First Name: ", newUser[0],
						"Last Name: ", newUser[1],
						"Username: ", newUser[2],
						"Password (5 characters min): ", password
				};
				int result = JOptionPane.showConfirmDialog(null, message, "New User", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					// Check for errors
					if(newUser[0].getText().length() < 20 && newUser[1].getText().length() < 20 
							&& newUser[2].getText().length() < 20 && password.getPassword().length > 5){

						// Send string to the server
						socketOut.flush();
						socketOut.println(newUser[0].getText());
						socketOut.println(newUser[1].getText());
						socketOut.println(newUser[2].getText());
						socketOut.println(String.valueOf(password.getPassword()));
					}
					
					// Display a message saying the user was added successfully
					String response = null;
					try{
						response = socketIn.readLine();
					}catch(IOException e){
						e.printStackTrace();
					}
					
					if(response.equalsIgnoreCase("USER_ADDED")){
						JOptionPane.showMessageDialog(null, "New user added successfully","New User", JOptionPane.INFORMATION_MESSAGE);
						display_firstname = newUser[0].getText();
						display_lastname = newUser[1].getText();
						display_username = newUser[2].getText();
						
						// Launch the passenger frame
						loginFrame.setVisible(false);
						launchPassengerGUI();
					}
					else if(response.equalsIgnoreCase("FAILED"))
						JOptionPane.showMessageDialog(null, "New user added successfully","New User", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnNewUser.setFont(new Font("Arial", Font.PLAIN, 11));
		btnNewUser.setBounds(76, 203, 89, 23);
		loginFrame.getContentPane().add(btnNewUser);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextField username = new JTextField();
				JPasswordField password = new JPasswordField(); 
				
				Object message [] = {
						"Username: ", username,
						"Password: ", password
				};
				int result = JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					// Send string to the server
					socketOut.println("LOGIN");
					socketOut.println(username.getText());
					socketOut.println(String.valueOf(password.getPassword()));
					String response = "";
					try{
						response = socketIn.readLine();
					}catch(IOException e1){
						e1.printStackTrace();
					}
					// If the server returns a message saying the login was successful, display a message saying login was successful
					if(response.equalsIgnoreCase("PASS")){
						JOptionPane.showMessageDialog(null, "Login Successful","Login", JOptionPane.INFORMATION_MESSAGE);
						// Read username, firstname, lastname
						display_username = username.getText();
						try{
							display_firstname = socketIn.readLine();
							display_lastname = socketIn.readLine();
						}catch(IOException e1){
							e1.printStackTrace();
						}
						
						// Launch the passenger frame
						loginFrame.setVisible(false);
						launchPassengerGUI();
					}else{
						JOptionPane.showMessageDialog(null, "Login Unsuccessful","Login", JOptionPane.INFORMATION_MESSAGE);
					}
				}				
			}
		});
		btnLogin.setFont(new Font("Arial", Font.PLAIN, 11));
		btnLogin.setBounds(263, 203, 89, 23);
		loginFrame.getContentPane().add(btnLogin);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon("C:\\Users\\Karan\\Downloads\\airplane.jpg"));
		label.setBounds(0, 0, 434, 261);
		loginFrame.getContentPane().add(label);
		
		loginFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				closeConnection();
			}
		});
	}
	
	private void launchPassengerGUI() {
		mainFrame = new JFrame();
		mainFrame.setBounds(100, 100, 800, 700);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setVisible(true);
		
		JLabel lblArsPassenger = new JLabel("ARS Passenger");
		lblArsPassenger.setFont(new Font("Arial", Font.PLAIN, 24));
		lblArsPassenger.setBounds(300, 11, 185, 47);
		mainFrame.getContentPane().add(lblArsPassenger);
		
		JButton btnNewButton = new JButton("New Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JTextField origin = new JTextField(), 
						destination = new JTextField(), date = new JTextField();
				// Get user to input date, destination, and origin
				Object [] message = {
						"Origin: ", origin,
						"Destination: ", destination,
						"Date (mm/dd/yyyy - ex: 4/24/2017): ", date
				};
				int result = JOptionPane.showConfirmDialog(null, message, 
						"New Search", JOptionPane.OK_CANCEL_OPTION);
				if(result == JOptionPane.OK_OPTION){
					// Check entry for errors
					if(!searchInputError(origin.getText(), destination.getText(), 
							date.getText())){
						// Send input to the server
						socketOut.flush();
						socketOut.println("SEARCH_FLIGHT");
						socketOut.flush();
						socketOut.println(origin.getText());
						socketOut.println(destination.getText());
						socketOut.println(date.getText());
						
						// Read listModel<String> from server
						deserializeFlights();
						
						// Update listArea
						updateListArea();
						
						textField_dateSearch.setText(date.getText()); 
						textField_originSearch.setText(origin.getText()); 
						textField_destinationSearch.setText(destination.getText());
						
					}
					
				}
			}
		});
		btnNewButton.setBounds(10, 213, 116, 23);
		mainFrame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Clear Search");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Clear listModel;
				listModel.clear();
				listArea.setModel(listModel);
				listFlights.clear();
				displayFlights.clear();
				
				textField_dateSearch.setText(""); 
				textField_originSearch.setText(""); 
				textField_destinationSearch.setText("");
				
				updateListArea();
			}
		});
		btnNewButton_1.setBounds(146, 213, 116, 23);
		mainFrame.getContentPane().add(btnNewButton_1);
		
		listArea.setVisibleRowCount(8);
		listArea.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listPanel = new JScrollPane(listArea);
		listPanel.setBounds(10, 247, 391, 360);
		mainFrame.getContentPane().add(listPanel);
		
		JLabel lblFlightNumber = new JLabel("Flight Number:");
		lblFlightNumber.setBounds(439, 248, 83, 14);
		mainFrame.getContentPane().add(lblFlightNumber);
		
		flightNumber = new JTextField();
		lblFlightNumber.setLabelFor(flightNumber);
		flightNumber.setEditable(false);
		flightNumber.setBounds(550, 245, 224, 20);
		mainFrame.getContentPane().add(flightNumber);
		flightNumber.setColumns(10);
		
		JLabel lblFlightSource = new JLabel("Flight Source:");
		lblFlightSource.setBounds(439, 297, 83, 14);
		mainFrame.getContentPane().add(lblFlightSource);
		
		flightSource = new JTextField();
		lblFlightSource.setLabelFor(flightSource);
		flightSource.setEditable(false);
		flightSource.setColumns(10);
		flightSource.setBounds(550, 294, 224, 20);
		mainFrame.getContentPane().add(flightSource);
		
		JLabel lblFlightDestination = new JLabel("Flight Destination:");
		lblFlightDestination.setBounds(439, 345, 101, 14);
		mainFrame.getContentPane().add(lblFlightDestination);
		
		flightDestination = new JTextField();
		lblFlightDestination.setLabelFor(flightDestination);
		flightDestination.setEditable(false);
		flightDestination.setColumns(10);
		flightDestination.setBounds(550, 342, 224, 20);
		mainFrame.getContentPane().add(flightDestination);
		
		JLabel lblFlightDate = new JLabel("Flight Date:");
		lblFlightDate.setBounds(439, 393, 83, 14);
		mainFrame.getContentPane().add(lblFlightDate);
		
		flightDate = new JTextField();
		lblFlightDate.setLabelFor(flightDate);
		flightDate.setEditable(false);
		flightDate.setColumns(10);
		flightDate.setBounds(550, 390, 224, 20);
		mainFrame.getContentPane().add(flightDate);
		
		JLabel lblFlightTime = new JLabel("Departure Time:");
		lblFlightTime.setBounds(439, 440, 100, 14);
		mainFrame.getContentPane().add(lblFlightTime);
		
		flightTime = new JTextField();
		lblFlightTime.setLabelFor(flightTime);
		flightTime.setEditable(false);
		flightTime.setColumns(10);
		flightTime.setBounds(550, 437, 224, 20);
		mainFrame.getContentPane().add(flightTime);
		
		JLabel lblPrice = new JLabel("Price:");
		lblPrice.setBounds(439, 489, 83, 14);
		mainFrame.getContentPane().add(lblPrice);
		
		flightPrice = new JTextField();
		lblPrice.setLabelFor(flightPrice);
		flightPrice.setEditable(false);
		flightPrice.setColumns(10);
		flightPrice.setBounds(550, 486, 224, 20);
		mainFrame.getContentPane().add(flightPrice);
		
		JLabel lblSeatsAvailable = new JLabel("Seats Available:");
		lblSeatsAvailable.setBounds(439, 539, 101, 14);
		mainFrame.getContentPane().add(lblSeatsAvailable);
		
		seatsAvailable = new JTextField();
		lblSeatsAvailable.setLabelFor(seatsAvailable);
		seatsAvailable.setEditable(false);
		seatsAvailable.setColumns(10);
		seatsAvailable.setBounds(550, 536, 224, 20);
		mainFrame.getContentPane().add(seatsAvailable);
		
		JLabel lblUsername = new JLabel("username:");
		lblUsername.setBounds(439, 109, 83, 14);
		mainFrame.getContentPane().add(lblUsername);
		
		textField_username = new JTextField();
		lblUsername.setLabelFor(textField_username);
		textField_username.setEditable(false);
		textField_username.setBounds(550, 106, 224, 20);
		mainFrame.getContentPane().add(textField_username);
		textField_username.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setBounds(439, 137, 83, 14);
		mainFrame.getContentPane().add(lblFirstName);
		
		textField_firstName = new JTextField();
		lblFirstName.setLabelFor(textField_firstName);
		textField_firstName.setEditable(false);
		textField_firstName.setColumns(10);
		textField_firstName.setBounds(550, 134, 224, 20);
		mainFrame.getContentPane().add(textField_firstName);
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setBounds(439, 168, 83, 14);
		mainFrame.getContentPane().add(lblLastName);
		
		textField_lastName = new JTextField();
		lblLastName.setLabelFor(textField_lastName);
		textField_lastName.setEditable(false);
		textField_lastName.setColumns(10);
		textField_lastName.setBounds(550, 165, 224, 20);
		mainFrame.getContentPane().add(textField_lastName);
		
		updatePassengerInfo();
		
		JLabel lblDate = new JLabel("Date:");
		lblDate.setBounds(10, 112, 83, 14);
		mainFrame.getContentPane().add(lblDate);
		
		textField_dateSearch = new JTextField();
		lblDate.setLabelFor(textField_dateSearch);
		textField_dateSearch.setEditable(false);
		textField_dateSearch.setColumns(10);
		textField_dateSearch.setBounds(121, 109, 224, 20);
		mainFrame.getContentPane().add(textField_dateSearch);
		
		JLabel lblOrigin = new JLabel("Origin:");
		lblOrigin.setBounds(10, 140, 83, 14);
		mainFrame.getContentPane().add(lblOrigin);
		
		textField_originSearch = new JTextField();
		lblOrigin.setLabelFor(textField_originSearch);
		textField_originSearch.setEditable(false);
		textField_originSearch.setColumns(10);
		textField_originSearch.setBounds(121, 137, 224, 20);
		mainFrame.getContentPane().add(textField_originSearch);
		
		JLabel lblDestination = new JLabel("Destination:");
		lblDestination.setBounds(10, 165, 83, 14);
		mainFrame.getContentPane().add(lblDestination);
		
		textField_destinationSearch = new JTextField();
		lblDestination.setLabelFor(textField_destinationSearch);
		textField_destinationSearch.setEditable(false);
		textField_destinationSearch.setColumns(10);
		textField_destinationSearch.setBounds(121, 162, 224, 20);
		mainFrame.getContentPane().add(textField_destinationSearch);
		
		JLabel lblSearchQuery = new JLabel("Search Query:");
		lblSearchQuery.setFont(new Font("Arial", Font.PLAIN, 15));
		lblSearchQuery.setBounds(10, 76, 106, 25);
		mainFrame.getContentPane().add(lblSearchQuery);
		
		JLabel lblPassengerInfo = new JLabel("Passenger Info:");
		lblPassengerInfo.setFont(new Font("Arial", Font.PLAIN, 15));
		lblPassengerInfo.setBounds(434, 73, 106, 25);
		mainFrame.getContentPane().add(lblPassengerInfo);
		
		JLabel lblFlightInfo = new JLabel("Flight Info:");
		lblFlightInfo.setFont(new Font("Arial", Font.PLAIN, 15));
		lblFlightInfo.setBounds(434, 213, 106, 25);
		mainFrame.getContentPane().add(lblFlightInfo);
		
		//textField_firstName, textField_lastName, flightNumber
		
		JButton btnBookTicket = new JButton("Book Ticket");
		btnBookTicket.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(flightNumber.getText().isEmpty()){
					JOptionPane.showMessageDialog(null, "Please select a flight first", "Error", JOptionPane.ERROR_MESSAGE);
				}else{
					// Read flight information fields
					// Send a request to the server to book a ticket
					// Print ticket to a file
					socketOut.flush();
					socketOut.println("BOOK_TICKET");
					socketOut.flush();
					socketOut.println(textField_firstName.getText());
					socketOut.println(textField_lastName.getText());
					socketOut.println(flightNumber.getText());
					
					try{
						String response = socketIn.readLine();
						if(response.equalsIgnoreCase("BOOKED"))
							JOptionPane.showMessageDialog(null, "Ticket saved in file tickets.txt", "Booking", JOptionPane.INFORMATION_MESSAGE);
						else if(response.equalsIgnoreCase("FULL"))
							JOptionPane.showMessageDialog(null, "Flight Full", "Error", JOptionPane.ERROR_MESSAGE);
					}catch(IOException e1){
						e1.printStackTrace();
					}
				}
			}
		});
		btnBookTicket.setBounds(460, 584, 106, 23);
		mainFrame.getContentPane().add(btnBookTicket);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Clear flight information fields
				flightNumber.setText(""); flightSource.setText(""); 
				flightDestination.setText(""); flightDate.setText(""); 
				flightTime.setText(""); flightPrice.setText(""); 
				seatsAvailable.setText("");
			}
		});
		btnClear.setBounds(621, 584, 106, 23);
		mainFrame.getContentPane().add(btnClear);
		
		JButton btnViewFlight = new JButton("View Flight");
		btnViewFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get selected index from the listArea
				int index = listArea.getSelectedIndex();
				
				// Read ListModel<String> element
				String [] flightInfo = listFlights.getElementAt(index).split(";");
				
				// Update flight information fields
				flightNumber.setText(flightInfo[0]);
				flightSource.setText(flightInfo[1]);
				flightDestination.setText(flightInfo[2]);
				flightDate.setText(flightInfo[3]);
				flightTime.setText(flightInfo[4]);
				flightPrice.setText(flightInfo[8]);
				seatsAvailable.setText(flightInfo[7]);
			}
		});
		btnViewFlight.setBounds(285, 213, 116, 23);
		mainFrame.getContentPane().add(btnViewFlight);
		
		mainFrame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent we){
				closeConnection();
			}
		});
	}
	
	/**
	 * Checks for entry errors
	 * @param src Flight source
	 * @param dest Flight destination
	 * @param date Flight departure date
	 * @return
	 */
	public boolean searchInputError(String src, String dest, String date){
		if(src.matches(".*\\d+.*") || dest.matches(".*\\d+.*")){
			JOptionPane.showMessageDialog(null, "Location must be strings","Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(src.equals(dest) && !src.isEmpty()){
			JOptionPane.showMessageDialog(null, "Source and Destination cannot be the same","Error", JOptionPane.ERROR_MESSAGE);
			return true;
		}else if(!date.isEmpty()){
			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
			try{
				Date today = formatter.parse(formatter.format(new Date()));
				Date searchedDate = formatter.parse(date);
				if(searchedDate.before(today)){
					JOptionPane.showMessageDialog(null, "Source and Destination cannot be the same","Error", JOptionPane.ERROR_MESSAGE);
					return true;
				}
			}catch(ParseException e){
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * Updates the the listArea with new information
	 */
	public void updateListArea(){
		// Implement method to update listArea with new flights
		listModel.clear();
		for(int i = 0; i < displayFlights.size(); i++){
			listModel.addElement(displayFlights.getElementAt(i));
		}
		listArea.setModel(listModel);
	}
	
	/**
	 * Updates the passenger information textfields 
	 */
	public void updatePassengerInfo(){
		textField_username.setText(display_username);
		textField_firstName.setText(display_firstname); 
		textField_lastName.setText(display_lastname);
	}
	
	public static void main(String[] args) {
		Passenger window = new Passenger();
		window.loginFrame.setVisible(true);
	}

}