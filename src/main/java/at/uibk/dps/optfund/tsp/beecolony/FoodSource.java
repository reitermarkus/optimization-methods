package at.uibk.dps.optfund.tsp.beecolony;

import com.google.common.collect.Streams;
import com.google.inject.*;
import org.opt4j.benchmarks.*;
import org.opt4j.core.*;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.common.random.*;

import java.util.*;
import java.util.stream.*;

public class FoodSource extends Individual {
  // The name of the `fitness` minimization objective.
  static Objective FITNESS = new Objective("fitness", Sign.MIN);

  private int abandonmentCounter;

  private FoodSource(DoubleString genotype) {
    super();
    this.setGenotype(genotype);
  }

  @Inject
  FoodSource() {}

  @Override
  public DoubleString getGenotype() {
    return (DoubleString)super.getGenotype();
  }


  // Generate a new food source location by adding/subtracting a random offset
  // (between `-a` and `a`) in the direction of another food source.
  // See http://www.scholarpedia.org/article/Artificial_bee_colony_algorithm#Eq-6.
  public FoodSource generateNeighbor(FoodSource otherSource, Rand random, double a, FoodSourceFactory factory) {
    var locationA = this.getGenotype().stream();
    var locationB = otherSource.getGenotype().stream();

    var newLocation = Streams.zip(locationA, locationB, (m, k) -> {
      var phi = (random.nextDouble() * 2.0 - 1.0) * a;
      return m + phi * (m - k);
    }).collect(Collectors.toCollection(DoubleString::new));

    return factory.create(newLocation);
  }

  // Get the food source's fitness value.
  public double fitness() {
    return this.getObjectives().get(FITNESS).getDouble();
  }

  // Determine whether the food source should be abandoned, based on a given limit.
  public boolean shouldBeAbandoned(int limit) {
    return this.abandonmentCounter > limit;
  }

  // Mark the food source for abandonment, increasing its abandonment counter.
  public void markForAbandonment() {
    this.abandonmentCounter++;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(this.getGenotype());
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null) {
      return false;
    }

    if (this.getClass() != other.getClass()) {
      return false;
    }

    return this.getGenotype().equals(((FoodSource)other).getGenotype());
  }
}
