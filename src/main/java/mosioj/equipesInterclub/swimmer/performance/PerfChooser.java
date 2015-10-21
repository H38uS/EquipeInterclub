package mosioj.equipesInterclub.swimmer.performance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

public class PerfChooser {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(PerfChooser.class);

	/**
	 * The initial list of list. Each list is for one specific race.
	 */
	private final List<List<Performance>> performances;

	/**
	 * Class constructor.
	 * 
	 * @param perfs
	 */
	public PerfChooser(List<Performance> perfs) {

		performances = new ArrayList<List<Performance>>();
		String race = null;

		// Initialization
		List<Performance> current = new ArrayList<Performance>();
		race = perfs.get(0).race;

		for (Performance perf : perfs) {

			if (race != perf.race) {

				// New race detected !
				race = perf.race;

				// Adding last ones
				performances.add(current);

				// Creating new empty one
				current = new ArrayList<Performance>();
			}

			current.add(perf);
		}
	}

	/**
	 * 
	 * @return The performance that will count for each race.
	 */
	public List<Performance> chooseIt() {

		List<Performance> result = new ArrayList<Performance>();
		for (List<Performance> perfs : performances) {
			Performance theOne = getRelevantPerf(perfs);
			result.add(theOne);
		}

		return result;
	}

	/**
	 * Eg, 23/10/2015 returns 2016, and 23/08/2015 2015.
	 * 
	 * @param date The date in format DD/MM/YYYY.
	 * @return The second year of the season, starting in September.
	 */
	private int getSeasonYear(String date) {
		String[] res = date.split("/");
		int year = Integer.parseInt(res[2]);
		return Integer.parseInt(res[1]) > 8 ? year + 1 : year;
	}

	/**
	 * 
	 * @param perfs
	 * @return The average time of those performances.
	 */
	private Time average(List<Performance> perfs) {

		long total = 0;
		for (Performance perf : perfs) {
			total += perf.time.getAsLong();
		}

		total /= perfs.size();
		return Time.getFromLong(total);
	}

	private Performance getRelevantPerf(List<Performance> perfs) {

		Performance first = perfs.get(0);
		String race = first.race;
		LOGGER.info("Processing race " + race + "...");
		SimpleDateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");
		final int thisSeason = getSeasonYear(sdf.format(new Date()));

		// Getting the max
		int max = 0;
		for (Performance perf : perfs) {
			if (getSeasonYear(perf.date) > max) {
				max = getSeasonYear(perf.date);
			}
		}

		// First try : keeping only two most recent season
		List<Performance> filteredPerfs = new ArrayList<Performance>();
		for (Performance perf : perfs) {
			if (getSeasonYear(perf.date) > max - 2)
				filteredPerfs.add(perf);
		}

		// Making the mean of it
		LOGGER.debug("Taking the mean of " + filteredPerfs.size() + " values...");
		Time time = average(filteredPerfs);

		// Malus
		if (max == thisSeason - 2) {
			LOGGER.warn("Malus for race " + race + ". No performance this season and the one before !");
		}

		if (max < thisSeason - 2) {
			LOGGER.warn("Huge malus for race " + race + ". No performance for the past two seasons :o");
		}
		
		if (filteredPerfs.size() < 3) {
			LOGGER.warn("Not enough values... Applying a malus.");
		}

		return new Performance(race, time, null);
	}
}
