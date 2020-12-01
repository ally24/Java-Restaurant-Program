package model;

/**
 * Enum values associated with MenuItem objects (each MenuItem is of one menu type)
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public enum MenuType {
	APPETIZER("Appetizers"),
	SALAD("Salads"),
	PIZZA("Pizzas"),
	PASTA("Pastas"),
	ENTREE("Entrees"),
	DESSERT("Desserts");
	
	private String description;
	
	private MenuType(String description) {
		this.description = description;
	}
	
	/**
	 * @return description String
	 * Accessor method for enum value's associated description (so you can print a formatted word rather than the 
	 * capitalized enum)
	 */
	public String getDescription() {
		return description;
	}
}
