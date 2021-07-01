package at.uibk.dps.optfund.tsp.beecolony;

import org.opt4j.benchmarks.*;
import org.opt4j.core.*;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.genotype.*;
import org.opt4j.core.problem.*;

import java.util.function.*;

public class FoodSourceEvaluator implements Evaluator<DoubleString> {
  @Override
  public Objectives evaluate(DoubleString phenotype) {
    double f = phenotype.stream().mapToDouble(d -> d * d).sum();

    var objectives = new Objectives();
    objectives.add("fitness", Sign.MIN, this.fitness(f));
    return objectives;
  }

  private double fitness(double f) {
    if (f >= 0) {
      return 1.0 / (1 + f);
    } else {
      return 1.0 + Math.abs(f);
    }
  }
}
