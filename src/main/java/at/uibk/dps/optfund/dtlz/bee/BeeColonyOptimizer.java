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
		this.population = population;
		this.individualFactory = (BeeFactory) individualFactory;
		this.completer = completer;
		this.random = random;

		this.populationSize = populationSize;
		this.limitCounter = limitCounter;
		this.alpha = alpha;
	}

	@Override
	public void initialize() throws TerminationException {
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

		// employer bee phase (update food sources)
		for (Individual ind : population) {
			Bee employed = (Bee) ind;
			if (employed.getBeeState() == BEE_STATUS.EMPLOYED) {
				Bee newBee = getNeighbourPosition(employed);
				completer.complete(newBee);

				if (newBee.getError() < employed.getError()) {
					employed.setGenotype(newBee.getGenotype());
				} else {
					employed.incrementCount();
				}
			}
		}

		// update food sources
		completer.complete(population);
		double mean = getMeanFitness();

		// onlooker bee phase (exploit better solutions according to a prob.)
		for (Individual ind : population) {
			Bee onlooker = (Bee) ind;
			if (onlooker.getBeeState() == BEE_STATUS.ONLOOKER) {
				Bee toFollow = rouletteWheelSelection(mean);
				Bee newBee = getNeighbourPosition(toFollow);
				completer.complete(newBee);

				if (newBee.getError() < onlooker.getError()) {
					onlooker.setGenotype(newBee.getGenotype());
					;
				} else {
					onlooker.incrementCount();
				}

			}
		}

		// scout bee phase (generate new food sources)
		for (Individual ind : population) {
			Bee bee = (Bee) ind;
			if (bee.getBeeState() == BEE_STATUS.EMPLOYED) {
				if (bee.getCount() > this.limitCounter) {
					Bee newBee = individualFactory.create();
					bee.setGenotype((DoubleGenotype) newBee.getGenotype());
					bee.setCount(0);
				}
			}
		}
	}

	private Bee getNeighbourPosition(Bee bee) {
		DoubleGenotype position = (DoubleGenotype) bee.getGenotype();

		Bee newBee = individualFactory.create();
		DoubleGenotype newPosition = (DoubleGenotype) newBee.getGenotype();
		double phi = this.alpha * rand(-1d, 1d);
		for (int i = 0; i < position.size(); i++) {
			double newPositionK = position.get(i) + phi * position.get(i);
			newPositionK = newPositionK < 0d ? 0d : newPositionK;
			newPositionK = newPositionK > 1d ? 1d : newPositionK;
			newPosition.set(i, newPositionK);
		}

		return newBee;
	}

	private double rand(double min, double max) {
		return random.nextDouble() * (max - min + 1) + min;
	}

	private double getMeanFitness() {
		double mean = 0.0;
		for (Individual ind : this.population) {
			Bee bee = (Bee) ind;
			mean += bee.getError();
		}
		return mean / this.populationSize;
	}

	private Bee rouletteWheelSelection(double mean) {

		List<Bee> bees = population.stream().map(i -> (Bee) i).filter(b -> b.getBeeState() == BEE_STATUS.EMPLOYED)
				.collect(Collectors.toList());

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
