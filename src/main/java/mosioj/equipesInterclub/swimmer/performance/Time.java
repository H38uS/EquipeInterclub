package mosioj.equipesInterclub.swimmer.performance;

/**
 * The time of a race made by a swimmer.
 * 
 * @author Jordan Mosio
 *
 */
public class Time {

	private final int minutes;
	private final int secondes;
	private final int millisecondes;

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
}
