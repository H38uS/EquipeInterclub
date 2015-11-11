package mosioj.equipesInterclub.ffn;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class JSonReader {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(JSonReader.class);

	/**
	 * The JSon string to parse.
	 */
	private String json;

	/**
	 * 
	 * @param pjson The JSon string to parse.
	 */
	public JSonReader(String pjson) {
		LOGGER.debug("Initialized with value: \"" + pjson + "\"");
		json = pjson;
	}
	
	/**
	 * Only supports basic JSON for the moment : [{*}].
	 * 
	 * @return The answer decoded. Each element is a pair key / value.
	 */
	public Map<String, String> readIt() {
		
		Map<String, String> answer = new HashMap<String, String>();
		LOGGER.debug("Parsing json...");
		
		// Removing last bracket/parenthesis
		// And first bracket
		// Keeping first parenthesis to act as comma
		json = json.substring(1, json.length() - 2);

		// We read only the first value
		while (json.length() > 0 && !json.startsWith("},{")) {
			
			// Reading the first comma / parenthesis
			json = json.substring(1);
			
			// Reading the key
			String key = readToken();
			
			// Reading the semicolon
			json = json.substring(1);
			
			// And then the value
			String value = readToken();
			
			// Registering this pair
			answer.put(key, value);
		}
		
		if (json.length() > 0) {
			LOGGER.warn("Multiple parts found... Reading only the first one.");
		}

		LOGGER.debug("Done !");
		return answer;
	}
	
	/**
	 * Reads the next token and return it.
	 * 
	 * @return The token value.
	 * @throws IllegalStateException
	 */
	private String readToken() throws IllegalStateException {
		
		if (!json.startsWith("\"")) {
			throw new IllegalStateException("A token must start by a quote ! Remaining string is: " + json);
		}
		
		json = json.substring(1);
		int index = json.indexOf("\"");
		if (index < 0) {
			throw new IllegalStateException("No more quote found... Remaining string is: " + json);
		}
		
		String value = json.substring(0, index);
		json = json.substring(index + 1);
		
		return value;
	}
	
}
