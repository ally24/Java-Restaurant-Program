package model;

import java.util.*;
import java.util.Map.*;


import view.*;

/**
 * Cook employee class; creates Cook object that holds info about tables & orders & menu items; Cook's options are printed when
 * printCookOptions is called (done so from Restaurant class), where Cook can choose to execute tasks or exit and user is returned
 * to main menu of Restaurant. 
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class Cook extends Employee implements HourlyWorker {
	
	private static final double HOURLY_WAGE = 15.25;
	//private ArrayList<Table> restaurantTables;
	private Map<Integer, Table> restaurantTables;
	private Map<Integer, ArrayList<String>> completedOrders;
	private UIConsole helper;
	private ArrayList<MenuItem> menu;
	private RobotServer robotServer;

	public Cook(String name, String phoneNumber, String email, boolean clockedIn,
			ArrayList<MenuItem> menu, Map<Integer, Table> restaurantTables) {
		super(name, phoneNumber, email, clockedIn);
		helper = new UIConsole();
		this.menu = menu;
		this.restaurantTables = restaurantTables;
		robotServer = new RobotServer();
		completedOrders = new HashMap<>();
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
	 * Cook user enters the amount of hours worked and shift earnings are calculated and displayed
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
	 * Prints options for Cook to do, Cook user can choose one and the appropriate methods are then called
	 */
	public void printCookOptions() {
		int cookChoice;
		do {
			helper.print("\nWhat would you like to do, " + name + "?");
			helper.print("\t1. Check for orders");
			helper.print("\t2. Ask servers for new orders");
			helper.print("\t3. View completed orders");
			helper.print("\t4. Change item availability");
			helper.print("\t5. Exit");
			cookChoice = helper.inputInt("Pick one", 1, 5);
			switch (cookChoice) {
			case 1:
				checkForOrders();
				break;
			case 2:
				getRobotOrders();
				break;
			case 3:
				viewCompletedOrders();
				break;
			case 4:
				changeAvailability();
				break;
			}
		} while (cookChoice != 5);
		
		
	}
	
	/**
	 * Displays all orders Cook has completed thus far
	 */
	private void viewCompletedOrders() {
		for (Entry<Integer, ArrayList<String>> entry: completedOrders.entrySet()) {
			helper.print("Table No. " + entry.getKey() + "'s completed orders: ");
			for (String o: entry.getValue()) {
				helper.print("> " + o);
			}
		}
		if (completedOrders.isEmpty()) {
			helper.print("No orders completed yet.");
		}
		
	}
	
	/**
	 * @param tableNo Integer
	 * @param table Table
	 * Adds key-value pair to completedOrders Map. If Integer key already exists, add Table's Order's toString String to existing value (ArrayList)
	 * If Integer key doesn't exist yet, add Integer key to map with new ArrayList<String> value containing Table's Order's toString
	 */
	private void addToCompletedOrders(Integer tableNo, Table table) {
		if (completedOrders.containsKey(tableNo)) {
			completedOrders.get(tableNo).add(table.getOrder().toString());
		}
		else {
			ArrayList<String> orderStrings = new ArrayList<>();
			orderStrings.add(table.getOrder().toString());
			completedOrders.put(tableNo, orderStrings);
		}
		
//		if (completedOrders.containsKey(entry.getKey())) {
//			ArrayList<Order> orders = completedOrders.get(entry.getKey());
//			orders.add(entry.getValue().getOrder());
//		}
//		else {
//			ArrayList<Order> orders = new ArrayList<>();
//			orders.add(entry.getValue().getOrder());
//			completedOrders.put(entry.getKey(), orders);
//		}
		
	}

	/**
	 * Iterate through restaurantTables map, if there are tables waiting for orders, print the orders and allow the cooks to 'complete'
	 * them (change Order's completedByCooks from false to true). If no tables, are in status Waiting for Order, tell cook.
	 */
	public void checkForOrders() {
		boolean ordersWaiting = false;
		
//		for (Table t: restaurantTables) {
//			if(t.getOrder().getCompletedByCooks() == false) {
//				helper.print("Table No. " + t.getTableNumber() + "'s " + t.getOrder());
//				ordersWaiting = true;
//			}
//		}
		
		for (Entry<Integer, Table> entry: restaurantTables.entrySet()) {
			if (entry.getValue().isPickedUpByServer() == true 	//Table must be picked up by server
					&& entry.getValue().getOrder().getCompletedByCooks() == false 	//Table must not have order completed yet
					&& entry.getValue().getStatus() == TableStatus.WAITING_FOR_ORDER) {	//Table must be in the Waiting for Order stage
				helper.print("Table No. " + entry.getKey() + "'s " + entry.getValue().getOrder());
				ordersWaiting = true;
			}
		}
		
		if (ordersWaiting == false) {
			helper.print("There are no orders waiting to be cooked.");
			//helper.print("Would you like to ask for orders?");
		}
		else {
			boolean cookComplete = helper.inputYesNoAsBoolean("Would you like to complete an order?");
			if (cookComplete == true) {
				completeOrder();
			}
		}
	}
	
