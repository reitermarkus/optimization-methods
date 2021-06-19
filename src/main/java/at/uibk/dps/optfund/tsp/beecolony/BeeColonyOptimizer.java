package at.uibk.dps.optfund.tsp.beecolony;

import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.inject.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

class BeeColonyOptimizer implements IterativeOptimizer {
  private final Population population;

  protected final int populationSize;
  protected final int n;
  protected final double lowerBound;
  protected final double upperBound;

  @Inject
  public BeeColonyOptimizer(
    Population population,
    @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
    @Constant(value = "n", namespace = BeeColonyOptimizer.class) int n,
    @Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class) double lowerBound,
    @Constant(value = "upperBound", namespace = BeeColonyOptimizer.class) double upperBound
  ) {
    this.population = population;
    this.populationSize = populationSize;
    this.n = n;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  public static void main(String[] args) {
    // var optimizer = new BeeColonyOptimizer();

    // optimizer.optimize(13, 4, new double[] {0, 0, 0, 0}, new double[] {1, 1, 1, 1});
  }

  void optimize(int populationSize, int n, double[] lowerBounds, double[] upperBounds) {
    var foodSources = IntStream.range(0, populationSize)
      .mapToObj(m -> new FoodSource(n, lowerBounds, upperBounds))
      .collect(Collectors.toList());

	@Inject
	public BeeColonyOptimizer(Population population,
			@Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
			@Constant(value = "n", namespace = BeeColonyOptimizer.class) int n,
			@Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class) double lowerBound,
			@Constant(value = "upperBound", namespace = BeeColonyOptimizer.class) double upperBound) {
		this.population = population;
		this.populationSize = populationSize;
		this.n = n;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
	}

	public static void main(String[] args) {
		// var optimizer = new BeeColonyOptimizer();

    System.out.println(newSource);
  }

  @Override
  public void initialize() throws TerminationException {
    System.out.println("TEST initialize");
  }

  @Override
  public void next() throws TerminationException {
    System.out.println("TEST next");
  }
}
