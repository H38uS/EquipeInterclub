package mosioj.equipesInterclub.tests.swimmer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import mosioj.equipesInterclub.swimmer.Sexe;

public class TestSexe {

	@Test
	public void testH() {
		Sexe sexe = Sexe.valueOf("H");
		assertTrue(sexe == Sexe.H);
	}

	@Test
	public void testAutre() {
		try {
			Sexe.valueOf("Femme");
			fail("Should throw an exception !");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testF() {
		Sexe sexe = Sexe.valueOf("F");
		assertTrue(sexe == Sexe.F);
	}

}
