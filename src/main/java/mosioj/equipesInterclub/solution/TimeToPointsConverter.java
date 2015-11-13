package mosioj.equipesInterclub.solution;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.swimmer.Category;
import mosioj.equipesInterclub.swimmer.Race;
import mosioj.equipesInterclub.swimmer.Swimmer;
import mosioj.equipesInterclub.swimmer.performance.Time;
import mosioj.equipesInterclub.util.ExcelReader;

public class TimeToPointsConverter {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(TimeToPointsConverter.class);

	/**
	 * The coefficient map.
	 */
	private final Map<CoefficientKey, Double> coeffMap = new HashMap<TimeToPointsConverter.CoefficientKey, Double>();

	/**
	 * The association between the integer and the race / sex.
	 */
	private final Map<Integer, SwimCodeValue> swimCodes = new HashMap<Integer, TimeToPointsConverter.SwimCodeValue>();

	/**
	 * Association between the race/sex and the list of time.
	 */
	private final Map<SwimCodeValue, List<Time>> pointsRows = new HashMap<SwimCodeValue, List<Time>>();

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

			LOGGER.info("Reading the cotation table...");
			List<List<String>> points = ExcelReader.readLines(file, "Table");
			readPoints(points);

			LOGGER.info("Init completed !");

		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException("Fail while reading Excel file...");
		}
	}

	/**
	 * Reads the cotation table.
	 * 
	 * @param data
	 */
	private void readPoints(List<List<String>> data) {

		// First, transpose it for facility of use
		List<List<String>> points = ExcelReader.transpose(data);

		for (List<String> line : points) {

			LOGGER.trace(line);

			if (line.size() < 19) {
				LOGGER.debug("Not a valid row (not enough data)... Skipping it. Row: " + line);
				continue;
			}

			double id = Double.parseDouble(line.get(0));
			SwimCodeValue code = swimCodes.get((int) Math.round(id));
			LOGGER.trace("Code: " + code);

			if (code == null) {
				LOGGER.debug("Invalid race, unknown code. Row is: " + line);
				continue;
			}

			List<Time> times = new ArrayList<Time>();
			line.remove(0);
			for (String time : line) {
				// Read as is
				double timeAsDble = Double.parseDouble(time);

				// partie entière
				long minutes = (long) Math.floor(timeAsDble);

				// Secondes + centieme en centieme
				long millisecondes = (long) ((timeAsDble - minutes) * 100 * 100);

				long realTime = (minutes * 60 * 100) + millisecondes;
				times.add(Time.getFromLong(realTime));
			}
			LOGGER.debug("Times found for " + code + ": " + times);
			pointsRows.put(code, times);
		}
	}

	/**
	 * Reads a main coefficient table data.
	 * 
	 * @param line The current line to parse.
	 */
	private void readCoefficientNonRelay(List<String> line) {

		// Parsing the race
		String fullRaceInfo = line.get(1);
		if (fullRaceInfo == null) {
			LOGGER.debug("Not a performance row (null race)... Skipping it. Row: " + line);
			return;
		}

		String raceToParse = fullRaceInfo.trim();

		// Get the race integer
		final String id = raceToParse.substring(0, 2);
		LOGGER.trace("Race id: " + id);

		raceToParse = raceToParse.substring(2).trim();
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
		int cat = 0;
		for (int i = 0; i < values.length; i++) {
			String value = line.get(i + 3);
			if (value == null)
				continue;
			double coeff = Double.parseDouble(value);
			if (coeff < 1.0 || coeff > 7) {
				LOGGER.trace("Skipping invalid coeffiscient : " + value);
				continue;
			}
			CoefficientKey key = new CoefficientKey(race, values[cat++], isWoman, false);
			LOGGER.trace(key + " / " + coeff);
			coeffMap.put(key, coeff);
		}
		LOGGER.info("Got coeffiscient list for race " + race + " ! Woman ? " + isWoman);

		// Registering the race
		swimCodes.put((int) Math.round(Double.parseDouble(id)), new SwimCodeValue(race, isWoman));
	}

	/**
	 * Reads a main coefficient table data for Relay.
	 * 
	 * @param line The current line to parse.
	 */
	private void readCoefficientForRelay(List<String> line) {

		// Parsing the race
		String fullRaceInfo = line.get(0);
		if (fullRaceInfo == null) {
			LOGGER.debug("Race is null, skipping. Row: " + line);
			return;
		}
		
		String raceToParse = fullRaceInfo.replaceAll(" ", "");
		Race race = Race.getRace(raceToParse);
		if (race == null) {
			LOGGER.debug("Not a performance row (not a valid race)... Skipping it. Row: " + line);
			return;
		}

		// Adding new values to the map
		Category[] values = Category.values();
		int cat = 0;
		for (int i = 1; i < line.size(); i++) {
			if (line.get(i) == null) {
				continue;
			}
			
			// Nécessairement pour une femme
			// Et un relai
			CoefficientKey key = new CoefficientKey(race, values[cat++], true, true);
			double val = Double.parseDouble(line.get(i));
			coeffMap.put(key, val);
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
			if (line.size() > 0 && "Dames en relais".equals(line.get(0))) {
				LOGGER.info("Reading relay coeffiscients...");
				isInRelay = true;
				continue;
			}

			if (line.size() < 15) {
				LOGGER.debug("Not a performance row (not the correct size)... Skipping it. Row: " + line);
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
	 * @return The number of points of points for this time on this race, for this category/sex and relay mode or not.
	 */
	public int getPoints(Race race, Time time, Swimmer swimmer, boolean forNextYear) {
		return getPoints(race, time, swimmer.getMemberCategory(forNextYear), swimmer.isAWoman());
	}

	/**
	 * Compute the points according to given parameters.
	 * 
	 * @param race The race to get the point from.
	 * @param time The time made in this race.
	 * @param category The category of the swimmer.
	 * @param isWoman Whether the swimmer is a woman (or man assumed...).
	 * @return The number of points of points for this time on this race, for this category/sex and relay mode or not.
	 */
	public int getPoints(Race race, Time time, Category category, boolean isWoman) {

		// Get the coefficient
		double coeff = getCoefficient(race, category, isWoman, false);
		LOGGER.trace("Before was: " + time);
		Time adjusted = Time.getFromLong(Math.round(time.getAsLong() / coeff));
		LOGGER.trace("After applying coefficient: " + adjusted);

		SwimCodeValue code = new SwimCodeValue(race, isWoman);
		List<Time> times = pointsRows.get(code);
		LOGGER.trace(code);
		for (int i = 0; i < times.size(); i++) {
			if (adjusted.compareTo(times.get(i)) <= 0) {
				return 1501 - i;
			}
		}

		return -1;
	}

	/**
	 * 
	 * @param times
	 * @param swimmers
	 * @param forNextYear
	 * @return The points for the given relay.
	 */
	public int getPointsOf4x50Relay(Time[] times, Swimmer[] swimmers, boolean forNextYear) {

		boolean[] isWoman = new boolean[4];
		Category[] categories = new Category[4];
		for (int i = 0; i < swimmers.length; i++) {
			isWoman[i] = swimmers[i].isAWoman();
			categories[i] = swimmers[i].getMemberCategory(forNextYear);
		}

		return getPointsOf4x50Relay(times, categories, isWoman);
	}

	/**
	 * 
	 * @param times
	 * @param categories
	 * @param isWoman
	 * @return The points for the given relay.
	 */
	public int getPointsOf4x50Relay(Time[] times, Category[] categories, boolean[] isWoman) {

		// Gets the coefficient
		// Mean of each coeff
		double coeff = 0.0;
		int nbWomen = 0;
		Race[] relai = { Race._50DOS, Race._50BRASSE, Race._50PAP, Race._50NL };
		for (int i = 0; i < categories.length; i++) {
			if (isWoman[i])
				nbWomen++;
			coeff += getCoefficient(relai[i], categories[i], isWoman[i], true);
		}

		// No points if less that 2 women
		if (nbWomen < 2)
			return 0;

		double mean = coeff / categories.length;

		// Compute the relay time
		long total = 0;
		for (Time time : times) {
			total += time.getAsLong();
		}
		Time time = Time.getFromLong(total);
		Time adjusted = Time.getFromLong(Math.round(time.getAsLong() / mean));

		// Read the associated points
		List<Time> timesTable = pointsRows.get(new SwimCodeValue(Race._4X504N, false));
		for (int i = 0; i < timesTable.size(); i++) {
			if (adjusted.compareTo(timesTable.get(i)) <= 0) {
				return 1501 - i;
			}
		}

		return -1;
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

	private class SwimCodeValue {

		private final Race race;
		private final boolean isWoman;

		public SwimCodeValue(Race pRace, boolean pIsWoman) {
			race = pRace;
			isWoman = pIsWoman;
		}

		@Override
		public boolean equals(Object other) {

			if (other == null || !(other instanceof SwimCodeValue)) {
				return false;
			}

			SwimCodeValue oth = (SwimCodeValue) other;
			if (isWoman && !oth.isWoman) {
				return false;
			}
			if (!isWoman && oth.isWoman) {
				return false;
			}

			return race.equals(oth.race);
		}

		@Override
		public int hashCode() {
			return (race.name() + isWoman).hashCode();
		}

		@Override
		public String toString() {
			return MessageFormat.format("[Race:{0}|isWoman:{1}]", race, isWoman);
		}

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
