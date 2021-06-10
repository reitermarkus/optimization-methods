package at.uibk.dps.optfund.tsp;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opt4j.core.common.random.Rand;
import org.opt4j.core.start.Constant;
import org.opt4j.tutorial.salesman.SalesmanProblem;

import com.google.inject.Inject;

/**
 * The {@link SalesPersonProblemSeed} creates the city map using an injected
 * {@link Rand} based on a configured seed for the generation of random numbers.
 * 
 * @author Fedor Smirnov
 */
public class SalesPersonProblemSeed extends SalesmanProblem {

	@Inject
	public SalesPersonProblemSeed(@Constant(value = "size") int size, Rand rand) {
		super(size);
		cities.clear();
		cities = Stream.generate(() -> {
			return new City(rand.nextDouble() * 100, rand.nextDouble() * 100);
		}).limit(size).collect(Collectors.toSet());
	}
}
