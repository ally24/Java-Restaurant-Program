package model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * MenuItem objects representing each item on the restaurant's menu
 * Contains item's name, type, price, and ingredients; also contains availability which Cook can change (item can only be
 * ordered if available)
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class MenuItem implements Comparable<MenuItem> {
	
	private MenuType type;
	private double price;
	private String[] ingredients;
	private String name;
	private boolean availability;
	
	public MenuItem(MenuType type, double price, String[] ingredients, String name) {
		this.type = type;
		this.price = price;
		this.ingredients = ingredients;
		this.name = name;
		availability = true;
	}

	/**
	 * @return the type
	 */
	public MenuType getType() {
		return type;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

//	/**
//	 * @return the ingredients
//	 */
//	public String[] getIngredients() {
//		return ingredients;
//	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the availability
	 */
	public boolean getAvailability() {
		return availability;
	}

	/**
	 * @param availability boolean - the availability to set
	 */
	public void setAvailability(boolean availability) {
		this.availability = availability;
	}

	/**
	 * @return String of formatted MenuItem information (name, $price, and ingredients tabbed underneath)
	 */
	@Override
	public String toString() {
		String ingredientList = "";
		for (String s: ingredients) {
			ingredientList += s + ",";
		}
		return name + ", $" + price + "\n\t" + ingredientList;
	}

	/**
	 * @param MenuItem object to compare this object to
	 * First compare object's prices; if they are the same, compare names
	 */
	public int compareTo(MenuItem other) {
		// return 0 if equal, 1 if object is greater, -1 if other is greater
		int val;
		if (Math.abs(price - other.price) <= 0.001) {
			val = name.compareTo(other.name);
		}
		else if (price - other.price > 0) {
			val = 1;
		}
		else {
			val = -1;
		}
		return val;
	}
	
	/**
	 * @param delimiter
	 * @return String
	 * Takes MenuItem variables and converts them into a String that matches the menu file's format
	 * In use when writing to a file; the new MenuItem that the user wants to add to the file must be written in the same
	 * format as the existing menu items in the file (so it can be read again next time)
	 */
	public String toFileString(String delimiter) {
		// APPETIZER	Garlic Bread	Jack cheese, roasted garlic and herbs	$9.00 
		String ingredientsString = String.join(", ", ingredients);
		return type + delimiter + name + delimiter + ingredientsString + delimiter + "$" + price;
	}

	/**
	 * If 2 objects are the same spot in memory, they are equal and return true
	 * If the other object does not exist (null), return false (other and this are not equal)
	 * If other object is not of the same class as this object, return false (other and this are not equal)
	 * If 2 MenuItem objects have the same name, price (within difference of 0.001), and MenuType enum, return true (equal objects)
	 * @return boolean (true if objects are equal, false if they are not equal)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MenuItem other = (MenuItem) obj;
		if (name.equalsIgnoreCase(other.name) && Math.abs(price - other.price) < 0.001 && type.equals(other.type)) {
			return true;
		}
		return false;
	}
	
	


}
