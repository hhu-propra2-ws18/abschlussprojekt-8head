package hhu.ausleihservice;

import hhu.ausleihservice.databasemodel.Rolle;
import org.junit.Assert;
import org.junit.Test;

public class RolleEnumTests {
	@Test
	public void enumStringAdmin() {
		Rolle rolle = Rolle.ADMIN;
		Assert.assertEquals("ADMIN", rolle.name());
	}

	@Test
	public void enumStringUser() {
		Rolle rolle = Rolle.USER;
		Assert.assertEquals("USER", rolle.name());
	}
}
