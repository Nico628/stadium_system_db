import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
	//private boolean logInSuccess = false;

	// Indexed some stuff for easy ctrl+f traversal
	// !!!: Constructor
	// ???: main
	// 111: guest queries
	// 222: manager queries

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
		  try { Class.forName("oracle.jdbc.driver.OracleDriver"); } catch (Exception e)
		  { System.out.println("Error"); }
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
		// TODO GET RID OF THE USER/PW. FILLED IN FOR QUICKER TESTING.
		if (connection
                (usernameField.getText(), String.valueOf(passwordField.getPassword()))
        ) {
			// if the username and password are valid,
			// remove the login window and display a text menu
			mainFrame.dispose();
			OptionSelector os = new OptionSelector(con);

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

	// ???
	public static void main(String args[]) {
		StadiumSystem ss = new StadiumSystem();

		// currently displays UI whether login is successful or not
		// I have added a new variable logInSuccess at top.
		// Eventually we will have to check whether login is successful or not.

	}
}
