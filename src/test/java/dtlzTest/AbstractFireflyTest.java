package dtlzTest;

import org.opt4j.core.Genotype;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.genotype.DoubleGenotype;

/**
 * Abstract class that provides some mocks and helper methods that are needed in
 * multiple other test classes.
 * 
 * @author Josef Gugglberger
 *
 */
public abstract class AbstractFireflyTest {

	protected static final Objective firstObj = new Objective("first", Sign.MAX);

	/**
	 * Constructs a Genotype from double values.
	 * 
	 * @param values
	 * @return Genotype from given double valuess
	 */
	protected static Genotype getGenotype(double... values) {
		DoubleGenotype type = new DoubleGenotype();
		for (double value : values) {
			type.add(value);
		}
		return type;
	}

	/**
	 * Constructs Objectives from integers.
	 * 
	 * @param objectives
	 * @return Objectives form given integer values
	 */
	protected static Objectives getObjectives(int... objectives) {
		Objectives result = new Objectives();
		for (double obj : objectives) {
			result.add(firstObj, obj);
		}
		return result;
	}

}
