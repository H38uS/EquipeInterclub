package mosioj.equipesInterclub;

import java.io.File;
import java.io.IOException;

import mosioj.equipesInterclub.solution.Solution;
import mosioj.equipesInterclub.solution.SolutionBuilder;

/**
 * Launcher.
 * 
 */
public final class App {

	/**
	 * Interdit.
	 */
	private App() {
	}

	/**
	 * 
	 * @param args Arguments.
	 */
	public static void main(String[] args) {
		Object o = new Object();
		try {
			File root = new File(o.getClass().getResource("/").getFile());
			SolutionBuilder builder = new SolutionBuilder(new File(root, "POINTS NAGEURS.xls"));
			Solution sol = builder.search();
			sol.print(builder.getSwimmers());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
