package mosioj.equipesInterclub.tests.solution;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mosioj.equipesInterclub.solution.TimeToPointsConverter;
import mosioj.equipesInterclub.swimmer.Category;
import mosioj.equipesInterclub.swimmer.Race;
import mosioj.equipesInterclub.swimmer.performance.Time;

public class TestTimeToPoints {

	@Test
	public void test() {
		
		TimeToPointsConverter converter = new TimeToPointsConverter();
		
		assertEquals(1100, converter.getPoints(Race._100NL, new Time("1:00.00"), Category.C1, false, false));
		// FIXME test it
	}

	@Test
	public void coefficientTest() {
		
		TimeToPointsConverter converter = new TimeToPointsConverter();

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
		assertEquals(0.999, converter.getCoefficient(Race._100PAP, Category.C2, false, false), 0.0001);
		assertEquals(1.134, converter.getCoefficient(Race._100PAP, Category.C7, false, false), 0.0001);
		assertEquals(2.032, converter.getCoefficient(Race._100PAP, Category.C12, false, false), 0.0001);
		
		// Test femme relai
		assertEquals(1.010, converter.getCoefficient(Race._100NL, Category.C2, true, true), 0.0001);
		assertEquals(1.172, converter.getCoefficient(Race._50BRASSE, Category.C3, true, true), 0.0001);
	}
}
