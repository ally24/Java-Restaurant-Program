package model;

import java.util.*;
import java.util.Map.*;

import view.*;

import view.UIConsole;

/**
 * Server employee class; creates Server object that holds info about tables & orders; Server's options are printed when
 * printServerOptions is called (done so from Restaurant class), where Server can choose to execute tasks or exit and user is
 * returned to main menu of Restaurant. 
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class Server extends Employee implements HourlyWorker {
	
	private int maxTables;
	//private ArrayList<Table> currTables;
	//private ArrayList<Table> restaurantTables;
	//private Map<Integer, Table> restaurantTables;
	private Map<Integer, Table> currTables;
	private double amountSold;
	private static final double HOURLY_WAGE = 13.25;
	private UIConsole helper;
	private RobotCook robotCook;
	

	public Server(String name, String phoneNumber, String email, boolean clockedIn, int maxTables,
			Map<Integer, Table> currTables, double amountSold) {
		super(name, phoneNumber, email, clockedIn);
		this.maxTables = maxTables;
		this.currTables = currTables;
		this.amountSold = amountSold;
		helper = new UIConsole();
		//this.restaurantTables = restaurantTables;
		robotCook = new RobotCook();
	}
	
	public Server(String name, String phoneNumber, String email, boolean clockedIn, int maxTables) {
		this(name, phoneNumber, email, clockedIn, maxTables, new HashMap<>(), 0.0);
	}

	public Server(String name, String phoneNumber, String email, boolean clockedIn) {
		this(name, phoneNumber, email, clockedIn, 3);
	}
	
	/**
	 * @return int (number of server's current tables)
	 * Accessor method for currTables map size
	 */
	public int getNumTables() {
		return currTables.size();
	}
	
	/**
	 * @return ArrayList<Integer> a list of table ID numbers that server currently has
	 * Iterates through server's current tables, prints each table number, and adds number to a list that is returned
	 */
	public ArrayList<Integer> getTableNums() {
		ArrayList<Integer> tableNums = new ArrayList<>();
		for (Integer i : currTables.keySet()) {
			helper.print("Table No. " + i);
			tableNums.add(i);
		}
		return tableNums;
	}
	
	/**
	 * @param newTables Map<Integer, Table>
	 * @return boolean
	 * Takes in a map of Integer keys and Table values and assigns them to server
	 * User has option to cancel if server has already hit their maxTables amount 
	 */
	public boolean addToCurrTables(Map<Integer, Table> newTables) {
		boolean proceed = true;
		if (currTables.size() >= maxTables) {
			proceed = helper.inputYesNoAsBoolean(name + " already has maximum tables. Do you still want to proceed?");
		}
		if (proceed == true) {
			for (Entry<Integer, Table> entry: newTables.entrySet()) {
				currTables.put(entry.getKey(), entry.getValue());
				entry.getValue().setServerName(name);
			}
			return true;
		}
		return false;
	}

	/**
	 * Accessor method for double hourly wage
	 * Necessary method for implementation of the HourlyWorker interface
	 */
	@Override
	public double getHourlyWage() {
		return HOURLY_WAGE;
	}


	/**
	 * Server user enters the amount of hours worked and shift earnings are calculated and displayed
	 * Necessary method for implementation of the HourlyWorker interface
	 */
	@Override
	public void calculateShiftEarnings() {
		UIConsole scnr = new UIConsole();
		int hoursWorked = scnr.inputInt("How many hours did you work?", 0, 1000);
		double shiftEarnings = hoursWorked * HOURLY_WAGE;
		helper.print("You earned $" + shiftEarnings);
	}
	
	/**
	 * Prints options for Server to do, Server user can choose one and the appropriate methods are then called
	 */
	public void printServerOptions(Map<Integer, Table> restaurantTables) {
		int serverChoice;
		do {
		helper.print("\nWhat would you like to do, " + name + "?");
		helper.print("\t1. Check table statuses");
		helper.print("\t2. Pick up new table");
		helper.print("\t3. Exit");
		serverChoice = helper.inputInt("Pick one", 1, 3);
		switch (serverChoice) {
		case 1:
			checkTableStatuses();
			break;
		case 2:
			pickUpTable(restaurantTables);
			break;
		}
		} while (serverChoice != 3);
		
	}

	/**
	 * Iterates through restaurantTables, if a table is empty, then add to server's tables, set the status to ready to order,
	 * and label the table with server name
	 * If no tables are empty or server has reached/surpassed their max tables, no tables is picked up
	 */
	private void pickUpTable(Map<Integer, Table> restaurantTables) {
		if (currTables.size() < maxTables) {
			boolean pickedUpTable = false;
			for (Entry<Integer, Table> entry: restaurantTables.entrySet()) {
				if (entry.getValue().getStatus() == TableStatus.EMPTY) { //get next table that is empty
					currTables.put(entry.getKey(), entry.getValue()); //add table to server's current tables
					entry.getValue().setPickedUpByServer(true); //set table to picked up by server
					entry.getValue().setServerName(name); //label table with server's name
					entry.getValue().setStatus(TableStatus.READY_TO_ORDER); //change table from empty to ready to order
					helper.print("Table No. " + entry.getKey() + " is now yours and ready to order");
					pickedUpTable = true;
					break; //stop iterating once a table has been added
				}
			}
			if (pickedUpTable == false) { 
				helper.print("Sorry, no tables are empty and ready for pick up.");
			}
		} else {
			helper.print("Sorry, you've reached your max tables. Please complete a table before picking up a new one.");
		}
	}
			

