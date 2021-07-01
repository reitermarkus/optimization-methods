package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.core.*;
import org.opt4j.core.config.annotations.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.core.start.*;

public class BeeColonyOptimizerModule extends OptimizerModule {
  @Info("The number of generations.")
  @Order(0)
  @MaxIterations
  protected int generations = 1000;

  @Constant(value = "populationSize", namespace = BeeColonyOptimizer.class)
  @Info("The size of the population.")
  @Order(1)
  protected int populationSize;

  @Constant(value = "alpha", namespace = BeeColonyOptimizer.class)
  @Info("A randomness parameter between 0 and 1.")
  @Order(2)
  protected double alpha;

  public int getGenerations() {
    return generations;
  }

  public void setGenerations(int generations) {
    this.generations = generations;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public void setPopulationSize(int populationSize) {
    this.populationSize = populationSize;
  }

  public double getAlpha() {
    return alpha;
  }

  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }

  @Override
  protected void config() {
    bindIterativeOptimizer(BeeColonyOptimizer.class);
    bind(IndividualFactory.class).to(FoodSourceFactory.class);
  }
}
