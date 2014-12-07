package equipesInterclub.Util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import equipesInterclub.util.ExcelReader;

public class TestExcelReader {

	private static List<List<String>> testResults;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Object o = new Object();
		File root = new File(o.getClass().getResource("/").getFile());
		File file = new File(root, "POINTS NAGEURS.xls");
		testResults = ExcelReader.readLines(file);
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
		assertEquals(48, testResults.size());
	}

	@Test
	public void testFirstLine() {
		String[] expected = { "NOM", "PRENOM", "NAGE", "POINTS", "Homme / Femme" };
		testLine(expected, 0);
	}

	@Test
	public void testMidEmptyLine() {
		String[] expected = { "100NL", "1128" };
		testLine(expected, 4);
	}

	@Test
	public void testMidDefLine() {
		String[] expected = { "JOURY", "ANTOINE", "50NL", "1070", "H" };
		testLine(expected, 11);
	}

	@Test
	public void testLastLine() {
		String[] expected = { "50NL", "1083" };
		testLine(expected, 47);
	}
}
