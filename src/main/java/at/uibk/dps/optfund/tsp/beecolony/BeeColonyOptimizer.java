package at.uibk.dps.optfund.tsp.beecolony;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

import org.opt4j.core.common.random.Rand;
import org.opt4j.core.optimizer.IndividualCompleter;
import org.opt4j.core.optimizer.IterativeOptimizer;
import org.opt4j.core.optimizer.Population;
import org.opt4j.core.optimizer.TerminationException;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

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
	protected final IndividualCompleter completer;

	@Inject
	public BeeColonyOptimizer(Population population, IndividualCompleter completer, Rand random,
			@Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
			@Constant(value = "alpha", namespace = BeeColonyOptimizer.class) double alpha,
			@Constant(value = "n", namespace = BeeColonyOptimizer.class) int n,
			@Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class) double lowerBound,
			@Constant(value = "upperBound", namespace = BeeColonyOptimizer.class) double upperBound) {
		this.population = population;
		this.completer = completer;
		this.random = random;
		this.populationSize = populationSize;
		this.alpha = alpha;
		this.n = n;
		this.lowerBounds = DoubleStream.generate(() -> lowerBound).limit(n).toArray();
		this.upperBounds = DoubleStream.generate(() -> upperBound).limit(n).toArray();
	}

	public static void main(String[] args) {
		// var optimizer = new BeeColonyOptimizer();

		// optimizer.optimize(13, 4, new double[] {0, 0, 0, 0}, new double[] {1, 1, 1,
		// 1});
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
				.limit(this.populationSize).collect(Collectors.toList());

		this.population.addAll(foodSources);

		var pop = this.population.stream().map(i -> (FoodSource) i).collect(Collectors.toList());
		System.out.println(pop.get(0).getObjectives());
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

			// var fitness = foodSource.fitness(BeeColonyOptimizer::objectiveFunction);
			// var newFitness =
			// newFoodSource.fitness(BeeColonyOptimizer::objectiveFunction);

			var newFoodSourceIsBetter = newFoodSource.getObjectives().dominates(foodSource.getObjectives());
			var selectedFoodSource = newFoodSourceIsBetter ? newFoodSource : foodSource;

			bee.setMemory(selectedFoodSource);
			foodSources.add(selectedFoodSource);
		});
	}

	void onlookerBeesPhase() {

	}

	void scoutBeesPhase() {

	}

	@Override
	public void next() throws TerminationException {
		this.completer.complete(this.population);

		var pop = this.population.stream().map(i -> (FoodSource) i).collect(Collectors.toList());
		System.out.println(pop.get(0).getObjectives());

		this.employedBeesPhase();
		this.onlookerBeesPhase();
		this.scoutBeesPhase();
	}
}
