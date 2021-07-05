package tspTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import org.junit.Test;
import org.opt4j.core.DoubleValue;
import org.opt4j.core.Objectives;

import at.uibk.dps.optfund.tsp.beecolony.FoodSourceEvaluator;
import testUtils.TestUtils;

public class FoodSourceEvaluatorTest {

	@Test
	public void foodSourceEvaluatorTest() {
		FoodSourceEvaluator eval = spy(FoodSourceEvaluator.class);

		Objectives obj = eval.evaluate(TestUtils.getDoubleString(0));
		DoubleValue val = (DoubleValue) obj.getValues().toArray()[0];

		assertEquals(val.getDouble(), 1d, TestUtils.ATOL);

		obj = eval.evaluate(TestUtils.getDoubleString(1));
		val = (DoubleValue) obj.getValues().toArray()[0];

		assertEquals(val.getDouble(), 0.5d, TestUtils.ATOL);

		obj = eval.evaluate(TestUtils.getDoubleString(-1));
		val = (DoubleValue) obj.getValues().toArray()[0];

		assertEquals(val.getDouble(), 0.5d, TestUtils.ATOL);
	}

}
