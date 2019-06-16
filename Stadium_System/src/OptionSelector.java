
//User Interface

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.ArrayList;

public class OptionSelector {
	// CTRL + F INDEX:
	// !!!: constructor
	// 111: guest
	// 222: Manager

	// constructor
	// displays initial selection choices.

	// use scanner to scan for user-inputs in terminal
	// input will hold the integer value users type in
	private Scanner scan = new Scanner(System.in);
	private int input;
	private Connection con;
	private int newFanID;
	private int eventID;

	public OptionSelector(Connection conn) {
		this.con = conn;

		try {
			con.setAutoCommit(false);
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		System.out.println("Welcome to StadiumSystem! \n" + "Are you a guest or a manager at Stadium? \n"
				+ "Please type the appropriate number and press enter");

		// if they don't input correct values, the option will repeat.

		boolean repeat = true;

		while (repeat) {
			System.out.println("1. New Guest \n" + "2. Old Guest \n" + "3. Manager \n" + "4. Exit application");

			input = scan.nextInt();
			switch (input) {
			case 1:
				Guest(true);
				break;
			case 2:
				Guest(false);
				break;
			case 3:
				manager();
				break;

			// System.exit will(0) terminate the application.
			case 4:
				System.out.println("Thank you for using StadiumSystem!");
				System.exit(0);
				break;
			default:
				System.out.println("Please Enter a Valid Number:");
			}

		}

	}

	// 111
	// =============================================GUEST========================================

	private void Guest(boolean newFan) {
		boolean fanCheck = false;

		// ============GET ID================
		if (newFan == true) // create new fan ID
			newFanID = createAFan();
		else { // prompt for old ID
			System.out.println("Please Enter Your ID:");
			input = scan.nextInt();

			fanCheck = checkForExistingFan(input);

			// deal with invalid ID
			while (input < 0 || input > 10000 || fanCheck == false) {
				System.out.println("Please Enter a Valid ID:");
				input = scan.nextInt();
				fanCheck = checkForExistingFan(input);
			}

			// got the old ID
			newFanID = input;
		}

		boolean repeat = true;

		while (repeat) {
			System.out
					.println("What would you like to do today?\n" + "1. Purchase event tickets\n" + "2. Purchase food\n"
							+ "3. Purchase merchandise\n" + "4. Request for parking\n" + "5. Go Back to Menu");

			input = scan.nextInt();

			switch (input) {
			case 1:
				listEvents();
				break;
			case 2:
				listFood();
				break;
			case 3:
				listMerchandise();
				break;
			case 4:
				requestForParking();
				break;
			case 5:
				return;
			default:
				System.out.println("Please Enter a Valid Number:");

			}
		}
	}

	// iterate through events in database and list them for customers to see
	private void listEvents() {
		int numOfTickets = 0;
		boolean vip = false;

		System.out.println("Select the event you would like to attend to:");
		// list out events
		ArrayList<Integer> eList = showAllEvents();
		int rowCount = eList.size();
		System.out.println("0: Return.");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			input = scan.nextInt();
		}

		// ========Get the eventID==========
		if (input == 0) {
			return;
		} else {
			eventID = eList.get(input - 1);
		}

		// =======Ask for what type of tickets======
		System.out.println("Enter 1 For VIP Ticket, 0 For Normal Ticket:");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			input = scan.nextInt();
		}
		if (input == 1)
			vip = true;
		else
			vip = false;

		// =======Ask for how many tickets========
		System.out.println("Enter the Number of Tickets You Want to Purchase:");
		input = scan.nextInt();

		while (input < 1 || input > 999) {
			System.out.println("Please Enter a Valid Number:");
			input = scan.nextInt();
		}

		numOfTickets = input;

