package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.genotype.DoubleGenotype;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyAlgorithm;
import at.uibk.dps.optfund.dtlz.FireflyFactory;

public class FireflyAlgorithmTest {

	private static FireflyFactory factory = mock(FireflyFactory.class);

	private static int POPULATION_SIZE = 10;
	private static Rand random = mock(Rand.class);
	private static FireflyAlgorithm alg = spy(
			new FireflyAlgorithm(null, factory, null, random, POPULATION_SIZE, 0, 0, 0));

	protected static final Objective firstObj = new Objective("first", Sign.MAX);
	protected static Firefly fly1 = spy(Firefly.class);
	protected static Firefly fly2 = spy(Firefly.class);
	protected static Firefly fly3 = spy(Firefly.class);

	protected static final DoubleGenotype gen1 = spy(DoubleGenotype.class);
	protected static final DoubleGenotype gen2 = spy(DoubleGenotype.class);

	@Test
	public void initPopulationTest() {
		for (int i = 0; i < POPULATION_SIZE; i++) {
			when(factory.create(i)).thenReturn(new Firefly(i));
		}
		List<Firefly> population = alg.getInitPopulation();

		assertEquals(10, population.size());

		for (int i = 0; i < POPULATION_SIZE; i++) {
			assertEquals(i, population.get(i).getId());
		}
	}

	@Test
	public void sortFirefliesTest() {
		when(fly1.getObjectives()).thenReturn(getObjectives(3));
		when(fly2.getObjectives()).thenReturn(getObjectives(2));
		when(fly3.getObjectives()).thenReturn(getObjectives(1));

		List<Firefly> population = Arrays.asList(fly1, fly2, fly3);

		assertFalse(population.get(0).getIntensity() > population.get(1).getIntensity());

		population = alg.sortFireflies(population);

		assertTrue(population.get(0).getIntensity() > population.get(1).getIntensity());
		assertTrue(population.get(0).getIntensity() > population.get(2).getIntensity());
		assertTrue(population.get(1).getIntensity() > population.get(2).getIntensity());
	}

	@Test
	public void moveFireflyTest() {
		when(fly1.getGenotype()).thenReturn(gen1);
		when(fly2.getGenotype()).thenReturn(gen2);

		gen1.init(new Random(), 1);
		when(gen1.getLowerBound(0)).thenReturn(0d);
		when(gen1.getUpperBound(0)).thenReturn(1d);
		when(gen1.get(0)).thenReturn(0d);

		gen2.init(new Random(), 1);
		when(gen2.getLowerBound(0)).thenReturn(0d);
		when(gen2.getUpperBound(0)).thenReturn(1d);
		when(gen2.get(0)).thenReturn(1d);

		when(random.nextDouble()).thenReturn(0d);

		DoubleGenotype newPosition = alg.move(fly1, fly2);

		assertEquals(Arrays.asList(0d), newPosition);
	}

	protected static Objectives getObjectives(int... objectives) {
		Objectives result = new Objectives();
		for (double obj : objectives) {
			result.add(firstObj, obj);
		}
		return result;
	}

}
