package mosioj.equipesInterclub.tests.solution;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;

import mosioj.equipesInterclub.solution.DataFactory;
import mosioj.equipesInterclub.swimmer.Swimmer;
import mosioj.equipesInterclub.util.ExcelReader;

public class TestDataFactory {

	@Test
	public void test() throws Exception {
		File root = new File(getClass().getResource("/").getFile());
		List<List<String>> lines = ExcelReader.readLines(new File(root, "POINTS NAGEURS.xls"));
		List<Swimmer> swimmers = DataFactory.getSwimmers(lines);
		assertEquals(16, swimmers.size());
	}

}
