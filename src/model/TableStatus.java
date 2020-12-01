package model;

/**
 * Contains various statuses a table can be in. Table objects start at empty and move through the TableStatus enum values 
 * depending on the tasks servers and cooks have done for the Table.
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public enum TableStatus {
	EMPTY("empty"),
	READY_TO_ORDER("ready to order"),
	WAITING_FOR_ORDER("waiting for order"),
	READY_TO_PAY("ready to pay");
	private String description;
	
	private TableStatus(String description) {
		this.description = description;
	}
	
	/**
	 * @return String - description associated with enum value
	 */
	public String getDescription() {
		return description;
	}
}
