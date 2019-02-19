package hhu.ausleihservice.databasemodel;

import org.junit.Assert;
import org.junit.Test;

public class AbholortTest {

	@Test
	public void thisTestCanBeRemoved() {
		Abholort abholort = new Abholort();
		abholort.setBeschreibung("Hier");
		abholort.setId(1L);
		Assert.assertEquals("Hier", abholort.getBeschreibung());
		Assert.assertEquals(Long.valueOf(1L), abholort.getId());
	}
}
