package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.Comparator;

import org.junit.Test;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyComparator;

/**
 * Collection of tests for the Firefly class.
 * 
 * @author Josef Gugglberger
 *
 */
public class FireflyTest extends AbstractFireflyTest {

	@Test
	public void testFireflyDistanceMethod() {

		Firefly fly1 = spy(Firefly.class);
		Firefly fly2 = spy(Firefly.class);

		// test genotype with 2 dimensions
		fly1.setGenotype(getGenotype(0.0, 0.0));
		fly2.setGenotype(getGenotype(1.0, 1.0));

		assertEquals(Math.sqrt(2), fly1.distance(fly2), 1e-3);

		// test with itself
		fly1.setGenotype(getGenotype(1.0, 1.0));
		assertEquals(0.0, fly1.distance(fly1), 1e-3);
	}

	@Test
	public void testFireflyIntensityMethod() {
		Firefly fly = spy(Firefly.class);

		fly.setObjectives(getObjectives(1));
		assertEquals(1.0 / 2.0, fly.getIntensity(), 1e-3);

		fly.setObjectives(getObjectives(0));
		assertEquals(1.0, fly.getIntensity(), 1e-3);
	}

	@Test
	public void testFireflyErrorMethod() {
		Firefly fly = spy(Firefly.class);

		fly.setObjectives(getObjectives(2));
		assertEquals(2.0, fly.getError(), 1e-3);

		fly.setObjectives(getObjectives(7));
		assertEquals(7.0, fly.getError(), 1e-3);
	}

	@Test
	public void testFireflyIdMethod() {
		Firefly fly = spy(Firefly.class);
		fly.setId(7);
		assertEquals(7, fly.getId());
	}

	@Test
	public void testFireflyToStringMethod() {
		Firefly fly = spy(Firefly.class);
		fly.setObjectives(getObjectives(1));

		assertEquals("Firefly: { id: 0, intensity: 0.5, error: 1.0 }", fly.toString());
	}

	@Test
	public void testFireflyComparator() {

		Firefly fly1 = spy(Firefly.class);
		Firefly fly2 = spy(Firefly.class);

		Comparator<Firefly> comp = new FireflyComparator();

		fly1.setObjectives(getObjectives(1));
		fly2.setObjectives(getObjectives(1));

		assertEquals(0, comp.compare(fly1, fly2));

		fly1.setObjectives(getObjectives(2));
		fly2.setObjectives(getObjectives(1));

		assertTrue(comp.compare(fly1, fly2) > 0);

		fly1.setObjectives(getObjectives(1));
		fly2.setObjectives(getObjectives(2));

		assertTrue(comp.compare(fly1, fly2) < 0);

	}

}
