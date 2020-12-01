/**
 * 
 * @author Kendra Walther
 * Email: kwalther@usc.edu 
 *
 */

package view;

public interface UI {
	public void print(String output);
	public void printPretty(String output);
	public String inputLine(String prompt);
	public String inputWord(String prompt);
	public String inputWord(String prompt, String option1, String option2);
	public int inputInt(String prompt) ;
	
	public int inputInt(String prompt, int minValue, int maxValue) ;
	public int inputInt(String prompt, int minValue, int maxValue, int quitValue);
	public int inputPostiveInt(String prompt);
	public double inputDouble(String prompt);
	
	public boolean inputBoolean(String prompt) ;
	public boolean inputYesNoAsBoolean(String prompt) ;
}
