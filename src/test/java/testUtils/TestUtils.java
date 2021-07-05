package testUtils;

import org.opt4j.benchmarks.DoubleString;
import org.opt4j.core.Genotype;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.genotype.DoubleGenotype;
import org.opt4j.core.genotype.PermutationGenotype;
import org.opt4j.tutorial.salesman.SalesmanProblem;

import java.util.List;

/**
 * Abstract class that provides some mocks and helper methods that are needed in
 * multiple other test classes.
 * 
 * @author Josef Gugglberger
 *
 */
public class TestUtils {

	static Objective FITNESS = new Objective("fitness", Sign.MIN);

	public static double ATOL = 0.0001;

	/**
	 * Constructs a Genotype from double values.
	 * 
	 * @param values
	 * @return Genotype from given double valuess
	 */
	public static Genotype getGenotype(double... values) {
		DoubleGenotype type = new DoubleGenotype();
		for (double value : values) {
			type.add(value);
		}
		return type;
	}

	public static PermutationGenotype<SalesmanProblem.City> getPermutationGenotype(List<SalesmanProblem.City> cities) {
		PermutationGenotype<SalesmanProblem.City> type = new PermutationGenotype<SalesmanProblem.City>();
		for (SalesmanProblem.City city : cities) {
			type.add(city);
		}
		return type;
	}

	/**
	 * Constructs Objectives from integers.
	 * 
	 * @param objectives
	 * @return Objectives form given integer values
	 */
	public static Objectives getObjectives(int... objectives) {
		Objectives result = new Objectives();
		for (double obj : objectives) {
			result.add(FITNESS, obj);
		}
		return result;
	}

}