// Before changed tables from ArrayList to Map -------
//			for (Table t: restaurantTables.values()) {
//				if (t.getStatus() == TableStatus.EMPTY) {
//					currTables.add(t);
//					t.setStatus(TableStatus.READY_TO_ORDER);
//					helper.print("Table No. " + t.getTableNumber() + " is now yours and ready to order.");
//					pickedUpTable = true;
//					break;
//				}
//			}
//			if (pickedUpTable == false) {
//				helper.print("Sorry, no tables are empty and ready for pick up.");
//			}
//		} else {
//			helper.print("Sorry, you've reached your max tables. Please complete a table before picking up a new one.");
//		}
//	}

	/**
	 * Iterates through server's current tables to display each's status
	 * Server can exit or choose to 'help' a table depending on its status (i.e., move it to its next status like ready
	 * to order to waiting for order)
	 */
	private void checkTableStatuses() {
		if (currTables.isEmpty() == true) {
			helper.print("You have no tables right now.");
		} else {
			helper.print("\nHere are your current tables: ");
			
//			Before changed tables from ArrayList to Map -------
//			for (int i = 0; i < currTables.size(); i++) {
//				helper.print(". Table No. " + currTables.get(i).getTableNumber() + " is " + currTables.get(i).getStringStatus());
//			}
			
			for (Entry<Integer, Table> entry: currTables.entrySet()) {
				helper.print("\tTable No. " + entry.getKey() + " is " + entry.getValue().getStringStatus());
			}
			
			boolean helpTable = helper.inputYesNoAsBoolean("\nWould you like to help a table?");
			if (helpTable == true) {
				int whichTable;
				do {
					whichTable = helper.inputInt("Which table?");
				} while (currTables.containsKey(whichTable) == false);
								
				switch (currTables.get(whichTable).getStatus()) {
				case READY_TO_ORDER:
					takeOrder(whichTable);
					break;
				case WAITING_FOR_ORDER:
					deliverOrder(whichTable);
					break;
				case READY_TO_PAY:
					tablePay(whichTable);
					break;
				default:
					System.err.println("Error with table no. " + whichTable);
				}
			}
		}
		
	}

	/**
	 * @param Integer that is associated with a Table in the restaurantTables map
	 * Table paired to integer key (tableNum) passed in as a parameter is switched from Ready to Pay to Empty
	 * Table's order amount is added to server's amountSold 
	 */
	private void tablePay(Integer tableNum) {
		helper.print("Table no. " + tableNum + " is now paying.");
		double tableSale = currTables.get(tableNum).getOrder().getTotalOrderSale();
		helper.print("You sold $" + tableSale + " with this table");
		amountSold += tableSale;
		helper.print("Your total sales this shift are now : $" + amountSold);
		
		
		currTables.get(tableNum).setStatus(TableStatus.EMPTY);
		currTables.get(tableNum).setPickedUpByServer(false);
		//restaurantTables.get(tableNum).setServerName(""); //reset table's server name to empty -- now done in Table class
		currTables.remove(tableNum);	
	}

	/**
	 * @param Integer that is associated with a Table in the restaurantTables map
	 * Gets Table's completedByCooks boolean; if true, Table is switched to ready to pay and server is notified that their
	 * order was delivered, if false, Server is notified that the Cook has not completed the order and can remind them (robotCook
	 * then completes the order, so Server can deliver it next time around)
	 */
	private void deliverOrder(Integer tableNum) {
		if (currTables.get(tableNum).getOrder().getCompletedByCooks() == true) {
			currTables.get(tableNum).setStatus(TableStatus.READY_TO_PAY);
			helper.print("You have delivered table no. " + tableNum + "'s order. They are now ready to pay.");
		}
		else {
			boolean remindCooks = helper.inputYesNoAsBoolean("The kitchen has not completed this order. Would you like to remind the cooks to finish it?");
			if (remindCooks == true) {
				robotCook.completeOrder(currTables, tableNum);
				helper.print("You have reminded the cooks to finish the order. Check back soon to see if it is completed.");
			}
		}		
	}

	/**
	 * @param Integer that is associated with a Table in the restaurantTables map
	 * Table's order is created, printed, set to not completed by cooks yet, and Table's status is changed to waiting for order
	 */
	private void takeOrder(Integer tableNum) {
		currTables.get(tableNum).getOrder().createOrder();
		helper.print("\nTable No. " + tableNum + "'s " + currTables.get(tableNum).getOrder());
		helper.print("and has been sent to the kitchen");
		
		currTables.get(tableNum).getOrder().setCompletedByCooks(false);
	
		currTables.get(tableNum).setStatus(TableStatus.WAITING_FOR_ORDER);
	}
	
	/**
	 * Prints Servers shift statistics (number of current tables, total amount sold so far, max tables able to handle)
	 * Necessary method for implementation of the HourlyWorker interface
	 */
	public void printStats() {
		//helper.print(name);
		String s = "";
		if (currTables.size() > 0) {
			s += ": " + currTables.keySet();
		}
		helper.print("\t   >" + currTables.size() + " tables" + s);
		helper.print("\t   >Total amount sold: $" + amountSold);
		helper.print("\t   >Max tables able to handle: " + maxTables);
	}
	
	

}
