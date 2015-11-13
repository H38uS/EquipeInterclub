package mosioj.equipesInterclub.tests.solution;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import mosioj.equipesInterclub.solution.TimeToPointsConverter;
import mosioj.equipesInterclub.swimmer.Category;
import mosioj.equipesInterclub.swimmer.Race;
import mosioj.equipesInterclub.swimmer.performance.Time;

public class TestTimeToPoints {
	
	private static TimeToPointsConverter converter;

	@BeforeClass
	public static void setUp() {
		converter = new TimeToPointsConverter();
	}

	@Test
	public void singleRaceTest() {

		// Femme
		assertEquals(1184, converter.getPoints(Race._100NL, new Time("1:00.00"), Category.C1, true));
		assertEquals(1217, converter.getPoints(Race._100NL, new Time("59.00"), Category.C1, true));
		assertEquals(1304, converter.getPoints(Race._100NL, new Time("59.00"), Category.C4, true));
		
		assertEquals(1139, converter.getPoints(Race._50BRASSE, new Time("36.00"), Category.C1, true));
		assertEquals(1160, converter.getPoints(Race._50BRASSE, new Time("36.00"), Category.C2, true));
		
		// Homme
		assertEquals(1124, converter.getPoints(Race._100PAP, new Time("1:00.01"), Category.C1, false));
		assertEquals(1154, converter.getPoints(Race._100PAP, new Time("59.00"), Category.C1, false));
		assertEquals(1182, converter.getPoints(Race._100PAP, new Time("59.00"), Category.C4, false));
		
		assertEquals(1001, converter.getPoints(Race._50DOS, new Time("31.52"), Category.C1, false));
		assertEquals(998, converter.getPoints(Race._50DOS, new Time("31.57"), Category.C1, false));
		assertEquals(1132, converter.getPoints(Race._50DOS, new Time("31.57"), Category.C5, false));
	}

	@Test
	public void relayTest() {
		
		Time[] times = {new Time("32.18"), new Time("31.77"), new Time("26.42"), new Time("25.59")};
		Category[] categories = {Category.C1, Category.C2, Category.C4, Category.C3};
		boolean[] isWoman = {true, false, false, true};
		assertEquals(1293, converter.getPointsOf4x50Relay(times, categories, isWoman));

		Time[] times2 = {new Time("35.17"), new Time("43.65"), new Time("33.15"), new Time("31.13")};
		Category[] categories2 = {Category.C7, Category.C10, Category.C4, Category.C3};
		boolean[] isWoman2 = {true, true, false, true};
		assertEquals(1237, converter.getPointsOf4x50Relay(times2, categories2, isWoman2));

		Time[] times3 = {new Time("33.49"), new Time("50.12"), new Time("27.72"), new Time("24.35")};
		Category[] categories3 = {Category.C3, Category.C12, Category.C3, Category.C3};
		boolean[] isWoman3 = {true, false, false, false};
		assertEquals(0, converter.getPointsOf4x50Relay(times3, categories3, isWoman3));
	}

	@Test
	public void coefficientTest() {

		// Test femme classique
		assertEquals(1.000, converter.getCoefficient(Race._100NL, Category.C1, true, false), 0.0001);
		assertEquals(1.010, converter.getCoefficient(Race._100NL, Category.C2, true, false), 0.0001);
		assertEquals(1.047, converter.getCoefficient(Race._100NL, Category.C4, true, false), 0.0001);
		assertEquals(1.236, converter.getCoefficient(Race._100NL, Category.C8, true, false), 0.0001);

		assertEquals(1.000, converter.getCoefficient(Race._50BRASSE, Category.C1, true, false), 0.0001);
		assertEquals(1.029, converter.getCoefficient(Race._50BRASSE, Category.C3, true, false), 0.0001);
		assertEquals(1.175, converter.getCoefficient(Race._50BRASSE, Category.C7, true, false), 0.0001);

		assertEquals(1.059, converter.getCoefficient(Race._2004N, Category.C4, true, false), 0.0001);

		// Test mec
		assertEquals(1.000, converter.getCoefficient(Race._100PAP, Category.C1, false, false), 0.0001);
		assertEquals(1.002, converter.getCoefficient(Race._100PAP, Category.C2, false, false), 0.0001);
		assertEquals(1.134, converter.getCoefficient(Race._100PAP, Category.C7, false, false), 0.0001);
		assertEquals(2.032, converter.getCoefficient(Race._100PAP, Category.C12, false, false), 0.0001);

		// Test femme relai
		assertEquals(1.010, converter.getCoefficient(Race._100NL, Category.C2, true, true), 0.0001);
		assertEquals(1.172, converter.getCoefficient(Race._50BRASSE, Category.C3, true, true), 0.0001); // FIXME relai pas lu
	}
}
