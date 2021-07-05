package at.uibk.dps.optfund.tsp.beecolony;

import java.util.Objects;

import org.opt4j.benchmarks.DoubleString;
import org.opt4j.core.Genotype;
import org.opt4j.core.Individual;
import org.opt4j.core.common.random.Rand;
import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.tutorial.salesman.SalesmanProblem;
import org.opt4j.tutorial.salesman.SalesmanProblem.City;

import com.google.inject.Inject;

public class FoodSource extends Individual {
	// The name of the `fitness` minimization objective.

	private int abandonmentCounter;

	/**
	 * Construct a new {@link FoodSource} with a given {@link Genotype}.
	 *
	 * @param permutationGenotype the food source's genotype.
	 */
	@Inject
	public FoodSource(PermutationGenotype<City> permutationGenotype) {
		super();
		this.setGenotype(permutationGenotype);
	}

	@Override
	public PermutationGenotype<SalesmanProblem.City> getGenotype() {
		return (PermutationGenotype<SalesmanProblem.City>) super.getGenotype();
	}

	/**
	 * Finds a neighboring route by swapping some of the cities in the genotype. The
	 * degree to which the swapping happens can be controlled with the alpha
	 * parameter.
	 * 
	 * @param random
	 * @param alpha  controls likelihood to swap
	 * @return a new {@link PermutationGenotype}
	 */
	public PermutationGenotype<SalesmanProblem.City> generateNeighbor(Rand random, double alpha) {
		PermutationGenotype<SalesmanProblem.City> newRoute = new PermutationGenotype<>();
		for (SalesmanProblem.City city : this.getGenotype()) {
			newRoute.add(city);
		}

		// get neighbors by swapping some cities in list
		for (City city : newRoute) {
			if (random.nextDouble() < alpha) {
				int randCityIdx = random.nextInt(newRoute.size());
				City tmpCity = newRoute.get(randCityIdx);
				newRoute.set(randCityIdx, city);
				newRoute.set(newRoute.indexOf(city), tmpCity);
			}
		}

		return newRoute;
	}

	/**
	 * Get the food source's fitness value.
	 *
	 * @return the fitness
	 */
	public double fitness() {
		return this.objectives.getValues().stream().mapToDouble(v -> v.getDouble()).sum();
	}

	/**
	 * Determine whether the food source should be abandoned, based on a given
	 * limit.
	 *
	 * @param limit the limit after which a food source should be abandoned
	 * @return whether or not the food source should be abandoned
	 */
	public boolean shouldBeAbandoned(int limit) {
		return this.abandonmentCounter > limit;
	}

	/**
	 * Mark the food source for abandonment, increasing its abandonment counter.
	 */
	public void markForAbandonment() {
		this.abandonmentCounter++;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(this.getGenotype());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		if (other == null) {
			return false;
		}

		if (this.getClass() != other.getClass()) {
			return false;
		}

		return this.getGenotype().equals(((FoodSource) other).getGenotype());
	}
}
