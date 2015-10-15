package mosioj.equipesInterclub.tests.swimmer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import equipesInterclub.Race;
import equipesInterclub.swimmer.Nageur;

public class TestSportMember {

	@Test
	public void test() {
		
		TestNageurInstance member = new TestNageurInstance("mosio", "jtt", null, 0);
		member.setInformation("354292", "MOSIO Jordan (1989) FRA");
		
		assertEquals(1989, member.getBirthYear());
	}

	private class TestNageurInstance extends Nageur {

		public TestNageurInstance(String pSurname, String pName, Race race, int points) {
			super(pSurname, pName, race, points);
		}
		
		private int getBirthYear() {
			return birthYear;
		}
	}
	
}
