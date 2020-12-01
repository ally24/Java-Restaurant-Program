package model;

import java.util.*;
import java.io.*;

/**
 * Reads a file containing menu items and converts each item into a MenuItem object which is added to a list that acts as the 
 * menu in the restaurant. Also contains a method where user can add an item to the menu file (write to file)
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public class MenuFileReader {	
	
	private static ArrayList<MenuItem> fullMenu;
	
	/**
	 * @param file
	 * @return ArrayList<MenuItem> (restaurant's menu - list of menu items)
	 * Tries to read a file with Scanner and sends each line to be converted to a MenuItem. Once converted, it is added
	 * to the list that is returned.
	 */
	public static ArrayList<MenuItem> readMenuData(String file){
		fullMenu = new ArrayList<>();
		
		try(Scanner fileSc = new Scanner(new FileInputStream(file))){
			fileSc.nextLine(); //skip header line
			while(fileSc.hasNextLine()) {
				String line = fileSc.nextLine();
				MenuItem mi = parseMenuItemData(line);
				if (mi != null) {
					fullMenu.add(mi);
				}
			}
			fileSc.close();
		} catch (FileNotFoundException e) {
			System.err.println("File \"" + file + "\" not found.");
		}
		return fullMenu;
	}
	
	/**
	 * @param line (String containing menu item information, separated by tabs)
	 * @return MenuItem
	 * Splits line by tabs and goes through each piece of information to create a new MenuItem from it
	 */
	public static MenuItem parseMenuItemData(String line) {
		MenuItem mi = null;
		try {
			String[] data = line.split("\t");
			MenuType type = getType(data[0]);
			String name = data[1];
			String[] ingredients = data[2].split(",");
			double price = Double.parseDouble(data[3].substring(1));
			mi = new MenuItem(type, price, ingredients, name);
		}
		catch (Exception e) {
			System.err.println("Couldn't parse this menu item: " + line);
		}
		return mi;
	}
	
	/**
	 * @param s String containing menu type declaration
	 * @return MenuType enum value
	 * Reads passed in String and matches it to the correct MenuType enum, which is returned
	 */
	public static MenuType getType(String s) {
		switch(s) {
		case("APPETIZER"): return MenuType.APPETIZER; 
		case("SALAD"): return MenuType.SALAD; 
		case("PIZZA"): return MenuType.PIZZA; 
		case("PASTA"): return MenuType.PASTA; 
		case("ENTREE"): return MenuType.ENTREE; 
		default: return MenuType.DESSERT; 
		
		}
		
	}
	
//	public static void main(String[] args) {
//		MenuFileReader tester = new MenuFileReader();
//		ArrayList<MenuItem> testerList = tester.readMenuData("bin/menu.txt");
//		for (MenuItem item: testerList) {
//			System.out.println(item);
//			//System.out.println(item.getName() + ", " + item.getPrice() + ", " + item.getType() + ", " + item.getIngredients());
//		}
//	}
	
	/**
	 * @param newItem (new MenuItem to be added to the menu file)
	 * Takes in a new MenuItem object and adds it to the menu file, in the correct format (menu information separated by tabs)
	 */
	public static void writeNewItemToFile(MenuItem newItem) {
		
		try (FileOutputStream fos = new FileOutputStream("bin/menu.txt");
				PrintWriter out = new PrintWriter(fos);){
			for(MenuItem m : fullMenu) {
				out.println(m.toFileString("\t"));
				System.out.println("Added Item");
			}
			out.println(newItem.toFileString("\t"));
			System.out.println("Added new item");
			out.close();
		} catch (FileNotFoundException e) {
			System.err.print("File was not found.");
			e.printStackTrace();
		} catch (IOException e) {
			System.err.print("Ran into IO Exception");
			e.printStackTrace();
		} //auto-close resources


	}
	

}
