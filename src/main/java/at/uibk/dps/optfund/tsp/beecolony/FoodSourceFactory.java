package at.uibk.dps.optfund.tsp.beecolony;

import com.google.inject.*;
import org.opt4j.core.*;
import org.opt4j.core.problem.*;

/**
 * Factory class, used to construct {@link FoodSource}s.
 *
 * @author Markus Reiter
 * @author Michael Kaltschmid
 */
@Singleton
public class FoodSourceFactory extends AbstractIndividualFactory<FoodSource> {
  /**
   * Constructs an {@link FoodSourceFactory} with a {@link Provider} for
   * {@link FoodSource}s.
   *
   * @param individualProvider the provider that creates new food sources
   * @param creator            the creator
   */
  @Inject
  public FoodSourceFactory(Provider<FoodSource> individualProvider, Creator<Genotype> creator) {
    super(individualProvider, creator);
  }

  /**
   * Create a new {@link FoodSource}, initialized with a random {@link Genotype}.
   *
   * @return a food source
   */
  @Override
  public FoodSource create() {
    return (FoodSource) super.create();
  }

  /**
   * Create a new {@link FoodSource} with a given {@link Genotype}.
   *
   * @param genotype a genotype
   * @return a food source
   */
  @Override
  public FoodSource create(Genotype genotype) {
    return (FoodSource) super.create(genotype);
  }
}
