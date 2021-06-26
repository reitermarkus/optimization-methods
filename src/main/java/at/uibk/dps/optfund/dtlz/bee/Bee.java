package at.uibk.dps.optfund.dtlz.bee;

import org.opt4j.core.Individual;

enum BEE_STATUS {
	EMPLOYED, ONLOOKER, SCOUT
}

public class Bee extends Individual {

	private BEE_STATUS beeState;

	private int count;

	public double getFitness(double meanFitness) {
		double errorSum = this.getObjectives().getValues().stream().mapToDouble(v -> v.getDouble()).sum();
		double beta = 0.1;
		return Math.pow(Math.E, -beta * errorSum / meanFitness);
	}

	public double getError() {
		return this.getObjectives().getValues().stream().mapToDouble(v -> v.getDouble()).sum();
	}

	public BEE_STATUS getBeeState() {
		return beeState;
	}

	public void setBeeState(BEE_STATUS state) {
		this.beeState = state;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public void incrementCount() {
		++this.count;
	}

}
