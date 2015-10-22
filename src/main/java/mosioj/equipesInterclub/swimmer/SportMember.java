package mosioj.equipesInterclub.swimmer;

public abstract class SportMember {
	
	/**
	 * Federal identifier.
	 */
	private String iuf;

	/**
	 * Member year of birth.
	 */
	protected int birthYear;

	/**
	 * Reads information provided.
	 * 
	 * @param piuf
	 * @param ind
	 */
	public void setInformation(String piuf, String ind) {

		iuf = piuf;
		
		int firstIndex = ind.indexOf("(");
		int lastIndex = ind.indexOf(")");
		
		birthYear = Integer.valueOf(ind.substring(firstIndex + 1, lastIndex));
	}
	
	/**
	 * 
	 * @return The FFN id of this swimmer.
	 */
	public String getId() {
		return iuf;
	}

	/**
	 * 
	 * @return The FFN compliant version of the toString.
	 */
	public abstract String getFFNToString();
}
