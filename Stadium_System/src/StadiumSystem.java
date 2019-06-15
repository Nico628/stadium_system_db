import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class StadiumSystem implements ActionListener {
	// command line reader
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private Connection con;
	// user is allowed 3 login attempts
	private int loginAttempts = 3;
	// components of the login window
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JFrame mainFrame;
	private JFrame stadiumFrame;
    private boolean logInSuccess = false;

    //Indexed some stuff for easy ctrl+f traversal
    //!!!: Constructor
    //???: main
    //111: guest queries
    //222: manager queries

    // constructor of login & driver loading, registration
    // !!!
	// constructor of login & driver loading, registration
	public StadiumSystem() {
		oracleLogin();

		// Load & register the Oracle JDBC driver
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			System.exit(-1);
		}

		// Don't delete pls
        // alternate try/catch
		/*
           try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
            } catch (Exception e) {
                System.out.println("Error");
            }
		 */
	}

	// oracle login
	private void oracleLogin() {
		mainFrame = new JFrame("User Login");

		JLabel usernameLabel = new JLabel("Enter username: ");
		JLabel passwordLabel = new JLabel("Enter password: ");

		usernameField = new JTextField(10);
		passwordField = new JPasswordField(10);
		passwordField.setEchoChar('*');

		JButton loginButton = new JButton("Log In");

		JPanel contentPane = new JPanel();
		mainFrame.setContentPane(contentPane);

		// layout components using the GridBag layout manager
		GridBagLayout gb = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		contentPane.setLayout(gb);
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// place the username label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(10, 10, 5, 0);
		gb.setConstraints(usernameLabel, c);
		contentPane.add(usernameLabel);

		// place the text field for the username
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(10, 0, 5, 10);
		gb.setConstraints(usernameField, c);
		contentPane.add(usernameField);

		// place password label
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.insets = new Insets(0, 10, 10, 0);
		gb.setConstraints(passwordLabel, c);
		contentPane.add(passwordLabel);

		// place the password field
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(0, 0, 10, 10);
		gb.setConstraints(passwordField, c);
		contentPane.add(passwordField);

		// place the login button
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(5, 10, 10, 10);
		c.anchor = GridBagConstraints.CENTER;
		gb.setConstraints(loginButton, c);
		contentPane.add(loginButton);

		// register password field and OK button with action event handler
		passwordField.addActionListener(this);
		loginButton.addActionListener(this);

		// anonymous inner class for closing the window
		mainFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// size the window to obtain a best fit for the components
		mainFrame.pack();

		// center the frame
		Dimension d = mainFrame.getToolkit().getScreenSize();
		Rectangle r = mainFrame.getBounds();
		mainFrame.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

		// make the window visible
		mainFrame.setVisible(true);

		// place the cursor in the text field for the username
		usernameField.requestFocus();
	}

	// Connect to database
	private boolean connection(String username, String password) {
		String connectURL = "jdbc:oracle:thin:@dbhost.students.cs.ubc.ca:1522:stu";
		try {
			con = DriverManager.getConnection(connectURL, username, password);
			System.out.println("\nConnected to Oracle!");
			return true;
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
			return false;
		}
	}

	// handler for login window
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (connection(usernameField.getText(), String.valueOf(passwordField.getPassword()))) {
			// if the username and password are valid,
			// remove the login window and display a text menu
			mainFrame.dispose();
			testing();
			//mainWindow();

		} else {
			loginAttempts--;
			if (loginAttempts <= 0) {
				mainFrame.dispose();
				System.exit(-1);
			} else {
				// clear the password
				passwordField.setText("");
			}
		}

	}

	private void mainWindow() {
		stadiumFrame = new JFrame("Welcome to the Stadium");

		JButton fanButton = new JButton("I am a Fan");
		JButton managerButton = new JButton("I am a Manager");

		JPanel stadiumPane = new JPanel();
		stadiumFrame.setContentPane(stadiumPane);

		// layout components using the GridBag layout manager
		GridBagLayout gb1 = new GridBagLayout();
		GridBagConstraints c1 = new GridBagConstraints();

		stadiumPane.setLayout(gb1);
		stadiumPane.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

		// place the fan button
		c1.gridwidth = GridBagConstraints.REMAINDER;
		c1.insets = new Insets(40, 40, 40, 40);
		c1.anchor = GridBagConstraints.CENTER;
		gb1.setConstraints(fanButton, c1);
		stadiumPane.add(fanButton);

		// place the manager button
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridwidth = GridBagConstraints.REMAINDER;
		c2.insets = new Insets(20, 20, 20, 20);
		c2.anchor = GridBagConstraints.CENTER;
		gb1.setConstraints(managerButton, c2);
		stadiumPane.add(managerButton);

		// register password field and OK button with action event handler
		fanButton.addActionListener(this);
		managerButton.addActionListener(this);

		// anonymous inner class for closing the window
		stadiumFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// size the window to obtain a best fit for the components
		stadiumFrame.pack();

		// center the frame
		Dimension d = stadiumFrame.getToolkit().getScreenSize();
		Rectangle r = stadiumFrame.getBounds();
		stadiumFrame.setLocation((d.width - r.width) / 2, (d.height - r.height) / 2);

		// make the window visible
		stadiumFrame.setVisible(true);

	}

	//???
	public static void main(String args[]) {
		StadiumSystem ss = new StadiumSystem();

        // currently displays UI whether login is successful or not
        // I have added a new variable logInSuccess at top.
        // Eventually we will have to check whether login is successful or not.
        //OptionSelector os = new OptionSelector();
	}

	private void testing() {
		// create fan id
		int newFanID = createAFan();
		System.out.println("createAFan Testing: " + newFanID + "\n");

		// show all events
		System.out.println("showAllEvents Testing:");
		showAllEvents();
		System.out.println("\n");

		// fans buy tickets
		fansBuyTickets(2, 100, false, newFanID);

		// show all food selling in that event
		System.out.println("showAllFood Testing:");
		showAllFood(100);
		System.out.println("\n");

		// fans buy food
		fansBuyFood(3, 100, "Hotdog", newFanID);

		// show all Merchandise
		System.out.println("showAllMerchandise Testing:");
		showAllMerchandise(100);
		System.out.println("\n");

		// fans buy merchandise
		fansBuyMerchandise(2, 100, "Wallet", newFanID);

		// fans parking
		fansParking(newFanID, false, 1);
	}

	//111
	//============================================== All Guests' Queries================================================
	// 1. Create Fan:
	// Fans(Fan_ID, Phone_no, Fname, CreditCardInfo)
	private int createAFan() {
		int fanID = 0;
		try {
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			int rowCount = stmt.executeUpdate("INSERT INTO Fans VALUES (fanID_counter.nextval, 'xxx', 'yyy', 'zzz')");
			con.commit();

			ResultSet rs = stmt.executeQuery("SELECT fanID_counter.currval FROM dual");
			// Get FanID*********
			if(rs.next())
				fanID = rs.getInt(1);
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fanID;
	}

	// 2. Show all events:
	// Stadium_Events(Event_id, StartTime, EndTime, TicketSold, Edate)
	private void showAllEvents() {
		int eid = 0;
		String ename = null;
		// String stringDate = new String("18/08/01");
		// SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yy");
		String stringTS = new String("2001-05-18 08:15:00");
		SimpleDateFormat tm = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		// java.util.Date utilDate = null;
		java.util.Date utilTS1 = null;
		java.util.Date utilTS2 = null;
		try {
			// utilDate = fm.parse(stringDate);
			utilTS1 = tm.parse(stringTS);
			utilTS2 = tm.parse(stringTS);
		} catch(ParseException pe) {
			System.out.println("Message: " + pe.getMessage());
		}

		// java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		java.sql.Timestamp sqlTS1 = new java.sql.Timestamp(utilTS1.getTime());
		java.sql.Timestamp sqlTS2 = new java.sql.Timestamp(utilTS2.getTime());

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Stadium_Events WHERE Edate >= SYSDATE ORDER BY Edate");
			// get eventID

			while (rs.next()) {
				// grab data
				eid = rs.getInt("Event_id");
				ename = rs.getString("EventName");

				sqlTS1 = rs.getTimestamp("StartTime");
				utilTS1.setTime(sqlTS1.getTime());
				sqlTS2 = rs.getTimestamp("EndTime");
				utilTS2.setTime(sqlTS2.getTime());

				System.out.println(eid  + ", " + ename + " " + tm.format(utilTS1) + " " + tm.format(utilTS2));

				// sqlDate = rs.getDate("Edate");
				// utilDate.setTime(sqlDate.getTime());
				// System.out.println(" " + fm.format(utilDate));
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 3. Fans buy ticket:
	// Tickets(Event_id, Seat_no, Section_no, Cost, Availability, Fan_ID)
	// VIP - $100, Normal = $50

	private void fansBuyTickets(int num, int event_id, Boolean VIP, int fanID) {
		PreparedStatement ps = null;

		if (VIP) { // vip tickets
			try {
				for (int i = 0; i < num; i++) {
					ps = con.prepareStatement("INSERT INTO Tickets VALUES (? , MOD(vip_seat_counter.nextval, 100)"
							+ ", vip_seat_counter.currval/100, 100, 0, ?)");
					ps.setInt(1, event_id);
					ps.setInt(2, fanID);

					ps.executeUpdate();

					// commit work
					con.commit();
				}
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
				// might have to deal with vip seats fulled here************
			}
		} else { // normal tickets
			try {
				for (int i = 0; i < num; i++) {
					ps = con.prepareStatement("INSERT INTO Tickets VALUES (? , MOD(normal_seat_counter.nextval, 100)"
							+ ", normal_seat_counter.currval/100, 50, 0, ?)");
					ps.setInt(1, event_id);
					ps.setInt(2, fanID);

					ps.executeUpdate();

					// commit work
					con.commit();
				}
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
				// might have to deal with normal seats fulled here************
			}
		}

	}

	// 4. Show all Food:
	// Food1(FoodName, Availability, MakingCost)
	// Food2(MakingCost, SellingPrice)
	// F_sells(Event_Id, FoodName, Quantity)

	private void showAllFood(int event_id) {
		PreparedStatement ps;
		String fname = null;
		int sellingPrice = 0;

		try {
			ps = con.prepareStatement("SELECT FoodName, SellingPrice FROM F_sells fs JOIN (SELECT FoodName, SellingPrice FROM Food1 JOIN Food2 USING (MakingCost) WHERE Availability = 1) USING (FoodName) WHERE ? = Event_Id");

			ps.setInt(1, event_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// grab data
				fname = rs.getString("FoodName");
				sellingPrice = rs.getInt("SellingPrice");
				System.out.println(fname + " " + sellingPrice);
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 5. Fans buy Food
	// F_sells(Event_Id, FoodName, Quantity)
	// F_buys(Fan_ID, FoodName, Quantity)

	private void fansBuyFood(int num, int event_id, String foodName, int fanID) {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement("UPDATE F_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(FoodName) = ?");
			ps.setInt(1, num);
			ps.setInt(2, event_id);
			ps.setString(3, foodName);

			ps.executeUpdate();

			// commit work
			con.commit();

			ps = con.prepareStatement("INSERT INTO F_buys VALUES (?,?,?)");
			ps.setInt(1, fanID);
			ps.setString(2, foodName);
			ps.setInt(3, num);

			ps.executeUpdate();

			// commit work
			con.commit();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 6. Show all Merchandise
	// Merchandise1(MName, Availability, MakingCost)
	// Merchandise2(MakingCost, SellingPrice)
	// M_sells(Event_Id, MName, Quantity)

	private void showAllMerchandise(int event_id) {
		PreparedStatement ps;
		String mname = null;
		int sellingPrice = 0;

		try {
			ps = con.prepareStatement("SELECT MName, SellingPrice FROM M_sells JOIN (SELECT MName, SellingPrice FROM Merchandise1 m1 JOIN Merchandise2 m2 USING (MakingCost) WHERE Availability = 1) USING (MName) WHERE ? = Event_Id");

			ps.setInt(1, event_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// grab data
				mname = rs.getString("MName");
				sellingPrice = rs.getInt("SellingPrice");
				System.out.println(mname + " " + sellingPrice);
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 7. Fans buy Merchandise
	// M_sells(Event_Id, MName, Quantity)
	// M_buys(Fan_ID, MName, Quantity)

	private void fansBuyMerchandise(int num, int event_id, String MName, int fanID) {
		PreparedStatement ps;

		try {
			ps = con.prepareStatement("UPDATE M_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(MName) = ?");

			ps.setInt(1, num);
			ps.setInt(2, event_id);
			ps.setString(3, MName);

			ps.executeUpdate();

			// commit work
			con.commit();

			ps = con.prepareStatement("INSERT INTO M_buys VALUES(?,?,?)");
			ps.setInt(1, fanID);
			ps.setString(2, MName);
			ps.setInt(3, num);

			ps.executeUpdate();

			// commit work
			con.commit();
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 8. Fans Parking
	// Fans(Fan_ID, Phone_no, Fname, CreditCardInfo)
	// ParkingSpace(Floor_no, Lot_no, Availability, Fan_ID, Disabled)

	private void fansParking(int fanID, Boolean disabled, int num) {
		PreparedStatement ps = null;

		if (disabled) {
			try {
				for (int i = 0; i < num; i++) {
					ps = con.prepareStatement(
							"INSERT INTO ParkingSpace VALUES(" + "disabled_parking_counter.nextval/100, "
									+ "MOD(disabled_parking_counter.currval, 100)," + "0, ?, 1)");
					ps.setInt(1, fanID);

					ps.executeUpdate();

					// commit work
					con.commit();
				}
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		} else {
			try {
				for (int i = 0; i < num; i++) {
					ps = con.prepareStatement(
							"INSERT INTO ParkingSpace VALUES(" + "normal_parking_counter.nextval/100, "
									+ "MOD(normal_parking_counter.currval, 100)," + "0, ?, 0)");
					ps.setInt(1, fanID);

					ps.executeUpdate();

					// commit work
					con.commit();
				}
			} catch (SQLException ex) {
				System.out.println("Message: " + ex.getMessage());
			}
		}
	}

    //222
	//Manager queries
    //============================================== All Guests' Queries================================================

	// QUIT PROGRAM:
	private void allDone() {
		return;
	}
}
