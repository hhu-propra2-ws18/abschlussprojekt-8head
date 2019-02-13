package hhu.ausleihservice;

import org.junit.Assert;
import org.junit.Test;
import hhu.ausleihservice.databasemodel.Rolle;

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

	@Test
	public void enumStringGuest() {
		Rolle rolle = Rolle.GUEST;
		Assert.assertEquals("GUEST", rolle.name());
	}

	@Test
	public void enumClass() {
		Rolle rolle = Rolle.GUEST;
		Assert.assertEquals("Rolle", rolle.getClass().getSimpleName());
	}
}
