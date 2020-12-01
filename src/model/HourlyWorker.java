package model;

/**
 * HourlyWorked interface (implemented by Cook and Server), dictates necessary methods for any employee that is on an hourly wage
 * basis (accessor for hourly wage amount, method to calculate shift earnings [hours worked x hourly wage], and method to print
 * statistics of worker's shift)
 * @author Ally Satterfield
 * ITP 265, Fall 2020, Gelato Section
 * Final Assignment
 * Email: allysons@usc.edu
 */
public interface HourlyWorker {
	
	public double getHourlyWage();
	
	public void calculateShiftEarnings();
	
	public void printStats();

}
