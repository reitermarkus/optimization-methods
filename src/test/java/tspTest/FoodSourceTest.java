package tspTest;

import at.uibk.dps.optfund.tsp.beecolony.*;
import org.junit.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.genotype.*;
import org.opt4j.tutorial.salesman.*;
import testUtils.*;

import java.util.*;
import java.util.stream.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for the {@link FoodSource}.
 *
 * @author Markus Reiter
 * @author Michael Kaltschmid
 */
public class FoodSourceTest {
  private final Bounds<Double> bound = mock(Bounds.class);
  private final Rand rand = mock(Rand.class);
  private final SalesmanProblem sp = mock(SalesmanProblem.class);

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
    assertEquals(source1.fitness(), 0.1, TestUtils.ATOL);

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
}
