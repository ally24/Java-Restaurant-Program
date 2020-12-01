package model;

import view.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.Map.*;
/**
 * Code description
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Assignment xx, Week xx
 * Email: allysons@usc.edu
 */
public class Manager extends Employee {
	
	private double salary;
	private UIConsole helper;
	private Map<Integer, Employee> employees;
	private Map<Integer, Table> restaurantTables;
	private ArrayList<MenuItem> menu;

	public Manager(String name, String phoneNumber, String email, boolean clockedIn, double salary, 
			 Map<Integer, Employee> employees, Map<Integer, Table> restaurantTables, ArrayList<MenuItem> menu) {
		super(name, phoneNumber, email, clockedIn);
		this.salary = salary;
		helper = new UIConsole();
		this.employees = employees;
		this.restaurantTables = restaurantTables;
		this.menu = menu;
	}
	
	public Manager(String name, String phoneNumber, String email, boolean clockedIn,
			Map<Integer, Employee> employees, Map<Integer, Table> restaurantTables, ArrayList<MenuItem> menu) {
		this(name, phoneNumber, email, clockedIn, 50000.00, employees, restaurantTables, menu);
	}
	
	public void printManagerOptions() {
		int managerChoice;
		do {
		helper.print("\nWhat would you like to do, " + name + "?");
		helper.print("\t1. View clocked in employee stats");
		helper.print("\t2. View restaurant table statuses");
		helper.print("\t3. Add or remove table");
		helper.print("\t4. Fire employee");
		helper.print("\t5. Edit menu");
		helper.print("\t6. Exit");
		managerChoice = helper.inputInt("Pick one (1-6)", 1, 6);
		switch (managerChoice) {
		case 1:
			viewEmployees();
			break;
		case 2:
			viewTableStatuses();
			break;
		case 3:
			addRemoveTable();
			break;
		case 4:
			fireEmployee();
			break;
		case 5:
			editMenu();
			break;
		}
		} while (managerChoice != 6);
	}
	
	public void viewEmployees() {
//		Before switching employee map keys to passwords------
//		for (String emp: employees.keySet()) {
//			helper.print("Clocked in " + emp + ":");
//			for (Employee e: employees.get(emp)) {
//				if (e.getClockedIn() == true) {
//					helper.print("\t" + e.getName());
//					if (e instanceof HourlyWorker) {
//						HourlyWorker hw = (HourlyWorker)e;
//						hw.getStats();
//					}
//				}
//			}
//		}
		
		helper.print("Clocked in Managers: ");
		for (Employee e : employees.values()) {
			if (e instanceof Manager && e.getClockedIn() == true) {
				helper.print("\t" + e.getName());
			}
		}
		helper.print("Clocked in Servers: ");
		for (Employee e : employees.values()) {
			if (e instanceof Server && e.getClockedIn() == true) {
				helper.print("\t" + e.getName());
				Server s = (Server)e;
				s.printStats();
			}
		}
		helper.print("Clocked in Cooks: ");
		for (Employee e : employees.values()) {
			if (e instanceof Cook && e.getClockedIn() == true) {
				helper.print("\t" + e.getName());
				Cook c = (Cook)e;
				c.printStats();
			}
		}
		
		//First try idea:
//		for(Entry<String, ArrayList<Employee>> entry: employees.entrySet()) {
//			helper.print("Clocked in " + entry.getKey() + ": ");
//			for (Employee e: entry.getValue()) {
//				if (e.getClockedIn() == true) {
//					
//				}
//			}
//		}
	}
	
	public void viewTableStatuses() {
		for (Entry<Integer, Table> entry: restaurantTables.entrySet()) {
			System.out.print("\tTable No. " + entry.getKey() + " is " + entry.getValue().getStringStatus() + ", seats " + entry.getValue().getNumCustomers());
			if (entry.getValue().getStatus() != TableStatus.EMPTY) {
				System.out.print(", Server: " + entry.getValue().getServerName());
			}
			helper.print("");
		}
	}
	
	public void addRemoveTable() {
		String addRemove = helper.inputWord("Would you like to add or remove a table? (Type \"add\" or \"remove\")", "add", "remove");
		if (addRemove.equalsIgnoreCase("add")) {
			int numCustomers = helper.inputInt("How many people can this table fit?");
			int greatest = 0;
			for (Integer i: restaurantTables.keySet()) {
				if (i > greatest) {
					greatest = i;
				}
			}
			restaurantTables.put(greatest + 1, new Table(numCustomers, menu)); 
			helper.print("Table with " + numCustomers + " seats has been added to the restaurant.");
		}
		else if (addRemove.equalsIgnoreCase("remove")) {
			viewTableStatuses();
			int toRemove;
			do {
				toRemove = helper.inputInt("\nWhich table would you like to remove?");
			} while (restaurantTables.containsKey(toRemove) == false);
			if (restaurantTables.get(toRemove).getStatus() != TableStatus.EMPTY) {
				helper.print("That table is not empty. Please try again after the table has been cleared.");
			}
			else {
				restaurantTables.remove(toRemove);
				helper.print("Table no. " + toRemove + " has been removed from the restaurant.");
			}
		}
		
	}
	
