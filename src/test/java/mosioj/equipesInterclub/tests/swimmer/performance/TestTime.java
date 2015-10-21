package mosioj.equipesInterclub.tests.swimmer.performance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mosioj.equipesInterclub.swimmer.performance.Time;

public class TestTime {

	@Test
	public void initTest() {

		Time time = new Time("1:32.00");
		assertEquals(9200, time.getAsLong());

		time = new Time("5.00");
		assertEquals(500, time.getAsLong());

		time = new Time("5.17");
		assertEquals(517, time.getAsLong());
		

		time = new Time("2:05.17");
		assertEquals(12517, time.getAsLong());
	}

}
