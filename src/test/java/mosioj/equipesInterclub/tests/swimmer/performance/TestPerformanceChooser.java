package mosioj.equipesInterclub.tests.swimmer.performance;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import mosioj.equipesInterclub.swimmer.performance.PerfChooser;
import mosioj.equipesInterclub.swimmer.performance.Performance;
import mosioj.equipesInterclub.swimmer.performance.Time;

public class TestPerformanceChooser {

	@Test
	public void averageTest() {
		
		String race = "100 Nage Libre";
		List<Performance> perfs = new ArrayList<Performance>();
		perfs.add(new Performance(race, "59.00", "17/02/2015"));
		perfs.add(new Performance(race, "1:00.00", "17/02/2015"));
		perfs.add(new Performance(race, "1:01.00", "17/02/2015"));
		
		PerfChooser chooser = new PerfChooser(perfs);
		assertEquals(new Time("1:00.00"), chooser.average(perfs));
	}

	
}
