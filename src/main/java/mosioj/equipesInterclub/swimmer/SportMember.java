package mosioj.equipesInterclub.swimmer;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class SportMember {

	/**
	 * Member year of birth.
	 */
	public int birthYear;

	/**
	 * Reads information provided.
	 * 
	 * @param ind
	 */
	public void setInformation(String ind) {
		int firstIndex = ind.indexOf("(");
		int lastIndex = ind.indexOf(")");
		birthYear = Integer.valueOf(ind.substring(firstIndex + 1, lastIndex));
	}

	/**
	 * 
	 * @return The FFN compliant version of the toString.
	 */
	public abstract String getFFNToString();

	/**
	 * 
	 * @param nextYear Consider we are looking at today's, or next year.
	 * @return The category of the swimmer.
	 */
	public Category getMemberCategory(boolean nextYear) {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY");
		int current = Integer.parseInt(sdf.format(new Date()));
		int age = current - birthYear;
		return Category.readCategory(nextYear ? age++ : age);
	}
}
