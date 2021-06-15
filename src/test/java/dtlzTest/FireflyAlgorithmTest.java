package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.opt4j.benchmarks.dtlz.DTLZModule;
import org.opt4j.core.common.logger.LoggerModule;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.genotype.DoubleGenotype;
import org.opt4j.core.optimizer.Population;
import org.opt4j.core.start.Opt4JTask;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyAlgorithm;
import at.uibk.dps.optfund.dtlz.FireflyAlgorithmModule;
import at.uibk.dps.optfund.dtlz.FireflyFactory;

/**
 * Collection of tests for the FireflyAlgorithm class.
 * 
 * @author Josef Gugglberger
 *
 */
public class FireflyAlgorithmTest extends AbstractFireflyTest {

	protected static int POPULATION_SIZE = 10;

	protected static Rand random = mock(Rand.class);
	protected static FireflyFactory factory = mock(FireflyFactory.class);
	protected static FireflyAlgorithm alg = spy(
			new FireflyAlgorithm(null, factory, null, random, POPULATION_SIZE, 0, 0, 0));

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
		Firefly fly1 = spy(Firefly.class);
		Firefly fly2 = spy(Firefly.class);
		Firefly fly3 = spy(Firefly.class);

		fly1.setObjectives(getObjectives(3));
		fly2.setObjectives(getObjectives(2));
		fly3.setObjectives(getObjectives(1));

		List<Firefly> population = Arrays.asList(fly1, fly2, fly3);

		assertFalse(population.get(0).getIntensity() > population.get(1).getIntensity());

		population = alg.sortFireflies(population);

		assertTrue(population.get(0).getIntensity() > population.get(1).getIntensity());
		assertTrue(population.get(0).getIntensity() > population.get(2).getIntensity());
		assertTrue(population.get(1).getIntensity() > population.get(2).getIntensity());
	}

	@Test
	public void moveFireflyTest() {
		Firefly fly1 = spy(Firefly.class);
		Firefly fly2 = spy(Firefly.class);

		DoubleGenotype gen1 = spy(DoubleGenotype.class);
		DoubleGenotype gen2 = spy(DoubleGenotype.class);

		fly1.setGenotype(gen1);
		fly2.setGenotype(gen2);

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

	@Test
	public void fireflyAlgorithmTest() {
		// setup firefly algorithm
		FireflyAlgorithmModule fireflyAlgorithm = new FireflyAlgorithmModule();
		fireflyAlgorithm.setGenerations(100);
		fireflyAlgorithm.setPopulationSize(POPULATION_SIZE);
		fireflyAlgorithm.setAlpha(0.005); // no random walk
		fireflyAlgorithm.setBeta0(1d);
		fireflyAlgorithm.setGamma(0.01);

		// use DTLZ1 problem
		DTLZModule dtlz = new DTLZModule();
		dtlz.setFunction(DTLZModule.Function.DTLZ1);

		// write results into file
		LoggerModule viewer = new LoggerModule();
		String filename = "./build/out.tsv";
		File file = new File(filename);
		viewer.setFilename(filename);

		Opt4JTask task = new Opt4JTask(false);
		task.init(fireflyAlgorithm, dtlz, viewer);

		try {
			task.execute();

			Population population = task.getInstance(Population.class);
			// check if population size keeps constant
			assertEquals(POPULATION_SIZE, population.size());

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				List<String> iterations = new ArrayList<>();
				List<Double> objective1 = new ArrayList<>();
				List<Double> objective2 = new ArrayList<>();
				List<Double> objective3 = new ArrayList<>();
				boolean first = true;
				while ((line = br.readLine()) != null) {
					if (first) {
						first = !first;
						continue;
					}
					String[] cols = line.split("\t");
					iterations.add(cols[0]);
					objective1.add(Double.valueOf(cols[3]));
					objective2.add(Double.valueOf(cols[3]));
					objective3.add(Double.valueOf(cols[3]));
				}

				// check number of iterations
				assertEquals("1", iterations.get(0));
				assertEquals("100", iterations.get(iterations.size() - 1));

				// check if optimization found better solution
				assertTrue(objective1.get(0) >= objective1.get(objective1.size() - 1));
				assertTrue(objective2.get(0) >= objective1.get(objective2.size() - 1));
				assertTrue(objective3.get(0) >= objective1.get(objective3.size() - 1));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			task.close();
			// delete file again
			file.delete();
		}
	}

}
