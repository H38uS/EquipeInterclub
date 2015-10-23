package mosioj.equipesInterclub.swimmer.performance;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.swimmer.Race;

/**
 * The time of a race made by a swimmer.
 * 
 * @author Jordan Mosio
 *
 */
public class Time implements Comparable<Time> {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(Time.class);

	private int minutes;
	private int secondes;
	private int millisecondes;

	/**
	 * Class constructor.
	 * 
	 * @param time The time at the format [min:]seconds.milli
	 */
	public Time(String time) {

		if (time.contains(":")) {
			minutes = Integer.parseInt(time.substring(0, time.indexOf(":")));
			time = time.substring(time.indexOf(":") + 1);
		} else {
			minutes = 0;
		}

		secondes = Integer.parseInt(time.substring(0, time.indexOf(".")));
		time = time.substring(time.indexOf(".") + 1);

		millisecondes = Integer.parseInt(time);
	}

	/**
	 * Class constructor.
	 * 
	 * @param min
	 * @param sec
	 * @param ms
	 */
	public Time(int min, int sec, int ms) {
		minutes = min;
		secondes = sec;
		millisecondes = ms;
	}

	/**
	 * 
	 * @return The long representation of this instance.
	 */
	public long getAsLong() {
		return millisecondes + (secondes * 100) + (minutes * 100 * 60);
	}

	@Override
	public String toString() {

		StringBuilder res = new StringBuilder();

		if (minutes != 0) {
			res.append(minutes);
			res.append(":");
		}

		if (secondes < 10)
			res.append("0");
		res.append(secondes);
		res.append(".");

		if (millisecondes < 10)
			res.append("0");
		res.append(millisecondes);

		return res.toString();
	}

	/**
	 * 
	 * @param ms The number of milliseconds of this time.
	 * @return A Time object.
	 */
	public static Time getFromLong(long ms) {
		return new Time((int) (ms / 100 / 60), (int) (ms / 100 % 60), (int) (ms % 100));
	}

	/**
	 * Adds the given amount of millisecond to the current time.
	 * 
	 * @param baseMS
	 */
	public void addMs(int baseMS) {
		LOGGER.trace("Was: " + toString());
		Time copy = getFromLong(getAsLong() + baseMS);
		millisecondes = copy.millisecondes;
		secondes = copy.secondes;
		minutes = copy.minutes;
		LOGGER.trace("And now is: " + toString());
	}

	/**
	 * Malus the current time based on the race. The longer, the bigger the malus is !
	 * 
	 * @param race
	 * @param huge
	 */
	public void malusIt(Race race, boolean huge) {

		int baseMS = race.getMalus();
		if (huge)
			baseMS *= 2;

		LOGGER.trace("Applying a malus of " + baseMS + " milliseconds.");
		addMs(baseMS);
	}

	/**
	 * Bonus the current time based on the race. The longer, the bigger the bonus is !
	 * 
	 * @param race
	 * @param huge
	 */
	public void bonusIt(Race race, boolean huge) {

		int baseMS = race.getMalus();
		if (huge)
			baseMS *= 2;

		LOGGER.trace("Applying a bonus of " + baseMS + " milliseconds.");
		addMs(-baseMS);
	}

	/**
	 * 
	 */
	public int compareTo(Time o) {

		if (minutes < o.minutes)
			return -1;

		if (minutes > o.minutes)
			return 1;

		// Minutes égales

		if (secondes < o.secondes)
			return -1;

		if (secondes > o.secondes)
			return 1;

		// Secondes égales

		if (millisecondes < o.millisecondes)
			return -1;

		if (millisecondes > o.millisecondes)
			return 1;

		return 0;
	}
}
