package hhu.ausleihservice.databasemodel;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;


public class PersonTest {

	@Test
	public void addAusleihe() {
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(1L);
		Person person = new Person();
		person.setUsername("bumar100");
		person.addAusleihe(ausleihe);
		Assert.assertEquals(person.getAusleihen().iterator().next(), ausleihe);
		Assert.assertEquals(person, ausleihe.getAusleiher());
	}

	@Test
	public void removeAusleihe() {
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(1L);
		Person person = new Person();
		person.setUsername("bumar100");

		person.addAusleihe(ausleihe);
		Assert.assertTrue(person.getAusleihen().contains(ausleihe));
		person.removeAusleihe(ausleihe);
		Assert.assertFalse(person.getAusleihen().contains(ausleihe));
	}


	@Test
	public void addItem() {
		Person person = new Person();
		person.setUsername("bumar100");
		Item item = new Item();
		item.setId(1L);
		person.addItem(item);
		Assert.assertEquals(person.getItems().iterator().next(), item);
		Assert.assertEquals(item.getBesitzer(), person);
		Assert.assertTrue(person.getItems().contains(item));
		Assert.assertEquals(1, person.getItems().size());
	}

	@Test
	public void removeItem() {
		Person person = new Person();
		person.setUsername("bumar100");
		Item item = new Item();
		item.setId(1L);
		person.addItem(item);

		person.removeItem(item);
		Assert.assertFalse(person.getItems().contains(item));
		Assert.assertEquals(0, person.getItems().size());
	}

	@Test
	public void thisTestCanBeRemoved() {
		Person person = new Person();
		Abholort ort = new Abholort();
		Set<Abholort> abholort = new HashSet<>();
		abholort.add(ort);
		ort.setId(55L);
		person.setId(1L);
		person.setNachname("Martin");
		person.setVorname("Burak");
		person.setPassword("123");    //Top secret
		person.setRolle(Rolle.ADMIN);
		person.setEmail("mail");
		person.setAbholorte(abholort);

		Assert.assertEquals(Long.valueOf(1L), person.getId());
		Assert.assertEquals("Burak", person.getVorname());
		Assert.assertEquals("Martin", person.getNachname());
		Assert.assertEquals("123", person.getPassword());
		Assert.assertEquals(Rolle.ADMIN, person.getRolle());
		Assert.assertEquals("mail", person.getEmail());
		Assert.assertEquals(ort, person.getAbholorte().iterator().next());
	}
}
