package at.uibk.dps.optfund.tsp.beecolony;

/**
 * A {@link Bee} represents a bee, including a possible memorized {@link FoodSource}.
 *
 * @author Markus Reiter
 * @author Michael Kaltschmid
 */
public class Bee {
  private FoodSource memory;

  /**
   * Construct a new {@link Bee}.
   */
  public Bee() {
  }

  /**
   * Get a {@link Bee}'s memorized {@link FoodSource}.
   *
   * @return the memorized food source
   */
  public FoodSource getMemory() {
    return memory;
  }

  /**
   * Set a {@link Bee}'s memorized {@link FoodSource}.
   *
   * @param foodSource the memorized food source
   */
  public void setMemory(FoodSource foodSource) {
    this.memory = foodSource;
  }
}
