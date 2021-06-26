package at.uibk.dps.optfund.dtlz.bee;

import org.opt4j.core.IndividualFactory;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;
import org.opt4j.core.start.Constant;

public class BeeColonyOptimizerModule extends OptimizerModule {
	@Info("The number of generations.")
	@Order(0)
	@MaxIterations
	protected int generations = 1000;

	@Constant(value = "populationSize", namespace = BeeColonyOptimizer.class)
	@Info("The size of the population.")
	@Order(1)
	protected int populationSize;

	@Constant(value = "n", namespace = BeeColonyOptimizer.class)
	@Info("The size of the solution vector.")
	@Order(2)
	protected int n;

	@Constant(value = "lowerBound", namespace = BeeColonyOptimizer.class)
	@Info("The lower bounds of the solution vector.")
	@Order(3)
	double lowerBound;

	@Constant(value = "upperBound", namespace = BeeColonyOptimizer.class)
	@Info("The upper bounds of the solution vector.")
	@Order(4)
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
		bind(IndividualFactory.class).to(BeeFactory.class);
	}
}
