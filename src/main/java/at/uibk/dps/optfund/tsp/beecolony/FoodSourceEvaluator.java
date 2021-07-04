package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.benchmarks.DoubleString;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class FoodSourceEvaluator implements Evaluator<DoubleString> {
  // Evaluate a food source's fitness objective.
  @Override
  public Objectives evaluate(DoubleString phenotype) {
    // Objective function: Sum of squares.
    double f = phenotype.stream().mapToDouble(d -> d * d).sum();

    var objectives = new Objectives();
    objectives.add(FoodSource.FITNESS, this.fitness(f));
    return objectives;
  }

  // Calculate the fitness for a given objective function value `f`.
  private double fitness(double f) {
    if (f >= 0) {
      return 1.0 / (1 + f);
    } else {
      return 1.0 + Math.abs(f);
    }
  }
}
