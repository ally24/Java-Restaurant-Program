package model;

import java.util.*;

/**
 * RobotServer allows Cook to complete orders even if no actual Server objects have sent some in. Generates a list of orders
 * that Cook can then complete. 
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class RobotServer {

	private Map<Integer, Table> currRobotTables;
	//private ArrayList<MenuItem> menu;
	
	public RobotServer() {
		currRobotTables = new HashMap<>();
//		this.menu = menu;
	}
	
	/**
	 * @return robotTables Map<Integer, Table>
	 * Generates a random number of tables between 1 and 10, creates orders for each table, and returns the 
	 * table Map
	 */
	public Map<Integer, Table> makeOrders(ArrayList<MenuItem> menu) {
		Random gen = new Random();
		int numOrders = gen.nextInt(9) + 1; //get random number of orders between 1 and 10
		
		for (int i = 0; i < numOrders; i++) {
			currRobotTables.put((200 + i), new Table(gen.nextInt(7) + 1, menu));
		}
		
		for (Table t : currRobotTables.values()) {
			t.getOrder().createOrder();
		}
		
		return currRobotTables;
	}
	
	/**
	 * Clears the tables Map, so when Cook asks for robot tables again, it restarts 
	 */
	public void clearRobotTables() {
		currRobotTables.clear();
	}


//	public void getRobotOrders() {
//		Random gen = new Random();
//		Map<Integer, Table> testerMap = new HashMap<>();
//		int numOrders = (gen.nextInt(9)) + 1; //get random number of orders from 1-10
//	
//		for (int i = 0; i < numOrders; i++) {
//			testerMap.put((200 + i), new Table(gen.nextInt(8), menu));
//		}
//		
//		boolean cookOrder = true;
//		do {
//			System.out.println("Here are your orders:");
//			for (Integer i: testerMap.keySet()) {
//				if (testerMap.get(i).getOrder().getCompletedByCooks() == false) {
//					System.out.println("Table No. " + i + "'s " + testerMap.get(i).getOrder());
//				}
//			}
//			
//			cookOrder = helper.inputYesNoAsBoolean("Would you like to help a table?");
//			if (cookOrder == true) {
//				int whichTable;
//				do {
//					whichTable = helper.inputInt("Which table?");
//				} while (testerMap.containsKey(whichTable) == false);
//				
//				testerMap.get(whichTable).getOrder().setCompletedByCooks(true);	
//			}
//			
//		} while (cookOrder == true);	
//
//	}
	
}
