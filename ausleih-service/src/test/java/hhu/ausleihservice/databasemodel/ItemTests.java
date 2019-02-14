package hhu.ausleihservice.databasemodel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemTests {
	@Test
	public void isAvaibleInItemPeriod() {
		Item item = new Item();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertTrue(item.isAvailable(LocalDate.of(2000, 2, 2)));
	}

	@Test
	public void isNotAvaibleOutsideItemPeriod() {
		Item item = new Item();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(item.isAvailable(LocalDate.of(2222, 2, 2)));
	}

	@Test
	public void isNotAvaibleInItemPeriod() {
		Item item = new Item();
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 5));
		item.setAusleihen(Collections.singleton(ausleihe));
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(item.isAvailable(LocalDate.of(2000, 1, 5)));
	}


	@Test
	public void isAvailableFromTillPeriodInside(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertTrue(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnLeftEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-01-01";
		String till = "2000-07-01";

		assertTrue(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnRightEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2001-01-01";

		assertTrue(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverLeftEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "1999-06-01";
		String till = "2000-07-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		String from = "2000-06-01";
		String till = "2001-07-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdgeSmallPeriod(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2000, 1, 10));

		String from = "2000-01-01";
		String till = "2000-02-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheNoConflict(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 10));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertTrue(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictInside(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 10));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnLeftEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 1));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnRightEdge(){
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe);

		String from = "2000-06-01";
		String till = "2000-07-01";

		assertFalse(item.isAvailableFromTill(from, till));
	}

}
