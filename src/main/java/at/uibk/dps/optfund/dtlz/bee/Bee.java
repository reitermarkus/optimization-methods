package at.uibk.dps.optfund.dtlz.bee;

import org.opt4j.core.Individual;

enum BEE_STATUS {
	EMPLOYED, ONLOOKER, SCOUT
}

public class Bee extends Individual {

	private BEE_STATUS beeState;

	private int count;

	public double getError() {
		return this.getObjectives().getValues().stream().mapToDouble(v -> v.getDouble()).sum();
	}

	public double getFitness(double meanFitness) {
		return 1 / (1 + getError());
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
