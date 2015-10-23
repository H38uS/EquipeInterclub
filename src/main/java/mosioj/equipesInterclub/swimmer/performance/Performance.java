package mosioj.equipesInterclub.swimmer.performance;

import mosioj.equipesInterclub.swimmer.Race;

public class Performance {
	
	public final Race race;
	public final Time time;
	public final String date;
	public final boolean isAWoman;
	

	/**
	 * Initialize a new Performance.
	 * 
	 * @param pRace
	 * @param pTime
	 * @param pDate
	 */
	public Performance(String pRace, String pTime, String pDate) {
		this(Race.getRace(pRace), new Time(pTime), pDate, !pRace.endsWith("Messieurs"));
	}

	/**
	 * Initialize a new Performance.
	 * 
	 * @param pRace
	 * @param pTime
	 * @param pDate
	 */
	public Performance(Race pRace, Time pTime, String pDate, boolean pIsWoman) {
		race = pRace;
		time = pTime;
		date = pDate;
		isAWoman = pIsWoman;
	}

	/**
	 * Apply a malus on this performance.
	 * 
	 * @param huge
	 */
	public void bonusIt(boolean huge) {
		if (race != null)
			time.bonusIt(race, huge);
	}

	@Override
	public String toString() {
		return race + ": " + time + " (" + date + ")";
	}
	
}