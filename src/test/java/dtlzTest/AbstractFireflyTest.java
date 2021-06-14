package dtlzTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.opt4j.core.Genotype;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.genotype.DoubleGenotype;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyFactory;

/**
 * Abstract class that provides some mocks and helper methods that are needed in
 * multiple other test classes.
 * 
 * @author Josef Gugglberger
 *
 */
public abstract class AbstractFireflyTest {

	protected static final Objective firstObj = new Objective("first", Sign.MAX);

	protected static Firefly fly1 = spy(Firefly.class);
	protected static Firefly fly2 = spy(Firefly.class);
	protected static Firefly fly3 = spy(Firefly.class);

	protected static FireflyFactory factory = mock(FireflyFactory.class);

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
