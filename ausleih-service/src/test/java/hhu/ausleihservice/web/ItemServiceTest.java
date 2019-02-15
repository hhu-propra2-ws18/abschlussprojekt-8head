package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemServiceTest {

	@Test
	public void isAvaibleInItemPeriod() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertTrue(itemService.isAvailable(item, LocalDate.of(2000, 2, 2)));
	}

	@Test
	public void isNotAvaibleOutsideItemPeriod() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(itemService.isAvailable(item, LocalDate.of(2222, 2, 2)));
	}

	@Test
	public void isNotAvaibleInItemPeriod() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 5));
		item.setAusleihen(Collections.singleton(ausleihe));
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(itemService.isAvailable(item, LocalDate.of(2000, 1, 5)));
	}


	@Test
	public void isAvailableFromTillPeriodInside() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertTrue(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnLeftEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-01-01";
		String till = "2000-07-01";

		assertTrue(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnRightEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2001-01-01";

		assertTrue(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverLeftEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "1999-06-01";
		String till = "2000-07-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2001-07-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdgeSmallPeriod() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2000, 1, 10));

		String from = "2000-01-01";
		String till = "2000-02-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheNoConflict() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 10));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertTrue(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictInside() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 10));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnLeftEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 1));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnRightEdge() {
		ItemService itemService = new ItemService(null);

		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(itemService.isAvailableFromTill(item, from, till));
	}
}
