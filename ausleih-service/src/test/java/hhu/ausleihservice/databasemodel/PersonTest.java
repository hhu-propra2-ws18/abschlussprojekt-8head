package hhu.ausleihservice.databasemodel;

import org.junit.Assert;
import org.junit.Test;

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
}
