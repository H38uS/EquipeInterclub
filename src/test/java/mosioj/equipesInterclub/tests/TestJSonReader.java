package mosioj.equipesInterclub.tests;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

import mosioj.equipesInterclub.ffn.JSonReader;

public class TestJSonReader {

	@Test
	public void test() {

		JSonReader reader = new JSonReader(
				"[{\"iuf\":\"354292\",\"ind\":\"MOSIO Jordan (1989) FRA\",\"clb\":\"NAUTIC CLUB ALP'38 *\"}]");
		Map<String, String> result = reader.readIt();

		assertEquals(3, result.size());
		assertEquals("354292", result.get("iuf"));
		assertEquals("MOSIO Jordan (1989) FRA", result.get("ind"));
		assertEquals("NAUTIC CLUB ALP'38 *", result.get("clb"));

		// Quand on en a plusieurs, on prend le 1er
		reader = new JSonReader("[{\"iuf\":\"1075224\",\"ind\":\"BARRET Cédric (1974) FRA\","
				+ "\"clb\":\"NAUTIC CLUB ALP'38 *\"}," + "{\"iuf\":\"1375288\",\"ind\":\"BARRET Cédric (1970) FRA\","
				+ "\"clb\":\"SAUMUR NATATION *\"}]");
		result = reader.readIt();
		
		assertEquals(3, result.size());
		assertEquals("1075224", result.get("iuf"));
		assertEquals("BARRET Cédric (1974) FRA", result.get("ind"));
		assertEquals("NAUTIC CLUB ALP'38 *", result.get("clb"));
	}

}
