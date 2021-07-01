package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.benchmarks.*;
import org.opt4j.core.config.annotations.*;
import org.opt4j.core.problem.*;
import org.opt4j.core.start.*;

public class BeeColonyProblemModule extends ProblemModule {
  @Info("The size of the solution vector.")
  @Constant(value = "n")
  @N()
  protected int n = 10;

  public int getN() {
    return n;
  }

  public void setN(int n) {
    this.n = n;
  }

  @Override
  protected void config() {
    bindProblem(DoubleCreator.class, DoubleCopyDecoder.class, FoodSourceEvaluator.class);
  }
}
