package at.uibk.dps.optfund.tsp.beecolony;

import java.util.*;
import java.util.stream.*;

import com.google.inject.*;
import org.apache.commons.math3.distribution.*;
import org.apache.commons.math3.util.Pair;
import org.opt4j.core.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

class BeeColonyOptimizer implements IterativeOptimizer {
  private final Population population;
  private final FoodSourceFactory foodSourceFactory;
  protected final Rand random;

  protected final int populationSize;
  protected final double alpha;

  protected final List<EmployedBee> employedBees = new ArrayList<>();
  protected final List<OnlookerBee> onlookerBees = new ArrayList<>();
  protected final List<ScoutBee> scoutBees = new ArrayList<>();
  protected final IndividualCompleter completer;

  @Inject
  public BeeColonyOptimizer(
    Population population,
    IndividualFactory individualFactory,
    IndividualCompleter completer,
    Rand random,
    @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
    @Constant(value = "alpha", namespace = BeeColonyOptimizer.class) double alpha
  ) {
    this.population = population;
    this.foodSourceFactory = (FoodSourceFactory)individualFactory;
    this.completer = completer;
    this.random = random;
    this.populationSize = populationSize;
    this.alpha = alpha;
  }

  @Override
  public void initialize() throws TerminationException {
    Stream.generate(() -> foodSourceFactory.create()).limit(this.populationSize).forEach(foodSource -> {
      this.population.add(foodSource);
    });

    /*

    this.completer.complete(this.population);

    var pop = this.population.stream().map(i -> (FoodSource)i).collect(Collectors.toList());
    var foodSource = pop.get(0);
    System.out.println("genotype: " + foodSource.getGenotype());
    System.out.println("phenotype: " + foodSource.getPhenotype());
    System.out.println("objectives: " + foodSource.getObjectives());

    */
  }

  FoodSource generateRandomFoodSource(FoodSource foodSource, List<FoodSource> foodSources) {
    var i = this.random.nextInt(foodSources.size());
    var randomFoodSource = foodSources.get(i);

    return foodSource.generateNeighbor(randomFoodSource, random, this.alpha, this.foodSourceFactory);
  }

  void employedBeesPhase() {
    var foodSources = this.population.stream().map(f -> (FoodSource) f).collect(Collectors.toList());

    this.employedBees.forEach(bee -> {
      var foodSource = bee.getMemory();
      foodSources.remove(foodSource);

      var newFoodSource = this.generateRandomFoodSource(foodSource, foodSources);

      var newFoodSourceIsBetter = newFoodSource.getObjectives().dominates(foodSource.getObjectives());
      var selectedFoodSource = newFoodSourceIsBetter ? newFoodSource : foodSource;

      bee.setMemory(selectedFoodSource);
      foodSources.add(selectedFoodSource);
    });

    this.population.clear();
    this.population.addAll(foodSources);
  }

  void onlookerBeesPhase () {
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

    var newEmployedBees = this.onlookerBees.stream().map(bee -> {
      var chosenFoodSource = distribution.sample();
      var newFoodSource = this.generateRandomFoodSource(chosenFoodSource, foodSources);

      var newFoodSourceIsBetter = newFoodSource.getObjectives().dominates(chosenFoodSource.getObjectives());

      var newBee = new EmployedBee();

      if (newFoodSourceIsBetter) {
        this.population.add(newFoodSource);
        newBee.setMemory(newFoodSource);
      } else {
        newBee.setMemory(chosenFoodSource);
      }

      return newBee;
    }).collect(Collectors.toList());

    this.employedBees.addAll(newEmployedBees);
    this.onlookerBees.clear();
  }

  void scoutBeesPhase() {

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
