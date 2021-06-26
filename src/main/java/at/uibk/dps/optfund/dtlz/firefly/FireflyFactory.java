package at.uibk.dps.optfund.dtlz.firefly;

import org.opt4j.core.AbstractIndividualFactory;
import org.opt4j.core.Genotype;
import org.opt4j.core.problem.Creator;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Factory class, used to construct {@link Firefly}'s.
 * 
 * @author Josef Gugglberger
 *
 */
@Singleton
public class FireflyFactory extends AbstractIndividualFactory<Firefly> {

	/**
	 * Constructs a {@link FireflyFactory}
	 * 
	 * @param particleProvider the provider for particles
	 * @param creator          the creator
	 */
	@Inject
	public FireflyFactory(Provider<Firefly> fireflyProvider, Creator<Genotype> creator) {
		super(fireflyProvider, creator);
	}

	@Override
	public Firefly create() {
		return (Firefly) super.create();
	}

	/**
	 * Creates a {@link Firefly} with a given ID.
	 * 
	 * @param id ID of the constructed Firefly
	 * @return the created Firefly
	 */
	public Firefly create(int id) {
		Firefly fly = (Firefly) create();
		fly.setId(id);
		return fly;
	}

}
