package mosioj.equipesInterclub.tests.swimmer.performance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mosioj.equipesInterclub.swimmer.Category;

public class TestCategory {

	@Test
	public void test() {
		assertEquals(Category.C1, Category.readCategory(1));
		assertEquals(Category.C1, Category.readCategory(24));
		assertEquals(Category.C1, Category.readCategory(25));
		assertEquals(Category.C1, Category.readCategory(29));
		assertEquals(Category.C2, Category.readCategory(30));
		assertEquals(Category.C5, Category.readCategory(47));
		assertEquals(Category.C14, Category.readCategory(90));
		assertEquals(Category.C14, Category.readCategory(94));
		assertEquals(Category.C14, Category.readCategory(97));
	}

}
