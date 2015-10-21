package mosioj.equipesInterclub.swimmer.performance;

public class Performance {
	
	public final String race;
	public final Time time;
	public final String date;
	

	/**
	 * Initialize a new Performance.
	 * 
	 * @param pRace
	 * @param pTime
	 * @param pDate
	 */
	public Performance(String pRace, String pTime, String pDate) {
		this(pRace, new Time(pTime), pDate);
	}

	/**
	 * Initialize a new Performance.
	 * 
	 * @param pRace
	 * @param pTime
	 * @param pDate
	 */
	public Performance(String pRace, Time pTime, String pDate) {
		race = pRace;
		time = pTime;
		date = pDate;
	}

	/**
	 * 
	 * @return True if and only if this is a woman performances.
	 */
	public boolean isAWoman() {
		return true; // FIXME fill this
	}
	
}