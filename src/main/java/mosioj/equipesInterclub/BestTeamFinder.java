package mosioj.equipesInterclub;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

import mosioj.equipesInterclub.solution.Solution;
import mosioj.equipesInterclub.solution.SolutionBuilder;

/**
 * Launcher.
 * 
 */
public final class BestTeamFinder {

	/**
	 * Class logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(BestTeamFinder.class);

	/**
	 * Properties used.
	 */
	private final Properties prop;

	/**
	 * Interdit.
	 * 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private BestTeamFinder() throws FileNotFoundException, IOException {
		File file = new File(getClass().getResource("/my.properties").getFile());
		LOGGER.debug("Reading properties in : " + file.getAbsolutePath());
		prop = new Properties();
		prop.load(new FileInputStream(file));
	}

	/**
	 * 
	 * @param args Arguments.
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {

		BestTeamFinder btf = new BestTeamFinder();
		try {
			SolutionBuilder builder = new SolutionBuilder(new File(btf.prop.getProperty("OUTPUT_FILE")));
			Solution sol = builder.search();
			sol.print(builder.getSwimmers());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
