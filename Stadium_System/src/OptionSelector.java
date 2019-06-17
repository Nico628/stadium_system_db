
//User Interface

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Scanner;

//added by Joao
import java.io.BufferedReader;
import java.io.InputStreamReader;

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
	private String foodName;
	private String merchName;
	
	//added by Joao
	private BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	

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
			System.out.print("Input: ");

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
			System.out.print("Input: ");
			input = scan.nextInt();

			fanCheck = checkForExistingFan(input);

			// deal with invalid ID
			while (input < 0 || input > 10000 || fanCheck == false) {
				System.out.println("Please Enter a Valid ID:");
				System.out.print("Input: ");
				input = scan.nextInt();
				fanCheck = checkForExistingFan(input);
			}

			// got the old ID
			newFanID = input;
		}

		boolean repeat = true;

		while (repeat) {
			System.out.println("");
			System.out.println("What would you like to do today?\n" + "1. Purchase event tickets\n" + "2. Purchase food\n"
							+ "3. Purchase merchandise\n" + "4. Request for parking\n" + "5. Return to Menu");
			System.out.print("Input: ");
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

		System.out.println("Select the Event You Would Like to Attend to:");
		// list out events
		ArrayList<Integer> eList = showAllEvents();
		int rowCount = eList.size();
		System.out.println("0: Return.");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
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
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}
		if (input == 1)
			vip = true;
		else
			vip = false;

		// =======Ask for how many tickets========
		System.out.println("Enter the Number of Tickets You Want to Purchase:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 1 || input > 999) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		numOfTickets = input;

		// ============confirmation of purchase=======
		System.out.println("Enter 1 to Confirm Your Purchase, 0 to Return:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ===========fans buy tickets==============
		if (input == 1) {
			fansBuyTickets(numOfTickets, eventID, vip, newFanID);
			System.out.println("Your Purchase has been Confirmed. Thank you.");
		} else {
			return;
		}
	}

	// iterate through food in database and list them for customers to see
	private void listFood() {
		int rowCount = 0;
		int numOfFood = 0;
		System.out.println("Select the Event You are Attending to:");
		// list out events
		ArrayList<Integer> eList = showAllEvents();
		rowCount = eList.size();
		System.out.println("0: Return.");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ========Get the eventID==========
		if (input == 0) {
			return;
		} else {
			eventID = eList.get(input - 1);
		}

		System.out.println("Select the Food Item You Would Like to Purchase:");
		// ====================list all food items================
		ArrayList<String> fList = showAllFood(eventID);
		rowCount = fList.size();
		System.out.println("0: Return.");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ========Get the food item name==========
		if (input == 0) {
			return;
		} else {
			foodName = fList.get(input - 1);
		}

		// =======Ask for how many items========
		System.out.println("Enter the Number of the Food Items You Want to Purchase:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 1 || input > 99) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		numOfFood = input;

		// ============confirmation of purchase=======
		System.out.println("Enter 1 to Confirm Your Purchase, 0 to Return:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		System.out.println(numOfFood + " " + eventID + " " + foodName + " " + newFanID);

		// ===========fans buy tickets==============
		if (input == 1) {
			fansBuyFood(numOfFood, eventID, foodName, newFanID);
			System.out.println("Your Purchase has been Confirmed. Thank you.");
		} else {
			return;
		}
	}

	// iterate through merchandise in database and list them for customers to see
	private void listMerchandise() {
		int rowCount = 0;
		int numOfMerch = 0;
		System.out.println("Select the Event You are Attending to:");
		// list out events
		ArrayList<Integer> eList = showAllEvents();
		rowCount = eList.size();
		System.out.println("0: Return.");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ========Get the eventID==========
		if (input == 0) {
			return;
		} else {
			eventID = eList.get(input - 1);
		}

		System.out.println("Select the Merchandise Item You Would Like to Purchase:");
		// ====================list all Merchandise items================
		ArrayList<String> mList = showAllMerchandise(eventID);
		rowCount = mList.size();
		System.out.println("0: Return.");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > rowCount) { // incorrect input
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ========Get the Merchandise item name==========
		if (input == 0) {
			return;
		} else {
			merchName = mList.get(input - 1);
		}

		// =======Ask for how many items========
		System.out.println("Enter the Number of the Merchandise Items You Want to Purchase:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 1 || input > 99) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		numOfMerch = input;

		// ============confirmation of purchase=======
		System.out.println("Enter 1 to Confirm Your Purchase, 0 to Return:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// ===========fans buy merchandise==============
		if (input == 1) {
			fansBuyMerchandise(numOfMerch, eventID, merchName, newFanID);
			System.out.println("Your Purchase has been Confirmed. Thank you.");
		} else {
			return;
		}
	}

	// iterate through open parking spots in database and list them for customers to
	// see
	private void requestForParking() {
		boolean disabled = false;
		int numOfParking = 0;
		// ========type of parking============================
		System.out.println("Enter 1 for Disabled Parking, 0 for Normal Parking:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		if (input == 1)
			disabled = true;

		// =================number of parking space============
		System.out.println("Enter the Number of Parking Space You Want to Reserve:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 1 || input > 10) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		numOfParking = input;

		// ============confirmation of booking=======
		System.out.println("Enter 1 to Confirm Your Reservation, 0 to Return:");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (input < 0 || input > 1) {
			System.out.println("Please Enter a Valid Number:");
			System.out.print("Input: ");
			input = scan.nextInt();
		}

		// =================fans book parking=========
		if (input == 1) {
			fansParking(newFanID, disabled, numOfParking);
			System.out.println("Your Reservation has been Confirmed. Thank you.");
		} else {
			return;
		}
	}

	// 222
	// =============================================MANAGER================================================

	private void manager() {

		boolean repeat = true;
		
		System.out.println("");
		System.out.println("What would you like to do today?\n"
					+ "1. Create new Event/Food/Merchandise\n" + "2. Delete Event/Food/Merchandise\n"
					+ "3. Employee assignment\n" + "4. Manage sponsorship\n" + "5. Look bookkeeping records\n"
							+ "6. Return to Menu\n");
		System.out.print("Input: ");
		input = scan.nextInt();

		while (repeat) {
			

			switch (input) {
			case 1:
				createItem();
				break;
			case 2:
				deleteItem();
				break;
			case 3:
				employeeAssginment();
				break;
			case 4:
				manageSponsorship();
				break;
			case 5:
				lookBookKeeping();
				break;
			case 6:
				return;
			default:
				System.out.println("Please Enter a Valid Number:");
			}

		}
	}

	private void createItem() {
		boolean repeat = true;
		System.out.println("What type of item would you like to create?\n"
					+ "1. Event\n" + "2. Food\n" + "3. Merchandise\n");
		System.out.print("Input: ");
		input = scan.nextInt();
		while (repeat) {
			

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
    // From Joao. formally addEvent();
	private void createEvent() {


            int EventID;
			String EventName;
            Time StartTime;
            Time endTime;
            int TicketSold;
            Date EventDate;
            PreparedStatement ps;

            try{
                ps = con.prepareStatement("INSERT INTO Stadium_Events VALUES (?,?,?,?,?,?)");

                System.out.print("\nEvent ID: ");
                EventID = Integer.parseInt(in.readLine());
                ps.setInt(1, EventID);
				
				System.out.print("\nEvent Name: ");
                EventName = in.readLine();
                ps.setString(2, EventName);

                System.out.print("\nStart Time: ");
                java.util.Date parseStartTime;
                SimpleDateFormat formatStart = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try{
                    parseStartTime = formatStart.parse(in.readLine());
                    StartTime = new Time(parseStartTime.getTime());
                    ps.setTime(3, StartTime);

                } catch (ParseException e) {
                    System.out.println("Invalid Start Time");
                }

                System.out.print("\nEnd Time: ");
                java.util.Date parseEndTime;
                SimpleDateFormat formatEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try{
                    parseEndTime = formatStart.parse(in.readLine());
                    endTime = new Time(parseEndTime.getTime());
                    ps.setTime(4, endTime);

                } catch (ParseException e) {
                    System.out.println("Invalid End Time");
                }

                System.out.print("\nTickets Sold: ");
                TicketSold = Integer.parseInt(in.readLine());
                ps.setInt(5, TicketSold);

                System.out.print("\nEvent Date: ");
                java.util.Date parseDate;
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

                try{
                    parseDate = formatDate.parse(in.readLine());
                    // might be parseDate.getDate()
                    EventDate = new Date(parseDate.getTime());
                    ps.setDate(6, EventDate);

                } catch (ParseException e) {
                    System.out.println("Invalid Event Date");
                }
                ps.executeUpdate();

                con.commit();

                ps.close();


            } catch (SQLException e) {
                System.out.println("Message: " + e.getMessage());
                try{
                    con.rollback();
                } catch (SQLException e1) {
                    System.out.println("Message: " + e1.getMessage());
                    System.exit(-1);
                }
            } catch (IOException e) {
                System.out.print("IO Exception...");
            }

	}

	// add new Food to database
	private void createFood() {
        String foodName;
        boolean availability;
        int makingCost;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("INSERT INTO Food1 VALUES(?,?,?)");

            System.out.print("\nFood Name: ");
            foodName = in.readLine();
            ps.setString(1, foodName);

            System.out.print("\nAvailability: ");
            availability = Boolean.parseBoolean(in.readLine());
            ps.setBoolean(2, availability);

            System.out.print("\nMaking Cost: ");
            makingCost = Integer.parseInt(in.readLine());
            ps.setInt(3, makingCost);


            ps.executeUpdate();

            con.commit();

            ps.close();


        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            try{
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Message: " + e1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.print("IO Exception...");
        }
	}

	// add new Merchandise to database
	private void createMerchandise() {
        String merchandiseName;
        boolean availability;
        int makingCost;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("INSERT INTO Merchandise1 VALUES (?,?,?)");

            System.out.print("\nMerchandise Name: ");
            merchandiseName = in.readLine();
            ps.setString(1, merchandiseName);

            System.out.print("\nAvailability: ");
            availability = Boolean.parseBoolean(in.readLine());
            ps.setBoolean(2, availability);

            System.out.print("\nMaking Cost: ");
            makingCost = Integer.parseInt(in.readLine());
            ps.setInt(3, makingCost);


            ps.executeUpdate();

            con.commit();

            ps.close();


        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            try{
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Message: " + e1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.print("IO Exception...");
        }
	}

	private void deleteItem() {
		boolean repeat = true;
		System.out.println("What type of item would you like to delete?\n"
					+ "1. Event\n" + "2. Food\n" + "3. Merchandise\n");
		System.out.print("Input: ");
		input = scan.nextInt();
		while (repeat) {
			

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
        int EventID;
        PreparedStatement ps;

        try{
            ps = con.prepareStatement("DELETE FROM Stadium_Events WHERE Event_id = ?");
            System.out.print("\nEvent ID: ");
            EventID = Integer.parseInt(in.readLine());
            ps.setInt(1, EventID);

            int rowCount = ps.executeUpdate();

            if(rowCount == 0){
                System.out.println("\n Event " + EventID + "doesn't exist");
            }

            con.commit();

            ps.close();

        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());

            try
            {
                con.rollback();
            }
            catch (SQLException ex1)
            {
                System.out.println("Message: " + ex1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
	}

	// delete Food from database
	private void deleteFood() {
        String foodName;
        PreparedStatement ps;

        try{
            ps = con.prepareStatement("DELETE FROM Food1 WHERE FoodName = ?");
            System.out.print("\nFood Name: ");
            foodName = in.readLine();
            ps.setString(1, foodName);

            int rowCount = ps.executeUpdate("DELETE FROM Food1 WHERE FoodName = " + foodName);

            if(rowCount == 0){
                System.out.println("\n Food " + foodName + " doesn't exist");
            }

            con.commit();

            ps.close();
			if(rowCount > 0){
				System.out.println("\n Delete complete");
			}

        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());

            try
            {
                con.rollback();
            }
            catch (SQLException ex1)
            {
                System.out.println("Message: " + ex1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
	}

	// delete Merchandise from database
	private void deleteMerchandise() {
        String merchName;
        PreparedStatement ps;

        try{
            ps = con.prepareStatement("DELETE FROM Merchandise1 WHERE MName = ?");
            System.out.print("\nMerchandise Name: ");
            merchName = in.readLine();
            ps.setString(1, merchName);

            int rowCount = ps.executeUpdate("DELETE FROM Merchandise1 WHERE MName = " + merchName);

            if(rowCount == 0){
                System.out.println("\n Merchandise " + merchName + " doesn't exist");
            }

            con.commit();

            ps.close();
			
			if(rowCount > 0){
				System.out.println("\n Delete complete");
			}

        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());

            try
            {
                con.rollback();
            }
            catch (SQLException ex1)
            {
                System.out.println("Message: " + ex1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
	}

	// assigns which employee works which event
	// probably should use listEvent() from Guest and another helper function
	// listEmployee() below
	private void employeeAssginment() {
        int employeeID;
        int eventID;
        boolean availability;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("INSERT INTO WorksAt VALUES (?,?,?)");

            System.out.print("\nEmployee ID: ");
            employeeID = Integer.parseInt(in.readLine());
            ps.setInt(1, employeeID);

            System.out.print("\nEvent ID: ");
            eventID = Integer.parseInt(in.readLine());
            ps.setInt(2, eventID);

            System.out.print("\nAvailability: ");
            availability = Boolean.parseBoolean(in.readLine());
            ps.setBoolean(3, availability);


            ps.executeUpdate();

            con.commit();

            ps.close();


        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            try{
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Message: " + e1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.print("IO Exception...");
        }
	}

	// list all employees for stadium
	private void listEmployee() {

	}

	// actually not sure what managing sponsorship is for... someone fill me in
	// but we probably should make another helper function listSponsors()
	private void manageSponsorship() {
        String companyName;
        int eventID;
        int sizeOfAd;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("INSERT INTO Sponsors (?,?,?)");

            System.out.print("\nCompany Name: ");
            companyName = in.readLine();
            ps.setString(1, companyName);

            System.out.print("\nEvent ID: ");
            eventID = Integer.parseInt(in.readLine());
            ps.setInt(2, eventID);

            System.out.print("\nMaking Cost: ");
            sizeOfAd = Integer.parseInt(in.readLine());
            ps.setInt(3, sizeOfAd);


            ps.executeUpdate();

            con.commit();

            ps.close();


        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            try{
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Message: " + e1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.print("IO Exception...");
        }
	}

	// lists sponsors of the Stadium
    // delete if not needed
	private void listSponsors() {

	}

	// given date should return bookkeeping record for that day
	// OR we can just display the last 4 day's worth of bookkeeping like nico
	// suggested lol
	// IF NEEDED make the helper function listBookKeeping() else delete the helper
	// function
	private void lookBookKeeping() {
        String eventDate;
        String income;
        String expense;
        String attendance;
        String netIncome;
        ResultSet rs;
        Statement s;

        try {
            s = con.createStatement();

            rs = s.executeQuery("SELECT * FROM BookKeeping1");

            ResultSetMetaData rsmd = rs.getMetaData();
            int numCols = rsmd.getColumnCount();

            System.out.println(" ");

            for(int i = 0; i < numCols; i++){
                System.out.printf("%-15s", rsmd.getColumnName(i + 1));
            }

            System.out.println(" ");

            while(rs.next()){
                eventDate = rs.getString("Event Date");
                System.out.printf("%-10.10s", eventDate);

                income = rs.getString("Income");
                System.out.printf("%-20.20s", income);

                expense = rs.getString("Expense");
                System.out.printf("%-20.20s", expense);

                attendance = rs.getString("Attendance");
                System.out.printf("%-15.15s", attendance);

                netIncome = rs.getString("Net Income");
                System.out.printf("%-15.15s", netIncome);

            }

            s.close();

        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
        }
	}

	// return list of bookkeepings
	private void listBookKeeping() {

	}

    private void createEmployee(){
        int employeeID;
        String employeeName;
        int phoneNumber;
        String occupation;
        PreparedStatement ps;

        try {
            ps = con.prepareStatement("INSERT INTO Employee VALUES (?,?,?,?)");

            System.out.print("\nEmployee ID: ");
            employeeID = Integer.parseInt(in.readLine());
            ps.setInt(1, employeeID);

            System.out.print("\nEmployee Name: ");
            employeeName = in.readLine();
            ps.setString(2, employeeName);

            System.out.print("\nPhone Number: ");
            phoneNumber = Integer.parseInt(in.readLine());
            ps.setInt(3, phoneNumber);

            System.out.print("\nOccupation: ");
            occupation = in.readLine();
            ps.setString(3, occupation);


            ps.executeUpdate();

            con.commit();

            ps.close();


        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());
            try{
                con.rollback();
            } catch (SQLException e1) {
                System.out.println("Message: " + e1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.print("IO Exception...");
        }
    }

    private void deleteEmployee(){
        int employeeID;
        PreparedStatement ps;

        try{
            ps = con.prepareStatement("DELETE FROM Employee WHERE Employee_id = ?");
            System.out.print("\nEmployee ID: ");
            employeeID = Integer.parseInt(in.readLine());
            ps.setInt(1, employeeID);

            int rowCount = ps.executeUpdate();

            if(rowCount == 0){
                System.out.println("\n Employee " + employeeID + "doesn't exist");
            }

            con.commit();

            ps.close();

        } catch (SQLException e) {
            System.out.println("Message: " + e.getMessage());

            try
            {
                con.rollback();
            }
            catch (SQLException ex1)
            {
                System.out.println("Message: " + ex1.getMessage());
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }

	// =====================================FansQueries======================================
	//
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
			System.out.println("All events are fulled. We apologize for any inconvenience caused.");
			System.out.println("Thank you for using StadiumSystem!");
			System.exit(0);
		}

		System.out.println("Please keep a record of your ID: " + fanID);
		return fanID;
	}

	// 2. Show all events:
	// Stadium_Events(Event_id, StartTime, EndTime, TicketSold, Edate)
	// Concerts(Event_id, Performer, Capacity)
	// Games(Event_id, HomeTeam, AwayTeam)
	// Team(HomeTeam, sportsType)
	
	private ArrayList<Integer> showAllEvents() {
		int eid = 0;
		String ename = null;
		int countCMLRow = 1;
		String perf = null;
		String sports = null;
		String ht = null;
		String at = null;

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
			
			// showing concerts
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM Stadium_Events JOIN Concerts USING (Event_id) WHERE Edate >= SYSDATE ORDER BY Edate");

			System.out.println("Concerts:");
			while (rs.next()) {
				// grab data
				eid = rs.getInt("Event_id");
				EN.add(eid);
				
		
				ename = rs.getString("EventName");
				sqlTS1 = rs.getTimestamp("StartTime");
				utilTS1.setTime(sqlTS1.getTime());
				sqlTS2 = rs.getTimestamp("EndTime");
				utilTS2.setTime(sqlTS2.getTime());
				perf = rs.getString("Performer");

				System.out.println(countCMLRow + ". ID: " + eid + "   Name: " + ename.trim() + "   From: "
						+ tm.format(utilTS1) + "   To: " + tm.format(utilTS2) + "   Performer: " + perf.trim());
				countCMLRow++;
			}
			
			
			// showing games
			
			rs = stmt.executeQuery("SELECT * FROM (SELECT * FROM Games JOIN Stadium_Events USING (Event_id)) JOIN Team USING (HomeTeam) WHERE Edate >= SYSDATE ORDER BY Edate");

			System.out.println("Games:");
			while (rs.next()) {
				// grab data
				eid = rs.getInt("Event_id");
				EN.add(eid);
				
				ht = rs.getString("HomeTeam");
				at  = rs.getString("AwayTeam");
				ename = rs.getString("EventName");
				sqlTS1 = rs.getTimestamp("StartTime");
				utilTS1.setTime(sqlTS1.getTime());
				sqlTS2 = rs.getTimestamp("EndTime");
				utilTS2.setTime(sqlTS2.getTime());
				sports = rs.getString("sportsType");

				System.out.println(countCMLRow + ". ID: " + eid + "   Name: " + ename.trim() + "   From: "
						+ tm.format(utilTS1) + "   To: " + tm.format(utilTS2) + "   Sports: " + sports.trim() + "   HomeTeam: " + ht.trim() + "   AwayTeam: " + at.trim());
				countCMLRow++;
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
		
		
		// update ticketsSold in Stadium_Events
		try {
			ps = con.prepareStatement("UPDATE Stadium_Events SET TicketSold = TicketSold + ? WHERE Event_id = ?");
			
			ps.setInt(1, num);
			ps.setInt(2, event_id);

			ps.executeUpdate();

			// commit work
			con.commit();
			
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		
		

	}

	// 4. Show all Food:
	// Food1(FoodName, Availability, MakingCost)
	// Food2(MakingCost, SellingPrice)
	// F_sells(Event_Id, FoodName, Quantity)

	private ArrayList<String> showAllFood(int event_id) {
		PreparedStatement ps;
		String fname = null;
		int sellingPrice = 0;
		int rowCount = 1;
		ArrayList<String> fn = new ArrayList<String>();

		try {
			ps = con.prepareStatement(
					"SELECT FoodName, SellingPrice FROM F_sells fs JOIN (SELECT FoodName, SellingPrice FROM Food1 JOIN Food2 USING (MakingCost) WHERE Availability = 1) USING (FoodName) WHERE ? = Event_Id");

			ps.setInt(1, event_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// grab data
				fname = rs.getString("FoodName");
				fn.add(fname);
				sellingPrice = rs.getInt("SellingPrice");
				System.out.println(rowCount + ". " + fname + "  $" + sellingPrice);
				rowCount++;
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}

		return fn;
	}

	// 5. Fans buy Food
	// F_sells(Event_Id, FoodName, Quantity)
	// F_buys(Fan_ID, FoodName, Quantity)

	private void fansBuyFood(int num, int event_id, String foodName, int fanID) {
		PreparedStatement ps;
		int quan = -1;

		try {
			ps = con.prepareStatement(
					"UPDATE F_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(FoodName) = ?");
			ps.setInt(1, num);
			ps.setInt(2, event_id);
			ps.setString(3, foodName);

			ps.executeUpdate();

			// commit work
			con.commit();

			ps = con.prepareStatement("SELECT * FROM F_buys WHERE Fan_ID = ? AND FoodName = ?");
			ps.setInt(1, fanID);
			ps.setString(2, foodName);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				quan = rs.getInt("Quantity");

			if (quan == -1) {
				ps = con.prepareStatement("INSERT INTO F_buys VALUES (?,?,?)");

				ps.setInt(1, fanID);
				ps.setString(2, foodName);
				ps.setInt(3, num);

				ps.executeUpdate();

				// commit work
				con.commit();
			} else {
				ps = con.prepareStatement(
						"UPDATE F_buys SET Quantity = Quantity + ? WHERE Fan_ID = ? AND FoodName = ?");
				ps.setInt(1, num);
				ps.setInt(2, fanID);
				ps.setString(3, foodName);

				ps.executeUpdate();

				// commit work
				con.commit();
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
	}

	// 6. Show all Merchandise
	// Merchandise1(MName, Availability, MakingCost)
	// Merchandise2(MakingCost, SellingPrice)
	// M_sells(Event_Id, MName, Quantity)

	private ArrayList<String> showAllMerchandise(int event_id) {
		PreparedStatement ps;
		String mname = null;
		int sellingPrice = 0;
		ArrayList<String> MN = new ArrayList<String>();
		int rowCount = 1;

		try {
			ps = con.prepareStatement(
					"SELECT MName, SellingPrice FROM M_sells JOIN (SELECT MName, SellingPrice FROM Merchandise1 m1 JOIN Merchandise2 m2 USING (MakingCost) WHERE Availability = 1) USING (MName) WHERE ? = Event_Id");

			ps.setInt(1, event_id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// grab data
				mname = rs.getString("MName");
				MN.add(mname);
				sellingPrice = rs.getInt("SellingPrice");
				System.out.println(rowCount + ". " + mname + "  $" + sellingPrice);
				rowCount++;
			}
		} catch (SQLException ex) {
			System.out.println("Message: " + ex.getMessage());
		}
		return MN;
	}

	// 7. Fans buy Merchandise
	// M_sells(Event_Id, MName, Quantity)
	// M_buys(Fan_ID, MName, Quantity)

	private void fansBuyMerchandise(int num, int event_id, String MName, int fanID) {
		PreparedStatement ps;
		int quan = -1;

		try {
			ps = con.prepareStatement(
					"UPDATE M_sells SET Quantity = Quantity + ? WHERE Event_Id = ? AND rtrim(MName) = ?");

			ps.setInt(1, num);
			ps.setInt(2, event_id);
			ps.setString(3, MName);

			ps.executeUpdate();

			// commit work
			con.commit();

			ps = con.prepareStatement("SELECT * FROM M_buys WHERE Fan_ID = ? AND MName = ?");
			ps.setInt(1, fanID);
			ps.setString(2, MName);
			ResultSet rs = ps.executeQuery();

			if (rs.next())
				quan = rs.getInt("Quantity");

			if (quan == -1) {
				ps = con.prepareStatement("INSERT INTO M_buys VALUES (?,?,?)");

				ps.setInt(1, fanID);
				ps.setString(2, MName);
				ps.setInt(3, num);

				ps.executeUpdate();

				// commit work
				con.commit();
			} else {
				ps = con.prepareStatement("UPDATE M_buys SET Quantity = Quantity + ? WHERE Fan_ID = ? AND MName = ?");
				ps.setInt(1, num);
				ps.setInt(2, fanID);
				ps.setString(3, MName);

				ps.executeUpdate();

				// commit work
				con.commit();
			}
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
