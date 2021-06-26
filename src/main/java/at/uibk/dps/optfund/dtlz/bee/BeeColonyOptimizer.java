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

	protected final int populationSize;
	protected final int n;
	protected final double lowerBound;
	protected final double upperBound;

	@Inject
	public BeeColonyOptimizer(Population population, IndividualFactory individualFactory, IndividualCompleter completer,
			Rand random, @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class) int populationSize,
			@Constant(value = "n", namespace = BeeColonyOptimizer.class) int n,
			@Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class) double lowerBound,
			@Constant(value = "upperBound", namespace = BeeColonyOptimizer.class) double upperBound) {
		this.population = population;
		this.individualFactory = (BeeFactory) individualFactory;
		this.completer = completer;
		this.random = random;

		this.populationSize = populationSize;
		this.n = n;
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
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
					;
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
				if (bee.getCount() > n) {
					Bee newBee = individualFactory.create();
					bee.setGenotype(newBee.getGenotype());
					bee.setCount(0);
				}
			}
		}
	}

	private Bee getNeighbourPosition(Bee bee) {
		DoubleGenotype position = (DoubleGenotype) bee.getGenotype();
		int randK = random.nextInt(position.size());
		double randValK = rand(position.getLowerBound(randK), position.getUpperBound(randK));
		double phi = rand(-1d, 1d);
		double newValue = position.get(randK) + phi * (position.get(randK) - randValK);
		if (newValue < 0 || newValue > 1) {
			newValue = rand(0d, 1d);
		}

		Bee newBee = individualFactory.create(position);
		DoubleGenotype newPosition = (DoubleGenotype) newBee.getGenotype();
		newPosition.set(randK, newValue);
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
