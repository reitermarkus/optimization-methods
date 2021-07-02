package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.core.*;

import java.util.*;

public class Bee {
  private FoodSource memory;

  public Bee() {}

  public FoodSource getMemory() {
    return memory;
  }

  public void setMemory(FoodSource memory) {
    this.memory = memory;
  }
}
