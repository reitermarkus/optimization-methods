package tspTest;

import at.uibk.dps.optfund.tsp.beecolony.*;
import com.google.inject.*;
import org.junit.*;
import static org.junit.Assert.*;

import org.opt4j.core.*;
import org.opt4j.core.common.completer.*;
import org.opt4j.core.common.random.*;
import org.opt4j.core.optimizer.*;
import org.opt4j.tutorial.salesman.*;

public class BeeColonyOptimizerTest {
  @Test
  public void testNext() throws TerminationException {
    var generations = 1000;
    var populationSize = 100;
    var alpha = 1.0;
    var limit = 3;

    var optimizerModule = new BeeColonyOptimizerModule();
    optimizerModule.setGenerations(generations);
    optimizerModule.setPopulationSize(populationSize);
    optimizerModule.setAlpha(alpha);
    optimizerModule.setLimit(limit);

    var salesmanModule = new SalesmanModule();

    var injector = Guice.createInjector(optimizerModule, salesmanModule);

    // var optimizer = injector.getInstance(BeeColonyOptimizer.class);

    var population = new Population();
    var factory = injector.getInstance(IndividualFactory.class);
    var completer = injector.getInstance(SequentialIndividualCompleter.class);
    var random = new RandomDefault();

    var optimizer = new BeeColonyOptimizer(
      population,
      factory,
      completer,
      random,
      populationSize,
      alpha,
      limit
    );

    assertEquals(population.size(), 0);
    optimizer.initialize();
    assertEquals(population.size(), populationSize);

    for (int i = 0; i < generations; i++) {
      optimizer.next();
      assertEquals(population.size(), populationSize);
    }
  }
}
