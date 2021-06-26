package at.uibk.dps.optfund.dtlz.firefly;

import org.opt4j.core.IndividualFactory;
import org.opt4j.core.config.annotations.Citation;
import org.opt4j.core.config.annotations.Citation.PublicationMonth;
import org.opt4j.core.config.annotations.Info;
import org.opt4j.core.config.annotations.Order;
import org.opt4j.core.optimizer.MaxIterations;
import org.opt4j.core.optimizer.OptimizerModule;
import org.opt4j.core.start.Constant;

/**
 * The {@link FireflyAlgorithmModule} configures the {@link FireflyAlgorithm}.
 * 
 * @author Josef Gugglberger
 * 
 */

@Info("Firefly Algorithms for Multimodal Optimization. Works only with real-valued problems.")
@Citation(title = "Firefly Algorithms for Multimodal Optimization", authors = "Yang, Xin-She", journal = "International symposium on stochastic algorithms", year = 2009, month = PublicationMonth.UNKNOWN)

public class FireflyAlgorithmModule extends OptimizerModule {

	@Info("The number of generations.")
	@Order(0)
	@MaxIterations
	protected int generations = 1000;

	@Constant(value = "populationSize", namespace = FireflyAlgorithm.class)
	@Info("The size of the population.")
	@Order(1)
	protected int populationSize = 50;

	@Constant(value = "alpha", namespace = FireflyAlgorithm.class)
	@Info("A randomness parameter between 0 and 1.")
	@Order(2)
	protected double alpha;

	@Constant(value = "beta0", namespace = FireflyAlgorithm.class)
	@Info("The attractiveness parameter at distance 0.")
	@Order(3)
	protected double beta0;

	@Constant(value = "gamma", namespace = FireflyAlgorithm.class)
	@Info("The light absorption coefficient.")
	@Order(4)
	protected double gamma;

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

	public double getBeta0() {
		return beta0;
	}

	public void setBeta0(double beta0) {
		this.beta0 = beta0;
	}

	public double getGamma() {
		return gamma;
	}

	public void setGamma(double gamma) {
		this.gamma = gamma;
	}

	@Override
	public void config() {
		bindIterativeOptimizer(FireflyAlgorithm.class);
		bind(IndividualFactory.class).to(FireflyFactory.class);
	}
}
