package tspTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import at.uibk.dps.optfund.tsp.beecolony.Bee;
import at.uibk.dps.optfund.tsp.beecolony.FoodSource;

public class BeeTest {

	@Test
	public void testBee() {
		Bee bee = new Bee();
		FoodSource source = mock(FoodSource.class);
		bee.setMemory(source);

		assertEquals(bee.getMemory(), source);
	}

}
