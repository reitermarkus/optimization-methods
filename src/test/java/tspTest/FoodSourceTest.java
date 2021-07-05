package tspTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.genotype.Bounds;
import org.opt4j.core.genotype.DoubleGenotype;
import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.tutorial.salesman.SalesmanProblem;
import org.opt4j.tutorial.salesman.SalesmanProblem.City;

import at.uibk.dps.optfund.tsp.beecolony.FoodSource;
import testUtils.TestUtils;

public class FoodSourceTest {

	private Bounds<Double> bound = mock(Bounds.class);
	private Rand rand = mock(Rand.class);
	private SalesmanProblem sp = mock(SalesmanProblem.class);

	List<SalesmanProblem.City> cities = Stream.generate(() -> {
		return sp.new City(rand.nextDouble() * 100, rand.nextDouble() * 100);
	}).limit(10).collect(Collectors.toList());

	@Test
	public void foodSourceEqualsTest() {

		when(rand.nextDouble()).thenReturn(0.5);

		FoodSource source1 = new FoodSource(TestUtils.getPermutationGenotype(cities));
		FoodSource source2 = new FoodSource(TestUtils.getPermutationGenotype(cities));

		assertTrue(source1.equals(source2));
		assertTrue(source2.equals(source1));
		assertTrue(source1.equals(source1));

		cities.set(0, sp.new City(0, 0));
		FoodSource source3 = new FoodSource(TestUtils.getPermutationGenotype(cities));

		assertFalse(source1.equals(source3));
		assertFalse(source1.equals(new DoubleGenotype()));
		assertFalse(source1.equals(null));
	}

	@Test
	public void foodSourceTest() {
		FoodSource source1 = new FoodSource(TestUtils.getPermutationGenotype(cities));

		source1.setObjectives(TestUtils.getObjectives(10));

		assertEquals(source1.fitness(), 10d, TestUtils.ATOL);

		source1.markForAbandonment();

		assertFalse(source1.shouldBeAbandoned(1));

		source1.markForAbandonment();

		assertTrue(source1.shouldBeAbandoned(1));
	}

	@Test
	public void foodSourceHashCodeTest() {
		PermutationGenotype<SalesmanProblem.City> gen = TestUtils.getPermutationGenotype(cities);
		FoodSource source1 = new FoodSource(gen);

		assertEquals(source1.hashCode(), gen.hashCode());
	}

	@Test
	public void generateNeighborTest() {

	}

}
