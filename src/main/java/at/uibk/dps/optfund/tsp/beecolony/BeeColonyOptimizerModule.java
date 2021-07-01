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

  @Constant(value = "n", namespace = BeeColonyOptimizer.class)
  @Info("The size of the solution vector.")
  @Order(3)
  protected int n;

  @Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class)
  @Info("The lower bounds of the solution vector.")
  @Order(4)
  double lowerBound;

  @Constant(value = "upperBound", namespace = BeeColonyOptimizer.class)
  @Info("The upper bounds of the solution vector.")
  @Order(5)
  double upperBound;

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

  public int getN() {
    return n;
  }

  public void setN(int n) {
    this.n = n;
  }

  public double getLowerBound() {
    return lowerBound;
  }

  public void setLowerBound(double lowerBound) {
    this.lowerBound = lowerBound;
  }

  public double getUpperBound() {
    return upperBound;
  }

  public void setUpperBound(double upperBound) {
    this.upperBound = upperBound;
  }

  @Override
  protected void config() {
    bindIterativeOptimizer(BeeColonyOptimizer.class);
    bind(IndividualFactory.class).to(FoodSourceFactory.class);
  }
}
