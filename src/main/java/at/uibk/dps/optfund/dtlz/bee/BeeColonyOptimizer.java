package at.uibk.dps.optfund.dtlz.bee;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.opt4j.core.Individual;
import org.opt4j.core.IndividualFactory;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.genotype.DoubleGenotype;
import org.opt4j.core.optimizer.IndividualCompleter;
import org.opt4j.core.optimizer.IterativeOptimizer;
import org.opt4j.core.optimizer.Population;
import org.opt4j.core.optimizer.TerminationException;
import org.opt4j.core.start.Constant;

import com.google.inject.Inject;

class BeeColonyOptimizer implements IterativeOptimizer {

	private final Population population;
	private final BeeFactory individualFactory;
	private final Rand random;
	private final IndividualCompleter completer;

	private final int populationSize;
	private final int limitCounter;
	private final double alpha;

	@Inject
	public BeeColonyOptimizer(Population population, IndividualFactory individualFactory, IndividualCompleter completer,
			Rand random, @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
			@Constant(value = "limitCounter", namespace = BeeColonyOptimizer.class) int limitCounter,
			@Constant(value = "alpha", namespace = BeeColonyOptimizer.class) double alpha) {
		this.population = (Population) population;
		this.individualFactory = (BeeFactory) individualFactory;
		this.completer = completer;
		this.random = random;

		this.populationSize = populationSize;
		this.limitCounter = limitCounter;
		this.alpha = alpha;
	}

	@Override
	public void initialize() throws TerminationException {
		// initialize half of the bees as employees, and the other half as onlookers
		BEE_STATUS state = BEE_STATUS.EMPLOYED;
		for (int i = 0; i < populationSize; i++) {
			if (i > populationSize / 2) {
				state = BEE_STATUS.ONLOOKER;
			}
			Bee bee = individualFactory.create(state);
			population.add(bee);
		}
		completer.complete(population);
	}

	@Override
	public void next() throws TerminationException {

		// employee and onlooker phase
		for (Individual ind : population) {
			Bee bee = (Bee) ind;
			Bee newBee;
			if (bee.isEmployee()) {
				// employees explore neighbourhood
				newBee = getNeighbourPosition(bee);

			} else {
				// onlookers follow a solution according to a prob.
				Bee toFollow = rouletteWheelSelection();
				newBee = getNeighbourPosition(toFollow);
			}

			completer.complete(newBee);

			// greedy selection of fittest
			if (newBee.getError() < bee.getError()) {
				bee.setGenotype(newBee.getGenotype());
			} else {
				bee.incrementCount();
			}
		}

		// update food sources
		completer.complete(population);

		// scout bee phase (generate new food sources)
		for (Individual ind : population) {
			Bee bee = (Bee) ind;
			if (bee.isEmployee()) {
				if (bee.getCount() > this.limitCounter) {
					Bee newBee = individualFactory.create();
					bee.setGenotype((DoubleGenotype) newBee.getGenotype());
					bee.setCount(0);
				}
			}
		}
	}

	/**
	 * Returns a randomized neighbouring position of the given bee. Parameter alpha
	 * controlls the size of the search space around the given bee.
	 * 
	 * @param bee
	 * @return
	 */
	private Bee getNeighbourPosition(Bee bee) {

		Bee newBee = individualFactory.create();
		DoubleGenotype newPosition = (DoubleGenotype) newBee.getGenotype();

		double phi = this.alpha * rand(-1d, 1d);

		DoubleGenotype position = (DoubleGenotype) bee.getGenotype();
		for (int i = 0; i < position.size(); i++) {
			double newPositionK = position.get(i) + phi * position.get(i);

			// deal with border constraint
			newPositionK = newPositionK < 0d ? 0d : newPositionK;
			newPositionK = newPositionK > 1d ? 1d : newPositionK;

			newPosition.set(i, newPositionK);
		}

		newBee.setBeeState(bee.getBeeState());
		return newBee;
	}

	/**
	 * Returns random double in interval (min,max].
	 * 
	 * @param min
	 * @param max
	 * @return
	 */
	private double rand(double min, double max) {
		return random.nextDouble() * (max - min + 1) + min;
	}

	/**
	 * Calculates average fitness of a population.
	 * 
	 * @return
	 */
	private double getMeanFitness() {
		double mean = 0.0;
		for (Individual ind : this.population) {
			Bee bee = (Bee) ind;
			mean += bee.getError();
		}
		return mean / this.populationSize;
	}

	/**
	 * Select bee to follow based on a roulette wheel selection. Fitter bees have a
	 * higher chance to get selected.
	 * 
	 * @return
	 */
	private Bee rouletteWheelSelection() {
		double mean = getMeanFitness();
		List<Bee> bees = population.stream().map(i -> (Bee) i).filter(Bee::isEmployee).collect(Collectors.toList());

		double sum = bees.stream().mapToDouble(b -> b.getFitness(mean)).sum();

		List<Double> wheel = new ArrayList<>();
		for (int i = 0; i < bees.size(); i++) {
			double relFitness = bees.get(i).getFitness(mean) / sum;
			if (wheel.size() != 0)
				wheel.add(relFitness + wheel.get(wheel.size() - 1));
			else
				wheel.add(relFitness);
		}

		double spin = random.nextDouble();
		int selectionIndex = 0;
		for (int i = 0; i < wheel.size() - 1; i++) {
			if (spin > wheel.get(i) && spin < wheel.get(i + 1)) {
				selectionIndex = i;
			}
		}
		return bees.get(selectionIndex);
	}
}
