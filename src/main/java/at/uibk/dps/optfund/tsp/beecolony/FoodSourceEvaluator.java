package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.benchmarks.DoubleString;
import org.opt4j.core.*;
import org.opt4j.core.problem.Evaluator;

/**
 * The {@link FoodSourceEvaluator} evaluates a {@link FoodSource}s
 * fitness objective.
 */
public class FoodSourceEvaluator implements Evaluator<DoubleString> {
  /**
   * Evaluate a {@link FoodSource}'s {@link Objectives}.
   *
   * @param phenotype the food source's phenotype
   * @return the food source's objectives
   */
  @Override
  public Objectives evaluate(DoubleString phenotype) {
    // Objective function: Sum of squares.
    double f = phenotype.stream().mapToDouble(d -> d * d).sum();

    var objectives = new Objectives();
    objectives.add(FoodSource.FITNESS, this.fitness(f));
    return objectives;
  }

  /**
   * Calculate the fitness for a given objective function value `f`.
   *
   * @param f the objective function value
   * @return the fitness
   */
  private double fitness(double f) {
    if (f >= 0) {
      return 1.0 / (1 + f);
    } else {
      return 1.0 + Math.abs(f);
    }
  }
}
