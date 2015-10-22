package mosioj.equipesInterclub.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import mosioj.equipesInterclub.ffn.FFNReader;

public class TestFFNReader {

	FFNReader reader = new FFNReader();

	@Test
	public void readOpenTagTest() {

		String res = reader.readOpenTag("<tutu dzkidojz idjz >");
		assertEquals("", res);

		res = reader.readOpenTag("<td>100 NL </td>");
		assertEquals("100 NL </td>", res);

		res = reader.readOpenTag("<td class=\"nat-temps\"><a onMouseOut='hideddrivetip();' onMouse");
		assertEquals("<a onMouseOut='hideddrivetip();' onMouse", res);

		try {
			res = reader.readOpenTag("</tutu dzkidojz idjz >");
			fail("Should throw an exception !");
		} catch (IllegalStateException e) {
		}
	}

	@Test
	public void readClosingTagTest() {

		String res = reader.readClosingTag("</tutu dzkidojz idjz >");
		assertEquals("", res);

		res = reader.readClosingTag("</td>");
		assertEquals("", res);

		res = reader.readClosingTag("</td><td class=\'tooltipSplit\'>27.99</td><td class=\'tooltipLap\'>(27");
		assertEquals("<td class=\'tooltipSplit\'>27.99</td><td class=\'tooltipLap\'>(27", res);
	}

	@Test
	public void getValueInTagTest() {

		String res = reader.getValueInTag("<toto></ddokz k>");
		assertEquals("", res);

		res = reader.getValueInTag("<td>100 NL </td>");
		assertEquals("100 NL ", res);

		res = reader.getValueInTag("<td class=nat-temps><a onMouseOut='hideditOn'>2:09.09</a></td>");
		assertEquals("2:09.09", res);

		res = reader.getValueInTag("<td class=nat-temps><a onMouseOut='hideddrivetip();' onMouseOver=ddrivetip"
				+ "('<table id=\'styleNoBorderNoBottom\' cellpadding=\'1\' cellspacing=\'0\' border=\'0\'>"
				+ "<tr><td class=\'tooltipDis\'>50 m : </td><td class=\'tooltipSplit\'>"
				+ "27.99</td><td class=\'tooltipLap\'>(27.99)</td><td class=\'tooltipRelay\'>"
				+ "</td></tr><tr><td class=\'tooltipDis\'>100 m : </td><td class=\'tooltipSplit\'>"
				+ "57.62</td><td class=\'tooltipLap\'>(29.63)</td><td class=\'tooltipRelay\'>[57.62]"
				+ "</td></tr></table>',0); class='splitOn'>57.62</a></td>");
		assertEquals("57.62", res);

	}

}
