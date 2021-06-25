package at.uibk.dps.optfund.tsp.beecolony;

import java.util.*;
import java.util.stream.*;

import at.uibk.dps.optfund.dtlz.*;
import com.google.inject.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

class BeeColonyOptimizer implements IterativeOptimizer {
  private final Population population;
  protected final Rand random;

  protected final int populationSize;
  protected final double alpha;
  protected final int n;
  protected final double[] lowerBounds;
  protected final double[] upperBounds;

  protected final List<EmployedBee> employedBees = new ArrayList<>();
  protected final List<OnlookerBee> onlookerBees = new ArrayList<>();
  protected final List<ScoutBee> scoutBees = new ArrayList<>();

  @Inject
  public BeeColonyOptimizer(
    Population population,
    Rand random,
    @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
    @Constant(value = "alpha", namespace = BeeColonyOptimizer.class) double alpha,
    @Constant(value = "n", namespace = BeeColonyOptimizer.class) int n,
    @Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class) double lowerBound,
    @Constant(value = "upperBound", namespace = BeeColonyOptimizer.class) double upperBound
  ) {
    this.population = population;
    this.random = random;
    this.populationSize = populationSize;
    this.alpha = alpha;
    this.n = n;
    this.lowerBounds = DoubleStream.generate(() -> lowerBound).limit(n).toArray();
    this.upperBounds = DoubleStream.generate(() -> upperBound).limit(n).toArray();
  }

  public static void main(String[] args) {
    // var optimizer = new BeeColonyOptimizer();

    // optimizer.optimize(13, 4, new double[] {0, 0, 0, 0}, new double[] {1, 1, 1, 1});
  }

  void optimize(int populationSize, int n, double[] lowerBounds, double[] upperBounds) {
    var a = 1.0;
    var random = new Random();
    // var newSource = foodSources.get(0).neighbor(phi, foodSources.get(1));

    // System.out.println(newSource);
  }

  @Override
  public void initialize() throws TerminationException {
    var foodSources = Stream.generate(() -> new FoodSource(this.n, this.lowerBounds, this.upperBounds))
      .limit(this.populationSize)
      .collect(Collectors.toList());

    this.population.addAll(foodSources);
  }

  static double objectiveFunction(FoodSource foodSource) {
    // TODO
    return 0;
  }

  void employedBeesPhase() {
    var foodSources = this.population.stream().map(f -> (FoodSource) f).collect(Collectors.toList());

    this.employedBees.forEach(bee -> {
      var foodSource = bee.getMemory();
      foodSources.remove(foodSource);

      var j = this.random.nextInt(foodSources.size());
      var randomFoodSource = foodSources.get(j);
      var newFoodSource = foodSource.generateNeighbor(randomFoodSource, random, this.alpha);

      var fitness = foodSource.fitness(BeeColonyOptimizer::objectiveFunction);
      var newFitness = newFoodSource.fitness(BeeColonyOptimizer::objectiveFunction);
      var selectedFoodSource = newFitness > fitness ? newFoodSource : foodSource;

      bee.setMemory(selectedFoodSource);
      foodSources.add(selectedFoodSource);
    });
  }

  void onlookerBeesPhase () {

  }

  void scoutBeesPhase() {

  }

  @Override
  public void next() throws TerminationException {
    System.out.println("TEST next");
  }
}
