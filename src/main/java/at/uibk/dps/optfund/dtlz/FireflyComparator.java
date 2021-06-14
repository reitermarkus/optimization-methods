package at.uibk.dps.optfund.dtlz;

import java.util.Arrays;
import java.util.Comparator;

import org.opt4j.core.Objectives;

/**
 * Custom Comparator implementation, used for sorting the fireflies.
 * 
 * @author Josef Gugglberger
 *
 */
public class FireflyComparator implements Comparator<Firefly> {

	/**
	 * Compares the objectives of f1 and f1 according to Arrays.compare().
	 * 
	 */
	@Override
	public int compare(Firefly f1, Firefly f2) {
		Objectives o1 = f1.getObjectives();
		Objectives o2 = f2.getObjectives();
		return Arrays.compare(o2.array(), o1.array());
	}

}
