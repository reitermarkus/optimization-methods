package at.uibk.dps.optfund.dtlz.firefly;

import org.opt4j.core.Individual;
import org.opt4j.core.genotype.DoubleGenotype;

/**
 * The {@link Firefly} extends the {@link Individual} by an id and provides
 * useful functionalities such as the distance function and the intensity value.
 * 
 * @author Josef Gugglberger
 * 
 */
public class Firefly extends Individual {

	private int id;

	public Firefly() {
	}

	public Firefly(int id) {
		this.id = id;
	}

	/**
	 * Euclidean distance between two fireflies.
	 * 
	 * @param otherFly the fly to compare to
	 * @return distance between two fireflies
	 */
	public double distance(Firefly otherFly) {
		DoubleGenotype currentPosition = (DoubleGenotype) this.getGenotype();
		DoubleGenotype otherPosition = (DoubleGenotype) otherFly.getGenotype();

		double s = 0;
		for (int i = 0; i < currentPosition.size(); i++) {
			s += (currentPosition.get(i) - otherPosition.get(i)) * (currentPosition.get(i) - otherPosition.get(i));
		}

		return Math.sqrt(s);
	}

	/**
	 * Returns the intensity value. It is calculated as the inverse of the sum of
	 * the error.
	 * 
	 * @return intensity value
	 */
	public double getIntensity() {
		return 1 / (1 + getError());
	}

	public double getError() {
		return this.getObjectives().getValues().stream().mapToDouble(v -> v.getDouble()).sum();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Firefly: { id: " + this.id + ", intensity: " + this.getIntensity() + ", error: " + this.getError()
				+ " }";
	}

}
