package at.uibk.dps.optfund.tsp.beecolony;

public class Bee {
  private FoodSource memory;

  /**
   * Construct a new {@link Bee}.
   */
  public Bee() {}

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
