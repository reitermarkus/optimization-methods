package at.uibk.dps.optfund.dtlz.firefly;

import org.opt4j.core.*;

import java.util.*;

/**
 * Custom Comparator implementation, used for sorting.
 *
 * @author Josef Gugglberger
 */
public class FireflyComparator implements Comparator<Firefly> {

  /**
   * Compares the {@link Objectives} of {@link Firefly} f1 and {@link Firefly} f2
   * according to Arrays.compare().
   */
  @Override
  public int compare(Firefly f1, Firefly f2) {
    Objectives o1 = f1.getObjectives();
    Objectives o2 = f2.getObjectives();
    return Arrays.compare(o2.array(), o1.array());
  }

}
