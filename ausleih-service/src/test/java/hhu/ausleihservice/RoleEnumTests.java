package hhu.ausleihservice;

import org.junit.Assert;
import org.junit.Test;
import hhu.ausleihservice.databasemodel.Role;

public class RoleEnumTests {
	@Test
	public void enumStringAdmin() {
		Role role = Role.ADMIN;
		Assert.assertEquals("ADMIN", role.name());
	}

	@Test
	public void enumStringUser() {
		Role role = Role.USER;
		Assert.assertEquals("USER", role.name());
	}
}
