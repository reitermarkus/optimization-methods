package at.uibk.dps.optfund.dtlz;

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
	 * Returns -1 if f2 dominates f1, 1 if f1 dominates f2, and 0 otherwise.
	 * 
	 */
	@Override
	public int compare(Firefly f1, Firefly f2) {

		Objectives o1 = f1.getObjectives();
		Objectives o2 = f2.getObjectives();

		if (o2.weaklyDominates(o1)) {
			return -1;
		} else if (o1.weaklyDominates(o2)) {
			return 1;
		}
		return 0;
	}

}
