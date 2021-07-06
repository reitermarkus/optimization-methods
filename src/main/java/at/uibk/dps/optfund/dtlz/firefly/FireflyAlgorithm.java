package at.uibk.dps.optfund.dtlz.firefly;

import com.google.inject.*;
import org.opt4j.core.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.genotype.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

import java.util.*;
import java.util.stream.*;

/**
 * The {@link FireflyAlgorithm} is an implementation the algorithm given in the
 * paper: "Yang, Xin-She. "Firefly algorithms for multimodal optimization."
 * International symposium on stochastic algorithms. Springer, Berlin,
 * Heidelberg, 2009. ". Requires the problems to be based on the
 * {@link DoubleGenotype}.
 *
 * @author Josef Gugglberger
 */
public class FireflyAlgorithm implements IterativeOptimizer {

  private final FireflyFactory individualFactory;
  private final Population population;
  private final IndividualCompleter completer;
  private final Rand random;
  private final double alpha;
  private final double beta0;
  private final double gamma;
  private final int populationSize;

  /**
   * Constructs a {@link FireflyAlgorithm}
   *
   * @param population        the population of fireflies
   * @param individualFactory the factory to construct fireflies
   * @param completer         the completer
   * @param random            a random number generator
   * @param populationSize    the number of fireflies
   * @param alpha             parameter for random walk part of movement
   * @param beta0             attractiveness value at distance 0
   * @param gamma             absorption constant
   */
  @Inject
  public FireflyAlgorithm(Population population, IndividualFactory individualFactory, IndividualCompleter completer,
                          Rand random, @Constant(value = "populationSize", namespace = FireflyAlgorithm.class) int populationSize,
                          @Constant(value = "alpha", namespace = FireflyAlgorithm.class) double alpha,
                          @Constant(value = "beta0", namespace = FireflyAlgorithm.class) double beta0,
                          @Constant(value = "gamma", namespace = FireflyAlgorithm.class) double gamma) {
    this.population = population;
    this.individualFactory = (FireflyFactory) individualFactory;
    this.completer = completer;
    this.random = random;
    this.alpha = alpha;
    this.beta0 = beta0;
    this.gamma = gamma;
    this.populationSize = populationSize;
  }

  /**
   * Initialize population of fireflies before iterative process starts.
   */
  @Override
  public void initialize() {
    this.population.addAll(getInitPopulation());
  }

  /**
   * Defines one iteration of the Firefly algorithm.
   */
  @Override
  public void next() throws TerminationException {
    completer.complete(population);

    // population to list, such that we can sort fireflies
    List<Firefly> fireflies = population.stream().map(i -> (Firefly) i).collect(Collectors.toList());

    // sort fireflies by errors (lowest to highest)
    fireflies = sortFireflies(fireflies);

    // update position of fireflies
    updatePositions(fireflies);

  }

  /**
   * Update the position of each {@link Firefly}.
   *
   * @param list of {@link Firefly}'s, sorted ascending by their error
   * @throws TerminationException
   */
  private void updatePositions(List<Firefly> fireflies) throws TerminationException {
    for (Firefly current : fireflies) {
      for (Firefly other : fireflies) {

        if (current.getId() == other.getId()) {
          // don't compare to itself
          continue;
        }

        if (other.getIntensity() >= current.getIntensity()) {
          // move current into direction of firefly (other) with greater intensity
          DoubleGenotype newPosition = move(current, other);
          current.setGenotype(newPosition);

          // update error of current firefly
          completer.complete(current);
        }
      }
    }
  }

  /**
   * Sort {@link Firefly}'s by their errors, from lowest to highest.
   *
   * @param fireflies to sort
   * @return sorted list of fireflies
   */
  public List<Firefly> sortFireflies(List<Firefly> fireflies) {
    return fireflies.stream().sorted(new FireflyComparator()).collect(Collectors.toList());
  }

  /**
   * Creates the initial {@link Population}, filled with {@link Firefly}'s.
   *
   * @return List of {@link Firefly}'s
   */
  public List<Firefly> getInitPopulation() {
    List<Firefly> fireflies = new ArrayList<>();
    int id = 0;
    while (fireflies.size() < populationSize) {
      Firefly fly = individualFactory.create(id++);
      fireflies.add(fly);
    }
    return fireflies;
  }

  /**
   * Moves currentFly into direction of otherFly in every dimension of the problem
   * space and returns new position. New position depends on: current position,
   * attraction of otherFly, and a randomized term.
   *
   * @param currentFly firefly to move
   * @param otherFly   move currentFly into direction of otherFly
   * @return new position encoded in a Genotype
   */
  public DoubleGenotype move(Firefly currentFly, Firefly otherFly) {
    DoubleGenotype currentPosition = (DoubleGenotype) currentFly.getGenotype();
    DoubleGenotype otherPosition = (DoubleGenotype) otherFly.getGenotype();

    double distance = currentFly.distance(otherFly);

    // attraction between fireflies
    double attraction = beta0 * Math.pow(Math.E, -gamma * distance * distance);

    // move each dim. of position into direction of other (better) firefly
    for (int k = 0; k < currentPosition.size(); k++) {
      // distance in dimension k
      double distanceK = otherPosition.get(k) - currentPosition.get(k);

      // random walk term
      double scale = currentPosition.getUpperBound(k) - currentPosition.getLowerBound(k);
      double randWalk = alpha * (random.nextDouble() - 0.5) * scale;

      double newPositionK = currentPosition.get(k) + attraction * distanceK + randWalk;

      currentPosition.set(k, newPositionK);
    }

    return currentPosition;
  }

}
