package at.uibk.dps.optfund.tsp.beecolony;

import java.util.*;
import java.util.stream.*;

public class FoodSource {
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

  public FoodSource neighbor(double[] phi, FoodSource otherSource) {
    var random = new Random();

    var vector = IntStream.range(0, this.vector.size())
      .mapToObj(__ -> {
        var i = random.nextInt(this.vector.size());
        return this.vector.get(i) + phi[i] * (this.vector.get(i) - otherSource.vector.get(i));
      })
      .collect(Collectors.toCollection(ArrayList::new));

    return new FoodSource(vector);
  }

  @Override
  public String toString() {
    return "FoodSource" + vector;
  }
}
