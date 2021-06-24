package at.uibk.dps.optfund.tsp.beecolony;

import com.google.common.collect.Streams;
import org.opt4j.core.*;
import org.opt4j.core.common.random.*;

import java.util.*;
import java.util.stream.*;

public class FoodSource extends Individual {
  ArrayList<Double> vector;

  private FoodSource(ArrayList<Double> vector) {
    this.vector = vector;
  }

  FoodSource(int n, double[] lowerBounds, double[] upperBounds) {
    var random = new Random();
    this.vector = IntStream.range(0, n)
      .mapToObj(i -> lowerBounds[i] + random.nextDouble() * (upperBounds[i] - lowerBounds[i]))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  public FoodSource generateNeighbor(FoodSource otherSource, Rand random, double a) {
    return new FoodSource(Streams.zip(this.vector.stream(), otherSource.vector.stream(), (m, k) -> {
      var phi = (random.nextDouble() * 2.0 - 1.0) * a;
      return m + phi * (m - k);
    }).collect(Collectors.toCollection(ArrayList::new)));
  }

  public double fitness() {
    return 0;
  }

  @Override
  public String toString() {
    return "FoodSource" + vector;
  }
}
