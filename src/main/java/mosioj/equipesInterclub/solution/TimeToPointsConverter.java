package mosioj.equipesInterclub.solution;

import java.io.File;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.ffn.FFNReader;
import mosioj.equipesInterclub.swimmer.Category;
import mosioj.equipesInterclub.swimmer.Race;
import mosioj.equipesInterclub.swimmer.Swimmer;
import mosioj.equipesInterclub.swimmer.performance.Time;
import mosioj.equipesInterclub.util.ExcelReader;

public class TimeToPointsConverter {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(FFNReader.class);

	/**
	 * The coefficient map.
	 */
	private final Map<CoefficientKey, Double> coeffMap = new HashMap<TimeToPointsConverter.CoefficientKey, Double>();

	/**
	 * Class constructor.
	 */
	public TimeToPointsConverter() {
		try {
			String url = getClass().getResource("/Equipe InterclubsNat 2015 et +.xlsx").getFile();
			File file = new File(url.replaceAll("%20", " "));

			LOGGER.info("Reading the coefficients...");
			List<List<String>> coeffs = ExcelReader.readLines(file, "Coefficients");
			readCoefficients(coeffs);

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Fail while reading Excel file...");
		}
	}

	/**
	 * Reads a main coefficient table data.
	 * 
	 * @param line The current line to parse.
	 */
	private void readCoefficientNonRelay(List<String> line) {

		if (line.size() != 19) {
			LOGGER.debug("Not a performance row (not the correct size)... Skipping it. Row: " + line);
			return;
		}

		// Parsing the race
		String fullRaceInfo = line.get(1);
		if (fullRaceInfo == null) {
			LOGGER.debug("Not a performance row (null race)... Skipping it. Row: " + line);
			return;
		}
		String raceToParse = fullRaceInfo.trim().substring(2).trim();
		raceToParse = raceToParse.replaceAll(" ", "");
		if (raceToParse.length() < 7) {
			LOGGER.debug("Not a performance row (too short code)... Skipping it. Row: " + line);
			return;
		}
		raceToParse = raceToParse.substring(0, 4);
		LOGGER.trace("Race to parse is: " + raceToParse);
		Race race = Race.getRace(raceToParse);
		if (race == null) {
			LOGGER.debug("Not a performance row (not a valid race)... Skipping it. Row: " + line);
			return;
		}
		LOGGER.trace("Race is: " + race);

		boolean isWoman = fullRaceInfo.endsWith("Dames");
		LOGGER.trace("Woman ? " + isWoman);

		// Adding new values to the map
		Category[] values = Category.values();
		for (int i = 0; i < values.length; i++) {
			CoefficientKey key = new CoefficientKey(race, values[i], isWoman, false);
			coeffMap.put(key, Double.parseDouble(line.get(i + 3)));
		}
	}

	/**
	 * Reads a main coefficient table data for Relay.
	 * 
	 * @param line The current line to parse.
	 */
	private void readCoefficientForRelay(List<String> line) {

		if (line.size() != 19) {
			LOGGER.debug("Not a performance row (not the correct size)... Skipping it. Row: " + line);
			return;
		}

		// Parsing the race
		String fullRaceInfo = line.get(0);
		String raceToParse = fullRaceInfo.replaceAll(" ", "");
		Race race = Race.getRace(raceToParse);
		if (race == null) {
			LOGGER.debug("Not a performance row (not a valid race)... Skipping it. Row: " + line);
			return;
		}
		
		// Adding new values to the map
		Category[] values = Category.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				continue;
			}
			// Nécessairement pour une femme
			// Et un relai
			CoefficientKey key = new CoefficientKey(race, values[i], true, true);
			coeffMap.put(key, Double.parseDouble(line.get(i + 1)));
		}
	}

	/**
	 * Parses the brute data into more convenient ones.
	 * 
	 * @param coeffs The coefficient tab data.
	 */
	private void readCoefficients(List<List<String>> coeffs) {

		boolean isInRelay = false;
		for (List<String> line : coeffs) {

			LOGGER.trace(line);
			if (line.size() > 0 && "Dames en Relais".equals(line.get(0))) {
				isInRelay = true;
				continue;
			}

			if (isInRelay) {
				readCoefficientForRelay(line);
			} else {
				readCoefficientNonRelay(line);
			}
		}

	}

	/**
	 * Compute the points according to given parameters.
	 * 
	 * @param race The race to get the point from.
	 * @param time The time made in this race.
	 * @param swimmer The swimmer that did the race.
	 * @param forNextYear Whether to evaluate for this year, or take next year category.
	 * @param isRelay True if time is performed in a mixed relay (only for women).
	 * @return The number of points of points for this time on this race, for this category/sex and relay mode or not.
	 */
	public int getPoints(Race race, Time time, Swimmer swimmer, boolean forNextYear, boolean isRelay) {
		return getPoints(race, time, swimmer.getMemberCategory(forNextYear), swimmer.isAWoman(), isRelay);
	}

	/**
	 * Compute the points according to given parameters.
	 * 
	 * @param race The race to get the point from.
	 * @param time The time made in this race.
	 * @param category The category of the swimmer.
	 * @param isWoman Whether the swimmer is a woman (or man assumed...).
	 * @param isRelay True if time is performed in a mixed relay (only for women).
	 * @return The number of points of points for this time on this race, for this category/sex and relay mode or not.
	 */
	public int getPoints(Race race, Time time, Category category, boolean isWoman, boolean isRelay) {
		return 900;
	}

	/**
	 * 
	 * @param race The race to get the point from.
	 * @param category The category of the swimmer.
	 * @param isWoman Whether the swimmer is a woman (or man assumed...).
	 * @param isRelay True if time is performed in a mixed relay (only for women).
	 * @return The coefficient to apply before searching for the time in the table.
	 */
	public double getCoefficient(Race race, Category category, boolean isWoman, boolean isRelay) {

		Double res = coeffMap.get(new CoefficientKey(race, category, isWoman, isRelay));
		if (res == null) {
			// On essaie dans le nom relai
			res = coeffMap.get(new CoefficientKey(race, category, isWoman, false));
		}

		if (res == null) {
			throw new IllegalStateException(MessageFormat.format(	"No coefficient found ! Values: {0} / {1} / {2} / {3}",
																	race,
																	category,
																	isWoman,
																	isRelay));
		}

		return res;
	}

	private class CoefficientKey {

		private final Race race;
		private final Category category;
		private final boolean isWoman;
		private final boolean isRelay;

		public CoefficientKey(Race pRace, Category pCat, boolean pIsWoman, boolean pIsRelay) {
			race = pRace;
			category = pCat;
			isWoman = pIsWoman;
			isRelay = pIsRelay;
		}

		@Override
		public boolean equals(Object other) {

			if (other == null || !(other instanceof CoefficientKey)) {
				return false;
			}

			CoefficientKey oth = (CoefficientKey) other;
			if (isWoman && !oth.isWoman) {
				return false;
			}
			if (!isWoman && oth.isWoman) {
				return false;
			}

			if (isRelay && !oth.isRelay) {
				return false;
			}
			if (!isRelay && oth.isRelay) {
				return false;
			}

			return race.equals(oth.race) && category.equals(oth.category);
		}

		@Override
		public int hashCode() {
			int base = race.hashCode() + category.hashCode();
			if (isRelay)
				base += 1;
			if (isWoman)
				base += 2;
			return base;
		}

		@Override
		public String toString() {
			return MessageFormat.format("[Race:{0}|Category:{1}|isWoman:{2}|isRelay:{3}]",
										race,
										category,
										isWoman,
										isRelay);
		}
	}
}
