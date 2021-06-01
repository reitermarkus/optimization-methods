import java.util.*;
import java.util.stream.*;

class BeeColonyOptimizer {
  public static void main(String[] args) {
    var optimizer = new BeeColonyOptimizer();

    optimizer.optimize(13, 4, new double[] {0, 0, 0, 0}, new double[] {1, 1, 1, 1});
  }

  BeeColonyOptimizer() {}

  void optimize(int populationSize, int n, double[] lowerBounds, double[] upperBounds) {
    var foodSources = IntStream.range(0, populationSize)
      .mapToObj(m -> new FoodSource(n, lowerBounds, upperBounds))
      .collect(Collectors.toList());

    System.out.println(foodSources);

    var a = 1.0;
    var random = new Random();
    var phi = IntStream.range(0, n).mapToDouble(i -> (random.nextDouble() * 2.0 - 1.0) * a).toArray();
    var newSource = foodSources.get(0).neighbor(phi, foodSources.get(1));

    System.out.println(newSource);
  }
}
