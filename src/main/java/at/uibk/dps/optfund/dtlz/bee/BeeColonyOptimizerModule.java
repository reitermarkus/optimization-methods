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

	@Constant(value = "limitCounter", namespace = BeeColonyOptimizer.class)
	@Info("The abandonment limit counter.")
	@Order(2)
	protected int limitCounter;

	@Constant(value = "alpha", namespace = BeeColonyOptimizer.class)
	@Info("The lower bounds of the solution vector.")
	@Order(3)
	double alpha;

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

	public int getLimitCounter() {
		return limitCounter;
	}

	public void setLimitCounter(int limitCounter) {
		this.limitCounter = limitCounter;
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
		bind(IndividualFactory.class).to(BeeFactory.class);
	}
}
