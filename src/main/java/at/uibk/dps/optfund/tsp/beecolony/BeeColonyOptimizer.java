package at.uibk.dps.optfund.tsp.beecolony;

import java.util.*;
import java.util.stream.*;

import com.google.inject.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.Pair;
import org.opt4j.core.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.config.annotations.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

/**
 * The {@link BeeColonyOptimizer} is an implementation of the Artificial Bee Colony Algorithm.
 */
@Info("Artificial Bee Colony Algorithm")
public
class BeeColonyOptimizer implements IterativeOptimizer {
  private final Population population;
  private final FoodSourceFactory foodSourceFactory;
  protected final IndividualCompleter completer;
  protected final Rand random;

  protected final int populationSize;
  protected final double alpha;
  protected final int limit;
  public Object next;

  protected List<Bee> employedBees = new ArrayList<>();
  protected List<Bee> onlookerBees = new ArrayList<>();

  private boolean initialized = false;

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

  /**
   * Initialize the optimizer by assigning one half of the {@link Bee}s as onlooker bees,
   * and the other half as employed bees with random {@link FoodSource}s.
   */
  @Override
  public void initialize() {
    this.onlookerBees = Stream.generate(() -> new Bee()).limit(this.populationSize).collect(Collectors.toList());
    this.employedBees = Stream.generate(() -> new Bee()).limit(this.populationSize).map(bee -> {
      var foodSource = foodSourceFactory.create();
      bee.setMemory(foodSource);
      this.population.add(foodSource);
      return bee;
    }).collect(Collectors.toList());
  }

  /**
   * Generate a new {@link FoodSource} starting from a given {@link FoodSource} and
   * a random {@link FoodSource} chosen from a list of {@link FoodSource}s.
   *
   * @param foodSource   the starting food source
   * @param foodSources  the list of all available food sources
   * @return             a new food source
   */
  FoodSource generateRandomFoodSource(FoodSource foodSource, List<FoodSource> foodSources) {
    while (true) {
      var i = this.random.nextInt(foodSources.size());
      var randomFoodSource = foodSources.get(i);

      if (foodSource != randomFoodSource) {
        return foodSourceFactory.create(foodSource.generateNeighbor(this.random, this.alpha));
      }
    }
  }

  /**
   * In the employed bees phase, all employed bees look for a new random {@link FoodSource}
   * in the neighborhood of the {@link FoodSource} they have memorized and greedily choose
   * between them by comparing their fitness.
   *
   * @throws TerminationException
   */
  void employedBeesPhase() throws TerminationException {
    var foodSources = this.population.stream().map(f -> (FoodSource) f).collect(Collectors.toList());

    for (var bee: this.employedBees) {
      findBetterFoodSource(bee, foodSources);
    }
  }

  /**
   * In the onlooker bees phase, all onlooker {@link Bee}s probabilistically choose a {@link FoodSource} communicated
   * to them by employed {@link Bee}s. The onlooker bees then look for a random {@link FoodSource} and greedily choose
   * between both by comparing their fitness.
   *
   * @throws TerminationException
   */
  void onlookerBeesPhase () throws TerminationException {
    var foodSources = this.employedBees.stream().map(bee -> bee.getMemory()).collect(Collectors.toList());

    var fitnesses = foodSources.stream().map(foodSource -> foodSource.fitness()).collect(Collectors.toList());
    var totalFitness = fitnesses.stream().mapToDouble(d -> d).sum();

    EnumeratedDistribution<Bee> distribution = new EnumeratedDistribution(IntStream.range(0, foodSources.size()).mapToObj(i -> {
      var probability = fitnesses.get(i) / totalFitness;
      return new Pair(this.employedBees.get(i), probability);
    }).collect(Collectors.toList()));

    for (var onlookerBee: this.onlookerBees) {
      var bee = distribution.sample();
      findBetterFoodSource(bee, foodSources);
    }
  }


  /**
   * Find a better {@link FoodSource} for a given employed {@link Bee} by looking in the neighborhood
   * of its memorized {@link FoodSource} and greedily choosing the {@link FoodSource} with the better
   * fitness. Additionally, if the new {@link FoodSource} is better, mark the old one for abandonment.
   *
   * @param bee          the employed bee
   * @param foodSources  the list of food sources to choose from
   * @throws TerminationException
   */
  private void findBetterFoodSource(Bee bee, List<FoodSource> foodSources) throws TerminationException {
    var foodSource = bee.getMemory();

    var newFoodSource = this.generateRandomFoodSource(foodSource, foodSources);
    this.completer.complete(newFoodSource);

    // If the new food source is better, memorize the new one and forget the old one.
    // If the old one is better, increase its abandonment counter.
    if (newFoodSource.getObjectives().dominates(foodSource.getObjectives())) {
      bee.setMemory(newFoodSource);
      this.population.remove(foodSource);
      this.population.add(newFoodSource);
    } else {
      foodSource.markForAbandonment();
    }
  }

  /**
   * In the scout bees phase, all {@link FoodSource}s are evaluated and abandoned if needed. Scout bees
   * then look for random new food sources to replace abandoned ones.
   */
  void scoutBeesPhase() throws TerminationException {
    for (var bee: this.employedBees) {
      var foodSource = bee.getMemory();

      // If the abandonment limit for a food source is reached,
      // remove the old food source from the population and
      // scout for a random new food source.
      if (foodSource.shouldBeAbandoned(this.limit)) {
        var newFoodSource = foodSourceFactory.create();
        this.completer.complete(newFoodSource);

        bee.setMemory(newFoodSource);
        this.population.remove(foodSource);
        this.population.add(newFoodSource);
      }
    }
  }

  /**
   * Compute one iteration of the algorithm.
   *
   * @throws TerminationException
   */
  @Override
  public void next() throws TerminationException {
    if (!this.initialized) {
      this.completer.complete(this.population);
      this.initialized = true;
    }

    this.employedBeesPhase();
    this.onlookerBeesPhase();
    this.scoutBeesPhase();
  }
}
