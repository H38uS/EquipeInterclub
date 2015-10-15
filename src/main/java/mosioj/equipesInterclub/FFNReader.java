package mosioj.equipesInterclub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import equipesInterclub.SolutionBuilder;
import equipesInterclub.swimmer.Swimmer;

public class FFNReader {
	
	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(FFNReader.class);

	/**
	 * Reads basic information, and fill it on the swimmer.
	 * 
	 * @param swimmer
	 * @throws IOException
	 */
	private void fillBasicInformation(Swimmer swimmer) throws IOException {
	
		LOGGER.debug("Retrieving basic information for : " + swimmer);

		URL ffn = new URL("http://ffn.extranat.fr/webffn/_recherche.php?go=ind&idrch=" + swimmer);
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
}
