package at.uibk.dps.optfund.dtlz.firefly;

import com.google.inject.*;
import org.opt4j.core.*;
import org.opt4j.core.problem.*;

/**
 * Factory class, used to construct {@link Firefly}'s.
 *
 * @author Josef Gugglberger
 */
@Singleton
public class FireflyFactory extends AbstractIndividualFactory<Firefly> {

  /**
   * Constructs a {@link FireflyFactory}.
   *
   * @param particleProvider the provider for particles
   * @param creator          the creator
   */
  @Inject
  public FireflyFactory(Provider<Firefly> fireflyProvider, Creator<Genotype> creator) {
    super(fireflyProvider, creator);
  }

  /**
   * Constructs a {@link FireflyFactory}.
   */
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
    Firefly fly = create();
    fly.setId(id);
    return fly;
  }

}