		// ============confirmation of purchase=======
		System.out.println("Enter 1 to Confirm Your Purchase, 0 to Return:");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			input = scan.nextInt();
		}

		System.out.println(numOfTickets + " " + eventID + " " + vip + " " + newFanID);

		// ===========fans buy tickets==============
		if (input == 1) {
			fansBuyTickets(numOfTickets, eventID, vip, newFanID);
		} else {
			return;
		}
	}

	// iterate through food in database and list them for customers to see
	private void listFood() {
		System.out.println("Which food item would you like to purchase?");
		// TODO: Add Food query

	}

	// iterate through merchandise in database and list them for customers to see
	private void listMerchandise() {
		System.out.println("Which event ticket would you like to purchase?");
		// TODO: Add Merchandise query

	}

	// iterate through open parking spots in database and list them for customers to
	// see
	private void requestForParking() {
		System.out.println("Which parking spot would you like to reserve?");
		// TODO: Add ParkingSpot Query

	}

	// 222
	// =============================================MANAGER================================================

	private void manager() {

		boolean repeat = true;
		input = scan.nextInt();

		while (repeat) {
			System.out.println("Manager selected.\n" + "What would you like to do today?\n"
					+ "1. Create new Event/Food/Merchandise\n" + "2. Delete Event/Food/Merchandise\n"
					+ "3. Employee assignment\n" + "4. Manage sponsorship\n" + "5. Look bookkeeping records");

			switch (input) {
			case 1:
				repeat = false;
				createItem();
				break;
			case 2:
				repeat = false;
				deleteItem();
				break;
			case 3:
				repeat = false;
				employeeAssginment();
				break;
			case 4:
				repeat = false;
				manageSponsorship();
				break;
			case 5:
				repeat = false;
				lookBookKeeping();
				break;
			default:
				System.out.println("Please Enter a Valid Number:");
			}

		}
	}

	private void createItem() {
		boolean repeat = true;
		input = scan.nextInt();
		while (repeat) {
			System.out.println("Create item selected.\n" + "What type of item would you like to create?\n"
					+ "1. Event\n" + "2. Food\n" + "3. Merchandise\n");

			switch (input) {
			case 1:
				repeat = false;
				createEvent();
				break;
			case 2:
				repeat = false;
				createFood();
				break;
			case 3:
				repeat = false;
				createMerchandise();
				break;
			default:
				System.out.println("Please Enter a Valid Number:");
			}
		}
	}

	// add new Event to database
	private void createEvent() {
		System.out.println("New event has been created.");
	}

	// add new Food to database
	private void createFood() {
		System.out.println("New food has been created.");
	}

	// add new Merchandise to database
	private void createMerchandise() {
		System.out.println("New merchandise has been created");
	}

	private void deleteItem() {
		boolean repeat = true;
		input = scan.nextInt();
		while (repeat) {
			System.out.println("Delete item selected.\n" + "What type of item would you like to delete?\n"
					+ "1. Event\n" + "2. Food\n" + "3. Merchandise\n");

			switch (input) {
			case 1:
				repeat = false;
				deleteEvent();
				break;
			case 2:
				repeat = false;
				deleteFood();
				break;
			case 3:
				repeat = false;
				deleteMerchandise();
				break;
			default:
				System.out.println("Please Enter a Valid Number:");
			}
		}
	}

	// for deleting items, we should probably use list methods above from Guest
	// after listing, we can choose which item to delete.

	// delete Event from database
	private void deleteEvent() {
		// listEvents();
		System.out.println("Event has been deleted.");
	}

	// delete Food from database
	private void deleteFood() {
		// listFood();
		System.out.println("Food has been deleted");
	}

	// delete Merchandise from database
	private void deleteMerchandise() {
		// listMerchandise();
		System.out.println("New merchandise has been created");
	}

	// assigns which employee works which event
	// probably should use listEvent() from Guest and another helper function
	// listEmployee() below
	private void employeeAssginment() {
		// listEvent();
		// listEmployee();
		System.out.println("THIS_EMPLOYEE will work on THIS_EVENT");
	}

	// list all employees for stadium
	private void listEmployee() {

	}

	// actually not sure what managing sponsorship is for... someone fill me in
	// but we probably should make another helper function listSponsors()
	private void manageSponsorship() {
		// listSponsors();
	}

	// lists sponsors of the Stadium
	private void listSponsors() {

	}

	// given date should return bookkeeping record for that day
	// OR we can just display the last 4 day's worth of bookkeeping like nico
	// suggested lol
	// IF NEEDED make the helper function listBookKeeping() else delete the helper
	// function
	private void lookBookKeeping() {
		// INPUT date -> bookkeeping
		// MAYBE listBookKeeping();
	}

	// return list of bookkeepings
	private void listBookKeeping() {

	}

	// =====================================Fans
	// Queries======================================
	// Queries================================================
	// 1. Create Fan:
	// Fans(Fan_ID, Phone_no, Fname, CreditCardInfo)
	private int createAFan() {
		int fanID = 0;
		try {
			Statement stmt = con.createStatement();
			int rowCount = stmt.executeUpdate("INSERT INTO Fans VALUES (fanID_counter.nextval, 'xxx', 'yyy', 'zzz')");
			con.commit();

			ResultSet rs = stmt.executeQuery("SELECT fanID_counter.currval FROM dual");
			// Get FanID*********
			if (rs.next())
				fanID = rs.getInt(1);
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fanID;
	}

	// 2. Show all events:
	// Stadium_Events(Event_id, StartTime, EndTime, TicketSold, Edate)
	private ArrayList<Integer> showAllEvents() {
		int eid = 0;
		String ename = null;
		int countCMLRow = 1;

		ArrayList<Integer> EN = new ArrayList<Integer>();

		String stringTS = new String("2001-05-18 08:15:00");
		SimpleDateFormat tm = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");
		java.util.Date utilTS1 = null;
		java.util.Date utilTS2 = null;
		try {
			utilTS1 = tm.parse(stringTS);
			utilTS2 = tm.parse(stringTS);
		} catch (ParseException pe) {
			System.out.println("Message: " + pe.getMessage());
		}

		java.sql.Timestamp sqlTS1 = new java.sql.Timestamp(utilTS1.getTime());
		java.sql.Timestamp sqlTS2 = new java.sql.Timestamp(utilTS2.getTime());

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Stadium_Events WHERE Edate >= SYSDATE ORDER BY Edate");
			// get eventID

			while (rs.next()) {
				// grab data
				eid = rs.getInt("Event_id");

				EN.add(eid);

				ename = rs.getString("EventName");
				sqlTS1 = rs.getTimestamp("StartTime");
				utilTS1.setTime(sqlTS1.getTime());
				sqlTS2 = rs.getTimestamp("EndTime");
				utilTS2.setTime(sqlTS2.getTime());

				System.out.println(countCMLRow + ". ID:" + eid + "   Name:" + ename.trim() + "   From:"
						+ tm.format(utilTS1) + "   To:" + tm.format(utilTS2));
				countCMLRow++;
				// sqlDate = rs.getDate("Edate");
				// utilDate.setTime(sqlDate.getTime());
				// System.out.println(" " + fm.format(utilDate));
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return EN;
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
			ps = con.prepareStatement(
					"SELECT FoodName, SellingPrice FROM F_sells fs JOIN (SELECT FoodName, SellingPrice FROM Food1 JOIN Food2 USING (MakingCost) WHERE Availability = 1) USING (FoodName) WHERE ? = Event_Id");

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
			ps = con.prepareStatement(
					"UPDATE F_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(FoodName) = ?");
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
			ps = con.prepareStatement(
					"SELECT MName, SellingPrice FROM M_sells JOIN (SELECT MName, SellingPrice FROM Merchandise1 m1 JOIN Merchandise2 m2 USING (MakingCost) WHERE Availability = 1) USING (MName) WHERE ? = Event_Id");

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
			ps = con.prepareStatement(
					"UPDATE M_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(MName) = ?");

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

	// 9: check if a fan exist
	// Fans(Fan_ID, Phone_no, Fname, CreditCardInfo)
	private boolean checkForExistingFan(int id) {
		PreparedStatement ps;
		int temp = -1;

		try {
			ps = con.prepareStatement("SELECT * FROM Fans WHERE ? = Fan_ID");

			ps.setInt(1, id);

			ResultSet rs = ps.executeQuery();
			if (rs.next())
				temp = rs.getInt("Fan_ID");
			if (temp == -1)
				return false;
			return true;
		} catch (SQLException ex) {
			return false;
		}
	}

}
