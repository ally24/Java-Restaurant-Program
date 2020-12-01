package model;
import java.util.*;

/**
 * Table object that a server can pick up, take the order of (which can be completed by cooks), and complete
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class Table {
	
	//private int tableNumber;
	private int numCustomers;
	private TableStatus status;
	private Order order;
	private boolean pickedUpByServer;
	private String serverName;
	
	public Table(int numCustomers, TableStatus status, Order order) {
		this.numCustomers = numCustomers;
		this.status = status;
		this.order = order;
		pickedUpByServer = false;
		serverName = "";
	}
	
	public Table(int numCustomers, TableStatus status, ArrayList<MenuItem> menu) {
		this(numCustomers, status, new Order(numCustomers, menu));
	}
	
	public Table(int numCustomers, ArrayList<MenuItem> menu) {
		this(numCustomers, TableStatus.EMPTY, new Order(numCustomers, menu));
	}
//
//	/**
//	 * @return the tableNumber
//	 */
//	public int getTableNumber() {
//		return tableNumber;
//	}

	/**
	 * @return Order - the table's Order object
	 */
	public Order getOrder() {
		return order;
	}
	
	/**
	 * @return TableStatus - the table's TableStatus enum
	 */
	public TableStatus getStatus() {
		return status;
	}
	
	/**
	 * @return String - the String description of the table's TableStatus enum
	 */
	public String getStringStatus() {
		return getStatus().getDescription();
	}
	
	/**
	 * @param TableStatus
	 * Sets table's TableStatus enum to passed in enum status
	 */
	public void setStatus(TableStatus status) {
		this.status = status;
	}

	/**
	 * @return boolean pickedUpByServer
	 * returns true if table is associated with a Server, false if not
	 */
	public boolean isPickedUpByServer() {
		return pickedUpByServer;
	}

	/**
	 * @param boolean pickedUpByServer - the pickedUpByServer to set
	 */
	public void setPickedUpByServer(boolean pickedUpByServer) {
		this.pickedUpByServer = pickedUpByServer;
		if (pickedUpByServer == false) {
			setServerName("");
		}
	}
	
	/**
	 * @param String - name of Server associated with table, empty if no server
	 */
	public void setServerName(String name) {
		this.serverName = name;
	}
	
	/**
	 * @return String - name of Server associated with table, empty if no server
	 */
	public String getServerName() {
		return serverName;
	}
	
	/**
	 * @return int - number of customers at table (equivalent to number of MenuItems associated with table)
	 */
	public int getNumCustomers() {
		return numCustomers;
	}
	
	
	

}
