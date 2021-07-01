package at.uibk.dps.optfund.tsp.beecolony;

import com.google.inject.*;
import org.opt4j.core.*;
import org.opt4j.core.problem.*;

@Singleton
public class FoodSourceFactory extends AbstractIndividualFactory<FoodSource> {
  /**
   * Constructs an {@link FoodSourceFactory} with a {@link Provider}
   * for {@link FoodSource}s.
   *
   * @param individualProvider the provider that creates new food sources
   * @param creator            the creator
   */
  @Inject
  public FoodSourceFactory(Provider<FoodSource> individualProvider, Creator<Genotype> creator) {
    super(individualProvider, creator);
  }

  @Override
  public FoodSource create() {
    return (FoodSource)super.create();
  }

  @Override
  public FoodSource create(Genotype genotype) {
    return (FoodSource)super.create(genotype);
  }
}
