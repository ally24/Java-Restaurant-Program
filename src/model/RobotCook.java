package model;

import java.util.*;
/**
 * RobotCook completes a Server's table's orders that have been sent to be completed by the cooks. Allows the user to not
 * have to log in and out as various employees to complete tasks if they don't want to.
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class RobotCook{
	
	//private Map<Integer, Table> serversTables;

	public RobotCook() {
		//this.serversTables = serversTables;
	}
	
	/**
	 * @param Map<Integer, Table> containing server's current tables, tableNum Integer
	 * Takes in a table ID number, gets that table's order, and sets the order to true in completedByCooks
	 * Allows the server to 'deliver the order' instead of having to log out, log in as a Cook, and complete the order that way
	 */
	public void completeOrder(Map<Integer, Table> serversTables, Integer tableNum) {
		if(serversTables.get(tableNum).getOrder().getCompletedByCooks() == false) {
			serversTables.get(tableNum).getOrder().setCompletedByCooks(true);
		}
	}

}
