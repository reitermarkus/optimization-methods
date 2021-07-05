package tspTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.opt4j.benchmarks.DoubleString;
import org.opt4j.core.genotype.Bounds;

import at.uibk.dps.optfund.tsp.beecolony.FoodSource;
import testUtils.TestUtils;

public class FoodSourceTest {

	private Bounds<Double> bound = mock(Bounds.class);

	@Test
	public void foodSourceEqualsTest() {

		FoodSource source1 = new FoodSource(TestUtils.getDoubleString(0, 1));
		FoodSource source2 = new FoodSource(TestUtils.getDoubleString(0, 1));

		assertTrue(source1.equals(source2));
		assertTrue(source2.equals(source1));

		source2 = new FoodSource(TestUtils.getDoubleString(0, 10));

		assertFalse(source1.equals(source2));
		assertFalse(source1.equals(null));
		assertFalse(source1.equals(TestUtils.getDoubleString(1)));

	}

	@Test
	public void foodSourceTest() {
		FoodSource source1 = new FoodSource(TestUtils.getDoubleString(0, 1));

		source1.setObjectives(TestUtils.getObjectives(10));

		assertEquals(source1.fitness(), 10d, 0.001);

		source1.markForAbandonment();

		assertFalse(source1.shouldBeAbandoned(1));

		source1.markForAbandonment();

		assertTrue(source1.shouldBeAbandoned(1));
	}

	@Test
	public void foodSourceHashCodeTest() {
		DoubleString gen = TestUtils.getDoubleString(0, 1);
		FoodSource source1 = new FoodSource(gen);

		assertEquals(source1.hashCode(), gen.hashCode());
	}

}
