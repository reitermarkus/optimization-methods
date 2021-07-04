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
  private int limit;

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

  public FoodSource generateNeighbor(FoodSource otherSource, Rand random, double a, FoodSourceFactory factory) {
    var locationA = this.getGenotype().stream();
    var locationB = otherSource.getGenotype().stream();

    var newLocation = Streams.zip(locationA, locationB, (m, k) -> {
      var phi = (random.nextDouble() * 2.0 - 1.0) * a;
      return m + phi * (m - k);
    }).collect(Collectors.toCollection(DoubleString::new));

    return factory.create(newLocation);
  }

  public double fitness() {
    return this.getObjectives().get(new Objective("fitness", Sign.MIN)).getDouble();
  }

  public int getLimit() {
    return this.limit;
  }

  public void isStillTheBestEver() {
    this.limit++;
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
