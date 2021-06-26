package at.uibk.dps.optfund.dtlz.bee;

import org.opt4j.core.AbstractIndividualFactory;
import org.opt4j.core.Genotype;
import org.opt4j.core.problem.Creator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class BeeFactory extends AbstractIndividualFactory<Bee> {

	@Inject
	public BeeFactory(Provider<Bee> individualProvider, Creator<Genotype> creator) {
		super(individualProvider, creator);
	}

	@Override
	public Bee create() {
		return (Bee) super.create();
	}

	public Bee create(BEE_STATUS status) {
		Bee bee = create();
		bee.setBeeState(status);
		return bee;
	}

	@Override
	public Bee create(Genotype position) {
		return (Bee) super.create(position);
	}

}