//	Before switch from ArrayList to Map of restaurantTables
//	public boolean isValidTable(int order) {
//		for (Table t: restaurantTables) {
//			if (t.getTableNumber() == order && t.getOrder().getCompletedByCooks() == false) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	/**
	 * Asks user which order Cook should complete; if it's a valid order (waiting for order, exists in tables map, and order is not
	 * completed yet), then complete the order (change Order's completedByCooks from false to true)
	 */
	public void completeOrder() {
		int order;
		do {
			order = helper.inputInt("Which order?");
		} while (restaurantTables.containsKey(order) == false 
				|| restaurantTables.get(order).getOrder().getCompletedByCooks() == true
				|| restaurantTables.get(order).getStatus() != TableStatus.WAITING_FOR_ORDER);
		
		for (Entry<Integer, Table> entry: restaurantTables.entrySet()) {
			if (entry.getKey() == order) {
				entry.getValue().getOrder().setCompletedByCooks(true);
				addToCompletedOrders(entry.getKey(), entry.getValue());
				helper.print("You have completed table no. " + order + "'s order");
			}
		}
		
//		for (Table t: restaurantTables) {
//			if (t.getTableNumber() == order) {
//				t.getOrder().setCompletedByCooks(true);
//			}
//		}
	}
	
	/**
	 * Gets a list from robotServer of table orders (separate from restaurant's actual tables), cook can complete any number of those
	 * orders (which are added to the completedOrders map), or exit.
	 * When Cook exits, the robotServer's map of tables are cleared (so it is restarted every time the Cook asks for orders)
	 */
	public void getRobotOrders() {
		Map<Integer, Table> robotTables = robotServer.makeOrders(menu);
		
		boolean cookOrder = true;
		do {
			
			if (robotTables.isEmpty() == true) {
				helper.print("You have completed all of the orders.");
				cookOrder = false;
			} else {
			
				helper.print("Here are your orders:");
				for (Integer i: robotTables.keySet()) {
					if (robotTables.get(i).getOrder().getCompletedByCooks() == false) {
						helper.print("Table No. " + i + "'s " + robotTables.get(i).getOrder());
					}
				}
				
				cookOrder = helper.inputYesNoAsBoolean("Would you like to help a table?");
				if (cookOrder == true) {
					int whichTable;
					do {
						whichTable = helper.inputInt("Which table?");
					} while (robotTables.containsKey(whichTable) == false);
					
					robotTables.get(whichTable).getOrder().setCompletedByCooks(true);
					addToCompletedOrders(whichTable, robotTables.get(whichTable));
					robotTables.remove(whichTable);	
				}
			}
			
		} while (cookOrder == true);
		
		robotServer.clearRobotTables();
	}
	
	/**
	 * All items of menu are displayed (organized by available and unavailable). Cook can choose an item to switch availability of (available 
	 * to unavailable or unavailable to available). 
	 * Tables cannot order anything that is unavailable (however, if the Server takes their order before the item is set to 
	 * unavailable, it is valid, and table can order that item)
	 */
	public void changeAvailability() {
		ArrayList<MenuItem> availableItems = new ArrayList<>();
		ArrayList<MenuItem> unavailableItems = new ArrayList<>();
		
		for (MenuItem mi : menu) {
			if (mi.getAvailability() == true) {
				availableItems.add(mi);
			} else {
				unavailableItems.add(mi);
			}
		}
		
		helper.print("Available menu items:");
		int i = 1;
		for (MenuItem available : availableItems) {
			helper.print("\t" + i + ". " + available.getName());
			i++;
		}
		helper.print("Unavailable menu items:");
		for (MenuItem unavailable : unavailableItems) {
			helper.print("\t" + i + ". " + unavailable.getName());
			i++;
		}
		
		int toChange = (helper.inputInt("\nWhich item would you like to change the availability of?", 1, i - 1)) - 1;
		// 5 availble (0, 1, 2, 3, 4 index but displayed 1, 2, 3, 4, 5)
		// 3 unavailable (0, 1, 2 index but displayed 6, 7, 8)
		//to change last available user enters 5, toChange is 4 
		if (toChange < availableItems.size()) {
			availableItems.get(toChange).setAvailability(false);
		} else {
			toChange = toChange - availableItems.size();
			unavailableItems.get(toChange).setAvailability(true);
		}
		
	}
	
	/**
	 * Prints Cook's shift statistics (number of completed orders and orders waiting to be completed)
	 * Necessary method for implementation of the HourlyWorker interface
	 */
	public void printStats() {
		//helper.print(name);
		helper.print("\t   >" + completedOrders.size() + " completed orders");
		int numOrdersWaiting = 0;
		for (Table t: restaurantTables.values()) {
			if (t.getStatus() == TableStatus.WAITING_FOR_ORDER && t.getOrder().getCompletedByCooks() == false) {
				numOrdersWaiting += 1;
			}
		}
		helper.print("\t   >" + numOrdersWaiting + " orders waiting to be cooked");
		
	}

	
	
}
