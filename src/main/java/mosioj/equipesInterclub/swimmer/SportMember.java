package mosioj.equipesInterclub.swimmer;

public abstract class SportMember {
	
	/**
	 * Member year of birth.
	 */
	protected int birthYear;

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
}
