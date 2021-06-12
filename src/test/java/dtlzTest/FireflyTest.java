package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Comparator;

import org.junit.Test;
import org.opt4j.core.Genotype;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.genotype.DoubleGenotype;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyComparator;

public class FireflyTest {

	protected static Firefly fly1 = spy(Firefly.class);
	protected static Firefly fly2 = spy(Firefly.class);

	protected static final Objective firstObj = new Objective("first", Sign.MAX);

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
	public void testFireflyComparator() {

		Comparator<Firefly> comp = new FireflyComparator();

		when(fly1.getObjectives()).thenReturn(getObjectives(1));
		when(fly2.getObjectives()).thenReturn(getObjectives(1));

		assertEquals(-1, comp.compare(fly1, fly2));

		when(fly1.getObjectives()).thenReturn(getObjectives(2));
		when(fly2.getObjectives()).thenReturn(getObjectives(1));

		assertTrue(comp.compare(fly1, fly2) > 0);

		when(fly1.getObjectives()).thenReturn(getObjectives(-1));
		when(fly2.getObjectives()).thenReturn(getObjectives(1));

		assertTrue(comp.compare(fly1, fly2) < 0);

	}

	// Helpers

	private static Genotype getGenotype(double... values) {
		DoubleGenotype type = new DoubleGenotype();
		for (double value : values) {
			type.add(value);
		}
		return type;
	}

	protected static Objectives getObjectives(int... objectives) {
		Objectives result = new Objectives();
		for (double obj : objectives) {
			result.add(firstObj, obj);
		}
		return result;
	}

}
