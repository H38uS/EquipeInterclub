package mosioj.equipesInterclub.swimmer.performance;

import org.apache.log4j.Logger;

/**
 * The time of a race made by a swimmer.
 * 
 * @author Jordan Mosio
 *
 */
public class Time {

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
	 * Malus the current time based on the race.
	 * The longer, the bigger the malus is !
	 * 
	 * @param race
	 * @param huge
	 */
	public void malusIt(String race, boolean huge) {
		
		// FIXME quand on se sera basé sur des races, inclure ce calcul dans les races
		int baseMS = 50;
		
		if (race.startsWith("100")) {
			baseMS = 150;
		}

		if (race.startsWith("200")) {
			baseMS = 250;
		}
		
		if (race.startsWith("400")) {
			baseMS = 500;
		}
		
		if (race.startsWith("1500")) {
			baseMS = 2500;
		}
		
		if (huge)
			baseMS *= 2;
		
		LOGGER.debug("Applying a malus of " + baseMS + " milliseconds.");
		addMs(baseMS);
	}
}
