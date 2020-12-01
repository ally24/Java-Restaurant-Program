package model;

import java.util.*;

/**
 * Order object associated with each Table. Contains a list of MenuItem objects. Server can take the order (prints order to user),
 * and Cooks can complete this order (mark it as completedByCooks)
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */

public class Order {
	
	private int numPeopleInOrder;
	private ArrayList<MenuItem> itemsInOrder; 
	private ArrayList<MenuItem> fullMenu;
	private boolean completedByCooks;
	
	public Order(int numPeopleInOrder, ArrayList<MenuItem> fullMenu) {
		this.numPeopleInOrder = numPeopleInOrder;
		this.fullMenu = fullMenu;
		itemsInOrder = new ArrayList<>();
		//createOrder();
		completedByCooks = false;
	}
	
	public Order(int numPeopleInOrder, ArrayList<MenuItem> fullMenu, ArrayList<MenuItem> itemsInOrder) {
		this(numPeopleInOrder, fullMenu);
		this.itemsInOrder = itemsInOrder;
	}
	
	/**
	 * Creates an order for the table (a list of MenuItems called itemsInOrder). First clears the list of MenuItems (so if table
	 * was picked up more than once, the order is different each time and of the correct size). Fills itemsInOrder with MenuItems
	 * that are available. Number of MenuItems in the order is equivalent to the number of 'people at the table.'
	 */
	public void createOrder() {
		itemsInOrder.clear();
		Random gen = new Random();
		for (int i = 0; i < numPeopleInOrder; i++) {
			int r;
			do {
				r = gen.nextInt(fullMenu.size());
			} while (fullMenu.get(r).getAvailability() == false);
			itemsInOrder.add(fullMenu.get(r));
		}
	}
	
	/**
	 * @return double (Price of all items in the order)
	 * Calculates the order's full price by adding the price of each MenuItem in the order together 
	 */
	public double getTotalOrderSale() {
		double sale = 0.0;
		for (MenuItem mi : itemsInOrder) {
			sale += mi.getPrice();
		}
		return sale;
	}

//	/**
//	 * @return the itemsInOrder
//	 */
//	public ArrayList<MenuItem> getItemsInOrder() {
//		return itemsInOrder;
//	}

	/**
	 * @return boolean completedByCooks
	 */
	public boolean getCompletedByCooks() {
		return completedByCooks;
	}

	/**
	 * @return the numPeopleInOrder
	 */
	public int getNumPeopleInOrder() {
		return numPeopleInOrder;
	}

	/**
	 * @return the itemsInOrder
	 */
	public ArrayList<MenuItem> getItemsInOrder() {
		return itemsInOrder;
	}

	/**
	 * @param boolean completedByCooks - the completedByCooks to set
	 */
	public void setCompletedByCooks(boolean completedByCooks) {
		this.completedByCooks = completedByCooks;
	}

	/**
	 * @return String 
	 * Returns a formatted string of all the MenuItems in the order, called when object is printed
	 */
	@Override
	public String toString() {
		String s = "";
		for (MenuItem m: itemsInOrder) {
			s += "\n\t" + m.getName();
		}
		return "Order contains:" + s;
	}
	
	

}
