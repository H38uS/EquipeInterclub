package mosioj.equipesInterclub.ffn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.swimmer.SportMember;
import mosioj.equipesInterclub.swimmer.performance.Performance;

public class FFNReader {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(FFNReader.class);

	private static final String RACE_START = "<span style=\"border-bottom: 1px dotted #235991;"
			+ " font-weight: bold; color: #235991; font-size: 10pt;\">";

	private static final String PERF_START = "<tr onMouseOver=\"setPointer(this, '#FFE4C4');\" "
			+ "onMouseOut=\"setPointer(this, '');\">";

	/**
	 * Reads basic information, and fill it on the swimmer.
	 * 
	 * @param swimmer
	 * @throws IOException
	 */
	private void fillBasicInformation(SportMember swimmer) throws IOException {

		LOGGER.debug("Retrieving basic information for : " + swimmer);

		URL ffn = new URL("http://ffn.extranat.fr/webffn/_recherche.php?go=ind&idrch=" + swimmer.getFFNToString());
		LOGGER.debug("Contacting FFN website...");
		BufferedReader in = new BufferedReader(new InputStreamReader(ffn.openStream()));

		LOGGER.debug("Reading anwser...");
		String inputLine = in.readLine();
		JSonReader reader = new JSonReader(inputLine);
		Map<String, String> answer = reader.readIt();

		swimmer.setInformation(answer.get("iuf"), answer.get("ind"));

		if (in.readLine() != null) {
			LOGGER.error("Two many result found. Considering only the first one.");
			LOGGER.error("First one is: " + answer.get("ind"));
		}

		in.close();
	}

	/**
	 * 
	 * @param swimmer
	 * @return The list of performances found for this swimmer.
	 * @throws IOException
	 */
	public List<Performance> readRecord(SportMember swimmer) throws IOException {

		// Retrieving basic information
		fillBasicInformation(swimmer);

		// Getting the races
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		params.put("idrch_id", swimmer.getId());
		params.put("idopt", "prf");
		params.put("idbas", "25");

		StringBuilder postData = new StringBuilder();
		for (Map.Entry<String, Object> param : params.entrySet()) {
			if (postData.length() != 0)
				postData.append('&');
			postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
			postData.append('=');
			postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
		}
		byte[] postDataBytes = postData.toString().getBytes("UTF-8");

		URL url = new URL("http://ffn.extranat.fr/webffn/nat_recherche.php?idact=nat");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		conn.setDoOutput(true);
		conn.getOutputStream().write(postDataBytes);

		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"));
		return readLine(in);
	}

	/**
	 * Reads nbLines and return the last one.
	 * 
	 * @param in
	 * @param nbLines
	 * @throws IOException
	 */
	private String readNLines(BufferedReader in, int nbLines) throws IOException {

		String line = "";
		for (int i = 0; i < nbLines; i++) {
			line = in.readLine();
		}

		return line;
	}

	/**
	 * Extract the performances from the given reader.
	 * 
	 * @param in
	 * @throws IOException
	 */
	private List<Performance> readLine(BufferedReader in) throws IOException {

		List<Performance> perfs = new ArrayList<Performance>();

		String line = in.readLine();
		String race = "";
		while (line != null) {

			if (line.contains(RACE_START)) {
				line = in.readLine();
				race = line.trim();
				race = race.substring(0, race.indexOf(" - Bassin"));
			}

			// Reading a new performance
			if (line.contains(PERF_START)) {

				// Second gives the time performed
				line = in.readLine();
				final String time = getValueInTag(line);

				line = readNLines(in, 9);
				String date = getValueInTag(line);

				LOGGER.debug("New Performance read !! Is: " + race + " / " + time + " / " + date);
				perfs.add(new Performance(race, time, date));
			}

			line = in.readLine();
		}

		return perfs;
	}

	/**
	 * 
	 * 
	 * @param tag
	 * @return The value inside the last tag. Escape recursively tags if till found.
	 */
	public String getValueInTag(String tag) {

		tag = tag.trim();

		// Reading the first td tag
		LOGGER.trace("Reading open tag... Was: " + tag);
		tag = readOpenTag(tag);
		LOGGER.trace("After is: " + tag);

		// Reading inside tags
		while (tag.startsWith("<") && !tag.startsWith("</")) {

			int opened = 0;
			while (tag.startsWith("<") && !tag.startsWith("</")) {
				opened++;
				LOGGER.trace("Reading open tag... Was: " + tag);
				tag = readOpenTag(tag);
				LOGGER.trace("After is: " + tag);
			}

			String[] closing = tag.split("</");
			String[] opening = tag.split("<");
			if (closing.length == opening.length) {
				// We do have only closing tags
				// Exiting there
				break;
			}

			for (int i = 0; i < opened; i++) {
				LOGGER.trace("Reading closing tag... Was: " + tag);
				tag = readClosingTag(tag);
				LOGGER.trace("After is: " + tag);
			}

		}

		// Skipping the ending tags
		LOGGER.trace("Exiting ! Remaining: " + tag);
		return tag.indexOf("<") > -1 ? tag.substring(0, tag.indexOf("<")) : tag;
	}

	/**
	 * Reads the closing of the next tag, and return the remaining string.
	 * 
	 * @param tag
	 * @return
	 */
	public String readClosingTag(String tag) {

		if (!tag.contains("</")) {
			throw new IllegalStateException("Closing tag should start with \"<\" ! Found: " + tag);
		}
		tag.substring(tag.indexOf("</"));

		if (!tag.contains(">")) {
			throw new IllegalStateException("Cannot find the closing \">\"... Found: " + tag);
		}
		tag = tag.substring(tag.indexOf(">") + 1);

		return tag;
	}

	/**
	 * Reads the opening of the next tag, and return the remaining string.
	 * 
	 * @param tag
	 * @return
	 */
	public String readOpenTag(String tag) {

		if (!tag.startsWith("<")) {
			throw new IllegalStateException("The tag should start with \"<\" ! Found: " + tag);
		}

		if (tag.startsWith("</")) {
			throw new IllegalStateException("Closing tag found... String is: " + tag);
		}

		tag = tag.substring(1);

		if (!tag.contains(">")) {
			throw new IllegalStateException("Cannot find the closing \">\"... Found: " + tag);
		}

		while (tag.indexOf("<") >= 0 && (tag.indexOf("<") < tag.indexOf(">"))) {
			// Another tag inside...
			LOGGER.trace(">> Reading inside tag... Current is: " + tag.substring(tag.indexOf("<")));
			if (tag.indexOf("</") == tag.indexOf("<")) {
				tag = readClosingTag(tag.substring(tag.indexOf("<")));
			} else {
				tag = readOpenTag(tag.substring(tag.indexOf("<")));
			}
			LOGGER.trace(">> After is: " + tag);
		}

		tag = tag.substring(tag.indexOf(">") + 1);

		return tag;
	}
}
