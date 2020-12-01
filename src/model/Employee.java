package model;

/**
 * Abstract class that forms basis of any employee-extended objects (e.g., Server, Cook, Manager). All employees need a name,
 * phoneNumber, email, and clockedIn status in the system. Class contains accessor and mutator methods as well as an equals method.
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public abstract class Employee {
	
	protected String name;
	protected String phoneNumber;
	protected String email;
	protected boolean clockedIn;
//	protected int password;
	
	public Employee(String name, String phoneNumber, String email, boolean clockedIn) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.clockedIn = clockedIn;
//		this.password = password;
	}
	
	public Employee(String name, String phoneNumber, String email) {
		this(name, phoneNumber, email, false);
	}
	
	/**
	 * @return boolean (true if employee is clocked in, false if employee is clocked out)
	 * Accessor method for boolean clockedIn
	 */
	public boolean getClockedIn() {
		return clockedIn;
	}
	
	/**
	 * @param clockedIn boolean 
	 * Mutator method for boolean clockedIn
	 */
	public void setClockedIn(boolean clockedIn) {
		this.clockedIn = clockedIn;
	}
	
	/**
	 * @return String name
	 * Accessor method for String name variable
	 */
	public String getName() {
		return name;
	}
//
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (clockedIn ? 1231 : 1237);
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
//		return result;
//	}

	/**
	 * If 2 objects are the same spot in memory, they are equal and return true
	 * If the other object does not exist (null), return false (other and this are not equal)
	 * If other object is not of the same class as this object, return false (other and this are not equal)
	 * If 2 Employee objects have the same name, phoneNumber, and email, declare them as equal and return true
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
		Employee other = (Employee) obj;
		
		if (name.equals(other.name) && phoneNumber.equals(other.phoneNumber) && email.equals(other.email)) {
			return true;
		} 
		return false;
	}
	
//	public int getPassword() {
//		return password;
//	}
	
	

}
