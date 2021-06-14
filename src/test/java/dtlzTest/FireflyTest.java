package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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

		// test genotype with 2 dimensions
		when(fly1.getGenotype()).thenReturn(getGenotype(0.0, 0.0));
		when(fly2.getGenotype()).thenReturn(getGenotype(1.0, 1.0));

		assertEquals(Math.sqrt(2), fly1.distance(fly2), 1e-3);

		// test with itself
		when(fly1.getGenotype()).thenReturn(getGenotype(1.0, 1.0));
		assertEquals(0.0, fly1.distance(fly1), 1e-3);

		// test genotype with 3 dimensions
		when(fly1.getGenotype()).thenReturn(getGenotype(2.0, 2.0, 2.0));
		when(fly2.getGenotype()).thenReturn(getGenotype(0.0, 0.0, 0.0));
		assertEquals(Math.sqrt(12), fly1.distance(fly2), 1e-3);
	}

	@Test
	public void testFireflyIntensityMethod() {
		when(fly1.getObjectives()).thenReturn(getObjectives(1));

		assertEquals(1.0 / 2.0, fly1.getIntensity(), 1e-3);

		when(fly1.getObjectives()).thenReturn(getObjectives(0));

		assertEquals(1.0, fly1.getIntensity(), 1e-3);

	}

	@Test
	public void testFireflyIdMethod() {
		fly1.setId(0);
		assertEquals(0, fly1.getId());
	}

	@Test
	public void testFireflyToStringMethod() {
		fly1.setId(1);
		fly1.setObjectives(getObjectives(0, 1));

		assertEquals("Firefly: { id: 1, intensity: 0.5, error: 1.0 }", fly1.toString());
	}

	@Test
	public void testFireflyComparator() {

		Comparator<Firefly> comp = new FireflyComparator();

		when(fly1.getObjectives()).thenReturn(getObjectives(1));
		when(fly2.getObjectives()).thenReturn(getObjectives(1));

		assertEquals(0, comp.compare(fly1, fly2));

		when(fly1.getObjectives()).thenReturn(getObjectives(2));
		when(fly2.getObjectives()).thenReturn(getObjectives(1));

		assertTrue(comp.compare(fly1, fly2) > 0);

		when(fly1.getObjectives()).thenReturn(getObjectives(1));
		when(fly2.getObjectives()).thenReturn(getObjectives(2));

		assertTrue(comp.compare(fly1, fly2) < 0);

	}

}
