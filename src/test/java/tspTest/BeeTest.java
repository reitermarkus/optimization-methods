package tspTest;

import at.uibk.dps.optfund.tsp.beecolony.*;
import org.junit.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the {@link Bee}.
 *
 * @author Markus Reiter
 * @author Michael Kaltschmid
 */
public class BeeTest {
  @Test
  public void testBee() {
    Bee bee = new Bee();
    FoodSource source = mock(FoodSource.class);
    bee.setMemory(source);

    assertEquals(bee.getMemory(), source);
  }
}