	public void fireEmployee() {
		// Display list of HourlyWorker employees (fireable by manager)
		// Allow manager to pick employee from list to fire
		// Fire if employee has no tables
		// If employee has tables, tell manager to either transfer table or server must complete
		
		
		ArrayList<Employee> fireable = new ArrayList<>();
		int i = 1;
		for (Employee e : employees.values()) {
			if (e instanceof HourlyWorker) {
				helper.print(i + ". " + e.getName() + ": " + e.getClass().getSimpleName());
				fireable.add(e);
				i++;
			}
		}
		int toFire = helper.inputInt("Which employee would you like to fire?", 1, i);
		Employee fire = fireable.get(toFire - 1);
		
		for (Entry<Integer, Employee> entry: employees.entrySet()) {
			if (entry.getValue().equals(fire)) {
				if (entry.getValue() instanceof Server) {
					fireServer(entry.getKey());
					break;
				}
				else {
					fireCook(entry.getKey());
					break;
				}
			}
		}
		
//		String fireName;
//		do {
//			fireName = helper.inputLine("Which employee would you like to fire? (Please enter their name)");
//		} while (employees.values().contains(o))
	}
	
	public void fireServer(Integer key) {
		boolean hasTables = false;
		Server s = (Server)employees.get(key);
		if (s.getNumTables() > 0) {
			hasTables = true;
		}
		
		if (hasTables == false) { //if server doesn't have tables, proceed with the fire
			helper.print(s.getName() + " has been fired and removed from the system.");
			employees.remove(key);
		}
		else { //if server does have tables, 
			helper.print(s.getName() + " has tables currently.");
			int tableHandle = helper.inputInt("Would you like to: \n\t1. Transfer tables to another server\n\t2. Complete tables now\n\t3. Cancel" , 1, 3);
			switch (tableHandle) {
			case 1:
				boolean otherServer = false;
				for (Employee e : employees.values()) {
					if (e instanceof Server && e.getClockedIn() == true && e.equals(s) == false){
						otherServer = true;
					}
				}
				if (otherServer == true) {
						boolean successful = transferTables(s);
						if (successful == true) {
							helper.print("Transfer successful.\n" + s.getName() + " has been fired and removed from the system.");
							employees.remove(key);
							break;
						} else {
							helper.print("Tables were not transfered. Completing tables manually instead...");
						}
				} else {
					helper.print("No other servers are clocked in. Completing tables manually instead...");
				}
			case 2:
				for (Table t : restaurantTables.values()) {
					if (t.getServerName() == s.name) {
						t.setPickedUpByServer(false);
						t.setStatus(TableStatus.EMPTY);
					}
				}
				employees.remove(key);
				helper.print(s.name + "'s tables have been completed. " + s.name + " has been fired and removed from the system.");
			}
		}
	}
	
	public void fireCook(Integer key) {
		helper.print(employees.get(key).getName() + " has been fired and removed from the system.");
		employees.remove(key);
	}
	
	public boolean transferTables(Server transferFrom) {
		helper.print("Here are " + transferFrom.getName() + "'s tables: ");
		ArrayList<Integer> transferFromTables = transferFrom.getTableNums();
				
		helper.print("Which server would you like to transfer the tables to?");
		int i = 1;
		ArrayList<Server> availableServers = new ArrayList<>();
		for (Employee e : employees.values()) {
			if (e instanceof Server && e.getClockedIn() == true && e.equals(transferFrom) == false){
				helper.print("\t" + i + ". " + e.getName());
				availableServers.add((Server)e);
				i++;
			}
		}
		int transferTo = helper.inputInt("", 1, i);
		Map<Integer, Table> transferTables = new HashMap<>();

			for (Integer tableNo : transferFromTables) {
				transferTables.put(tableNo, restaurantTables.get(tableNo));
			}
		
		return availableServers.get(transferTo - 1).addToCurrTables(transferTables);
	}
	
	public void editMenu() {
		int addOrRemove = helper.inputInt("Would you like to:\n\t1. Add an item\n\t2. Remove an item", 1, 2);
		switch (addOrRemove) {
		case 1:
			addMenuItem();
			break;
		case 2:
			removeMenuItem();
			break;
		}
	}
	
	public void removeMenuItem() {
		int i = 1;
		for (MenuItem mi : menu) {
			helper.print(i + ". " + mi.getName() + ", $" + mi.getPrice());
			i++;
		}
		int toRemove = helper.inputInt("Which item would you like to remove?", 1, i);
		helper.print(menu.get(toRemove - 1).getName() + " has been removed from the menu.");
		menu.remove(toRemove - 1);
	}
	
	public void addMenuItem() {
		int i = 1;
		helper.print("Would you like to add a: ");
		for (MenuType mt : MenuType.values()) {
			helper.print(i + ". " + mt.getDescription());
			i++;
		}
		int type = helper.inputInt("", 1, i);
		MenuType typeToAdd = MenuType.values()[type - 1];
		String name = helper.inputLine("What is the name of your " + typeToAdd.getDescription() + "?");
		double price = helper.inputDouble("What is the price of one " + name);
		String ingredientsList = helper.inputLine("Please list ingredients separated by commas.");
		String[] ingredients = ingredientsList.split(",");
		
		MenuItem newItem = new MenuItem(typeToAdd, price, ingredients, name);
		
		boolean alreadyExists = false;
		for (MenuItem mi : menu) {
			if (mi.equals(newItem)) {
				alreadyExists = true;
			}
		}
		
		if (alreadyExists == false) {
			boolean permanent = helper.inputYesNoAsBoolean("Would you like to permanently add " + name + " to the menu?");
			if (permanent == true) {
				MenuFileReader.writeNewItemToFile(newItem);
			}
			menu.add(newItem);
		} else {
			helper.print("An item of that type, name, and price already exists in the menu.");
		}
	}
	



}
