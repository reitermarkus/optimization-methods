package at.uibk.dps.optfund.tsp.beecolony;

import java.util.*;
import java.util.stream.*;

import at.uibk.dps.optfund.dtlz.bee.*;
import com.google.inject.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.Pair;
import org.opt4j.core.*;
import org.opt4j.core.common.completer.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

class BeeColonyOptimizer implements IterativeOptimizer {
  private final Population population;
  private final FoodSourceFactory foodSourceFactory;
  protected final IndividualCompleter completer;
  protected final Rand random;

  protected final int populationSize;
  protected final double alpha;
  protected final int limit;

  protected List<Bee> employedBees = new ArrayList<>();
  protected List<Bee> onlookerBees = new ArrayList<>();

  @Inject
  public BeeColonyOptimizer(
    Population population,
    IndividualFactory individualFactory,
    IndividualCompleter completer,
    Rand random,
    @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
    @Constant(value = "alpha", namespace = BeeColonyOptimizer.class) double alpha,
    @Constant(value = "limit", namespace = BeeColonyOptimizer.class) int limit) {
    this.population = population;
    this.foodSourceFactory = (FoodSourceFactory)individualFactory;
    this.completer = completer;
    this.random = random;
    this.populationSize = populationSize;
    this.alpha = alpha;
    this.limit = limit;
  }

  private SequentialIndividualCompleter getCompleter() {
    return (SequentialIndividualCompleter)this.completer;
  }

  @Override
  public void initialize() throws TerminationException {
    this.onlookerBees = Stream.generate(() -> new Bee()).limit(this.populationSize / 2).collect(Collectors.toList());
    this.employedBees = Stream.generate(() -> new Bee()).limit(this.populationSize / 2).map(bee -> {
      var foodSource = foodSourceFactory.create();
      bee.setMemory(foodSource);
      this.population.add(foodSource);
      return bee;
    }).collect(Collectors.toList());
  }

  FoodSource generateRandomFoodSource(FoodSource foodSource, List<FoodSource> foodSources) {
    var i = this.random.nextInt(foodSources.size());
    var randomFoodSource = foodSources.get(i);

    return foodSource.generateNeighbor(randomFoodSource, random, this.alpha, this.foodSourceFactory);
  }

  void employedBeesPhase() {
    System.out.println("employedBeesPhase");

    var foodSources = this.population.stream().map(f -> (FoodSource) f).collect(Collectors.toList());

    System.out.println("Population: " + population.size());

    this.employedBees.forEach(bee -> {
      var foodSource = bee.getMemory();

      foodSources.remove(foodSource);
      var newFoodSource = this.generateRandomFoodSource(foodSource, foodSources);
      foodSources.add(foodSource);

      try {
        this.getCompleter().complete(foodSource);
        this.getCompleter().complete(newFoodSource);
      } catch (TerminationException e) {
        e.printStackTrace();
      }

      var newFoodSourceIsBetter = newFoodSource.getObjectives().dominates(foodSource.getObjectives());

      if (newFoodSourceIsBetter) {
        bee.setMemory(newFoodSource);
        System.out.println("removed: " + this.population.remove(foodSource));
        this.population.add(newFoodSource);
      } else {
        foodSource.isStillTheBestEver();
      }
    });

    System.out.println("Population: " + population.size());

  }

  void onlookerBeesPhase () {
    System.out.println("onlookerBeesPhase");

    System.out.println("Population: " + population.size());

    var foodSources = this.employedBees.stream().map(bee -> bee.getMemory()).collect(Collectors.toList());

    if (foodSources.isEmpty()) {
      return;
    }

    var fitnesses = foodSources.stream().map(foodSource -> foodSource.fitness()).collect(Collectors.toList());
    var totalFitness = fitnesses.stream().mapToDouble(d -> d).sum();

    EnumeratedDistribution<FoodSource> distribution = new EnumeratedDistribution(IntStream.range(0, foodSources.size()).mapToObj(i -> {
      var probability = fitnesses.get(i) / totalFitness;
      return new Pair(foodSources.get(i), probability);
    }).collect(Collectors.toList()));

    this.onlookerBees.stream().forEach(bee -> {
      var chosenFoodSource = distribution.sample();
      var newFoodSource = this.generateRandomFoodSource(chosenFoodSource, foodSources);

      try {
        this.getCompleter().complete(newFoodSource);
      } catch (TerminationException e) {
        e.printStackTrace();
      }

      var newFoodSourceIsBetter = newFoodSource.getObjectives().dominates(chosenFoodSource.getObjectives());

      if (newFoodSourceIsBetter) {
        this.population.remove(chosenFoodSource);
        this.population.add(newFoodSource);
      } else {
        chosenFoodSource.isStillTheBestEver();
      }
    });

    System.out.println("Population: " + population.size());

  }

  void scoutBeesPhase() {
    System.out.println("scoutBeesPhase");

    System.out.println("Population: " + population.size());

    this.employedBees = this.employedBees.stream().map(bee -> {
      var foodSource = bee.getMemory();

      // If the abandonment limit for a food source is reached,
      // remove the old food source from the population and
      // scout for a random new food source.
      if (foodSource.getLimit() > this.limit) {
        this.population.remove(foodSource);
        var newFoodSource = foodSourceFactory.create();
        bee.setMemory(newFoodSource);
        this.population.add(newFoodSource);
        return bee;
      }

      return bee;
    }).collect(Collectors.toList());

    System.out.println("Population: " + population.size());

  }

  @Override
  public void next() throws TerminationException {
    System.out.println("next");

    this.completer.complete(this.population);

    this.employedBeesPhase();
    this.onlookerBeesPhase();
    this.scoutBeesPhase();
  }
}
