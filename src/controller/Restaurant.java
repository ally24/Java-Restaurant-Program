package controller;
import model.*;
import view.*;
import java.util.*;

/**
 * Main controller of program. Creates all the employee, table, and menu data sets. Displays main menu to user, where user can
 * choose to work as an employee (existing or new), view menu, or exit. 
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class Restaurant {

//	private ArrayList<Table> tables;
//	private Map<String, ArrayList<Employee>> employees;
	private Map<Integer, Employee> employees;
	private Map<Integer, Table> tables;
	private ArrayList<MenuItem> menu;
	UIConsole helper;
	
	public Restaurant() {
//		tables = new ArrayList<>();
		tables = new HashMap<>();
//		employees = new HashMap<String, ArrayList<Employee>>();
		employees = new HashMap<Integer, Employee>();
		setUpMenu();
		setUpTables();
		setUpEmployees();
		helper = new UIConsole();
	}
	
	/**
	 * Creates 10 tables for the restaurant, each one with a random number of seats from 1 to 7
	 */
	private void setUpTables() {
//		int t = 100;
//		for (int i = 0; i < 11; i++) {
//			tables.add(new Table(t, 4, TableStatus.EMPTY, menu));
//			t++;
//		}
		Random gen = new Random();
		
		for (int i = 0; i < 10; i++) {
			tables.put((100 + i), new Table(gen.nextInt(7) + 1, menu));
		}
	}

	/**
	 * Calls MenuFileReader, which reads a txt file to generate and return a list of MenuItems
	 */
	private void setUpMenu() {
		//get menu from the MenuFileReader
		menu = MenuFileReader.readMenuData("bin/menu.txt");
	}
	
	/**
	 * Sets up initial 4 employees (2 servers, 1 cook, 1 manager), but user can add more in main menu
	 */
	private void setUpEmployees() {
		
//	Before switch from Map<String, ArrayList<Employee>> to Map<Integer, Employee>
//		ArrayList<Employee> servers = new ArrayList<>();
//		servers.add(new Server("Jane", "3059683948", "janedoe@gmail.com", false, 3, 1001, tables));
//		servers.add(new Server("Bob", "3957493759", "bob@hotmail.com", false, 2, 1002, tables));
//		ArrayList<Employee> cooks = new ArrayList<>();
//		cooks.add(new Cook("John", "3045938294", "johndoe@gmail.com", false, 2001, menu, tables));
//		ArrayList<Employee> managers = new ArrayList<>();
//		managers.add(new Manager("Kendra Walthern", "3950493849", "kwalther@usc.edu", false, 3001, employees, tables, menu));
//		employees.put("Servers", servers);
//		employees.put("Cooks", cooks);
//		employees.put("Managers", managers);
		
		employees.put(1001, new Server("Jane", "3059683948", "janedoe@gmail.com", false, 3));
		employees.put(1002, new Server("Bob", "3957493759", "bob@hotmail.com", false, 2));
		employees.put(2001, new Cook("John", "3045938294", "johndoe@gmail.com", false, menu, tables));
		employees.put(3001, new Manager("Kendra", "3950493849", "kwalther@usc.edu", false, employees, tables, menu));
	}
	
	/**
	 * Displays main menu where user can decide to do something or exit
	 */
	private void run() {
		int userNum;
		do {
			helper.print("\nChoose one of the following: ");
			helper.print("1. Clock in");
			helper.print("2. Clock out");
			helper.print("3. Create new employee profile");
			helper.print("4. Work");
			helper.print("5. View Food Menu");
			helper.print("6. Quit");
			userNum = helper.inputInt("Please enter number 1-6", 1, 6);
			switch(userNum) {
			case 1: 
				clockIn();
				break;
			case 2:
				clockOut();
				break;
			case 3:
				addNewEmp();
				break;
			case 4:
				workLogin();
				break;
			case 5:
				viewMenu();
				break;
			}
			
		} while (userNum != 6);
		
		helper.print("\nGoodbye!");
		
//		for (Table t: tables.values()) {
//		System.out.println("Table: " + t.getStatus() + t.isPickedUpByServer());
//	}
		
	}
	
	/**
	 * Adds a new employee to the employees map
	 * First, it asks if the new employee is a server, cook, or manager
	 * Second, it asks for the new employee information
	 * Third, it asks for Server/Cook/Manager-specific information, creates an object of that type, and adds it to the employees map
	 */
	private void addNewEmp() {
		helper.print("\t1. Server");
		helper.print("\t2. Cook");
		helper.print("\t3. Manager");
		int userNum = helper.inputInt("Are you a server, cook, or manager? (1-3)", 1, 3);
		
		String name = helper.inputLine("What is your name?");
		String phoneNumber = helper.inputLine("What is your phone number?");
		String email = helper.inputLine("What is your email?");
		
		int password = helper.inputInt("What would you like your 4 digit password to be?", 999, 10000);
		while (employees.containsKey(password) == true) {
			password = helper.inputInt("Another employee has that number. Please choose a different 4 digit number.", 999, 10000);
		}
		
		switch (userNum){
		case 1: 
			int maxTables = helper.inputInt("How many tables can you manage at most?");
//			ArrayList<Employee> currServers = employees.get("Servers");
//			currServers.add(new Server(name, phoneNumber, email, false, password, tables));
			employees.put(password, new Server(name, phoneNumber, email, false, maxTables));
			helper.print(name + " has been added to the system. Please clock in before working.");
			break;
		case 2:
//			ArrayList<Employee> currCooks = employees.get("Cooks");
//			currCooks.add(new Cook(name, phoneNumber, email, false, password, menu, tables));
			employees.put(password, new Cook(name, phoneNumber, email, false, menu, tables));
			helper.print(name + " has been added to the system. Please clock in before working.");
			break;
		case 3:
//			ArrayList<Employee> currManagers = employees.get("Managers");
//			currManagers.add(new Manager(name, phoneNumber, email, false, password, employees, tables, menu));
			employees.put(password, new Manager(name, phoneNumber, email, false, employees, tables, menu));
			helper.print(name + " has been added to the system. Please clock in before working.");
			break;
		}
			
	}

	/**
	 * Calls choosePosition() to get the employee the user would like to clock in as
	 * Changes chosen employee to clocked in and then calls work with that employee as a parameter
	 * If user is already clocked in, don't change clocked in status, just tell the user the employee is already clocked
	 * in and call work with that employee as a parameter
	 */
	public void clockIn() {
		Employee toClockIn = choosePosition();
		if (toClockIn.getClockedIn() == true) {
			helper.print(toClockIn.getName() + " is already clocked in");
			work(toClockIn);
		} else {
			toClockIn.setClockedIn(true);
			helper.print(toClockIn.getName() + " has successfully been clocked in.");
			work(toClockIn);
		}
	}
	
	/**
	 * Calls choosePosition() to get the employee the user would like to clock out as
	 * Changes chosen employee to clocked out, notifies user, and returns to main menu
	 * If user is already clocked out, don't change clocked in status, just tell the user the employee is already clocked out
	 * and return to the main menu
	 */
	public void clockOut() {
		Employee toClockOut = choosePosition();
		if (toClockOut.getClockedIn() == false) {
			helper.print(toClockOut.getName() + " is already clocked out");
		} else {
			if (toClockOut instanceof HourlyWorker) {
				HourlyWorker hw = (HourlyWorker)toClockOut;
				hw.calculateShiftEarnings();
			}
			toClockOut.setClockedIn(false);
			helper.print(toClockOut.getName() + " has successfully been clocked out.");
		}
	}
	
	/**
	 * Asks user to enter four digit password, calls work with employee that matches entered password key
	 * User can enter -1 to quit
	 * If employee is not clocked in, user is notified and brought back to main menu
	 */
	public void workLogin() {
//		Employee e = choosePosition();
//		if (e.getClockedIn() == false) {
//			System.out.println(e.getName() + " is not clocked in. Please clock in before working.");
//		}
//		else {
//			work(e);
//		}
		boolean foundEmp = false;
		int userPsswd;
		do {
			userPsswd = helper.inputInt("Enter four digit number or -1 to exit");
			for (Integer i : employees.keySet()) {
					if (userPsswd == i && employees.get(i).getClockedIn() == true) {
						foundEmp = true;
						work(employees.get(i));
					}
					else if (userPsswd == i && employees.get(i).getClockedIn() == false) {
						helper.print("Please clock in " + employees.get(i).getName() + " before working");
					}
				}
		} while (foundEmp == false && userPsswd != -1);
	}
	
	
	/**
	 * @param Employee
	 * Takes in an employee as parameter, casts it to Server, Cook, or Manager, and calls that object's main menu 
	 * (in server/cook/manager classes)
	 */
	public void work(Employee e) {
		if (e instanceof Server) {
			Server s = (Server)e;
			s.printServerOptions(tables);
		}
		else if (e instanceof Cook) {
			Cook c = (Cook)e;
			c.printCookOptions();
		}
		else if (e instanceof Manager) {
			Manager m = (Manager)e;
			m.printManagerOptions();
		}
}
	
	/**
	 * @return Employee object
	 * Used before the clockin and clockout functions
	 * Asks user if they are server, cook, or manager, prints a list of chosen employee type, so user can choose
	 * an employee of that type (to clock in or clock out)
	 */
	public Employee choosePosition() {
		helper.print("\t1. Server");
		helper.print("\t2. Cook");
		helper.print("\t3. Manager");
		int userNum = helper.inputInt("Are you a server, cook, or manager? (1-3)", 1, 3);
		int i = 1;
		ArrayList<Employee> tempList = new ArrayList<>();
		switch (userNum) {
		case 1:
			tempList.clear();
			for (Employee e : employees.values()) {
				if (e instanceof Server) {
					helper.print("\t" + i + ". " + e.getName());
					tempList.add(e);
					i++;
				}
			}
			int userIntS = (helper.inputInt("Choose a server", 1, tempList.size()));
			return tempList.get(userIntS - 1);		
		case 2:
			tempList.clear();
			for (Employee e : employees.values()) {
				if (e instanceof Cook) {
					helper.print("\t" + i + ". " + e.getName());
					tempList.add(e);
					i++;
				}
			}
			int userIntC = (helper.inputInt("Choose a cook", 1, tempList.size()));
			return tempList.get(userIntC - 1);
			
			
		default:
			tempList.clear();
			for (Employee e : employees.values()) {
				if (e instanceof Manager) {
					helper.print("\t" + i + ". " + e.getName());
					tempList.add(e);
					i++;
				}
			}
			int userIntM = (helper.inputInt("Choose a manager", 1, tempList.size()));
			return tempList.get(userIntM - 1);
		}
				
	}
	
	
	/**
	 * Displays options for user to view menu (full, menu type, or price ordered)
	 */
	public void viewMenu() {
		helper.print("\t1. View full Menu");
		helper.print("\t2. View menu items of desired meal type");
		helper.print("\t3. View menu items ordered by price");
		int menuChoice = helper.inputInt("How would you like to view the menu? (1-3)", 1, 3);
		switch (menuChoice) {
		case 1:
			viewFullMenu();
			break;
		case 2:
			viewCategory();
			break;
		case 3:
			viewPriceOrdered();
			break;
		}
	}
	
	/**
	 * Displays full menu to user, organized by menutype
	 */
	public void viewFullMenu() {
		for (MenuType mt : MenuType.values()) {
			helper.print(mt.getDescription());
			for (MenuItem mi : menu) {
				if (mi.getType() == mt) {
					helper.print("\t" + mi);
				}
			}
		}
	}
	
	/**
	 * User chooses menu type (e.g., appetizer, salad, desserts, etc.) and all menu items of that type are displayed
	 */
	public void viewCategory() {
		int i = 1;
		for (MenuType mt : MenuType.values()) {
			helper.print(i + ". " + mt.getDescription());
			i++;
		}
		int menuChoice = helper.inputInt("Which category?", 1, MenuType.values().length);
		MenuType chosenType = MenuType.values()[menuChoice - 1];
		helper.print("Here are the " + chosenType.getDescription() + ": ");
		for (MenuItem mi : menu) {
			if (mi.getType() == chosenType) {
				helper.print("   " + mi);
			}
		}
	}
	
	/**
	 * Displays all menu items, ordered by price from low to high
	 */
	public void viewPriceOrdered() {
		Collections.sort(menu);
//		for (MenuItem m : menu) {
//			helper.print(m);
//		}
		helper.printPretty(menu);
	}
	
	public static void main(String[] args) {
		Restaurant myRestaurant = new Restaurant();
		System.out.println("Welcome to my restaurant!");
		myRestaurant.run();
	}

}


//Previous code: 

//public Server chooseServer() {
//	
//	Server chosenServer = null;
//	
//	if (employees.get("Servers").isEmpty()) {
//		boolean addServer = helper.inputYesNoAsBoolean("No Servers currently exist in system. Would you like to add one?");
//		if (addServer == true) {
//			//TODO: addNewServer(); and assign new server to chosenServer
//		}
//	}
//	else {
//		int i = 1;
//		for (Employee s : employees.get("Servers")) {
//			System.out.println("\t" + i + ". " + s.getName());
//			i++;
//		}
//		System.out.println("\t" + i + ". Add new server");
//		int userServer = (helper.inputInt("Choose one", 1, i)) - 1;
//		if (userServer == i) {
//			//TODO: addNewServer();
//		}
//		else {
//			chosenServer = (Server)employees.get("Servers").get(userServer);
//		}
//	}
//		
//	return chosenServer;
//}
