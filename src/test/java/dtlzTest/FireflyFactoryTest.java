package dtlzTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Ignore;
import org.junit.Test;

import at.uibk.dps.optfund.dtlz.Firefly;
import at.uibk.dps.optfund.dtlz.FireflyFactory;

public class FireflyFactoryTest {

	private static FireflyFactory factory = spy(new FireflyFactory(null, null));

	private static Firefly fly = spy(Firefly.class);

	@Test
	@Ignore
	public void createTest() {
		when(factory.create()).thenReturn(fly);
		when(fly.getId()).thenReturn(1);

		assertEquals(1, factory.create(1).getId());
	}

}
