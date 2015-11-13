package mosioj.equipesInterclub.tests.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import mosioj.equipesInterclub.util.ExcelReader;

public class TestExcelReader {

	private static List<List<String>> testResults;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Object o = new Object();
		File root = new File(o.getClass().getResource("/").getFile());
		File file = new File(root, "POINTS NAGEURS.xlsx");
		testResults = ExcelReader.readLines(file, "Sheet0");
	}

	/**
	 * Vérifie que la ligne est égale au tableau passé en paramètre.
	 * 
	 * @param expected
	 * @param lineNumber
	 */
	private void testLine(String[] expected, int lineNumber) {
		List<String> line = testResults.get(lineNumber);
		assertEquals(expected.length, line.size());
		for (int i = 0; i < expected.length; i++) {
			assertEquals(expected[i], line.get(i));
		}
	}

	@Test
	public void testCount() {
		assertEquals(345, testResults.size());
	}

	@Test
	public void testFirstLine() {
		String[] expected = { "NOM", "PRENOM", "NAGE", "Année Naissance", "Homme / Femme", "TEMPS" };
		testLine(expected, 0);
	}

	@Test
	public void testMidEmptyLine() {
		String[] expected = { null, null, "50BRASSE", null, null, "49.34" };
		testLine(expected, 3);
	}

	@Test
	public void testMidDefLine() {
		String[] expected = { "Joury", "Antoine", "50PAP", "1973.0", "H", "31.09" };
		testLine(expected, 187);
	}

	@Test
	public void testLastLine() {
		String[] expected = { null, null, "400NL", null, null, "6:36.80" };
		testLine(expected, 344);
	}
}
