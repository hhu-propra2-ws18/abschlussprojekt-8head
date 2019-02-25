package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.web.service.ItemAvailabilityService;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ItemAvailabilityServiceTest {

	private ItemAvailabilityService itemAvailabilityService = new ItemAvailabilityService();

	@Test
	public void isAvaibleInItemPeriod() {
		AusleihItem item = new AusleihItem();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertTrue(itemAvailabilityService.isAvailable(item, LocalDate.of(2000, 2, 2)));
	}

	@Test
	public void isNotAvaibleOutsideItemPeriod() {
		AusleihItem item = new AusleihItem();
		item.setAusleihen(new HashSet<>());
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(itemAvailabilityService.isAvailable(item, LocalDate.of(2222, 2, 2)));
	}

	@Test
	public void isNotAvaibleInItemPeriod() {
		AusleihItem item = new AusleihItem();
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 5));
		item.setAusleihen(Collections.singleton(ausleihe));
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));
		assertFalse(itemAvailabilityService.isAvailable(item, LocalDate.of(2000, 1, 5)));
	}

	@Test
	public void isAvailableFromTillPeriodInside() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertTrue(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnLeftEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		LocalDate from = LocalDate.of(2000, 1, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertTrue(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOnRightEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2001, 1, 1);

		assertTrue(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverLeftEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		LocalDate from = LocalDate.of(1999, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2001, 7, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillPeriodOverRightEdgeSmallPeriod() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2000, 1, 10));

		LocalDate from = LocalDate.of(2000, 1, 1);
		LocalDate till = LocalDate.of(2000, 2, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheNoConflict() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 1, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 1, 10));
		item.addAusleihe(ausleihe);

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertTrue(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictInside() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 10));
		item.addAusleihe(ausleihe);

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnLeftEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 6, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 6, 1));
		item.addAusleihe(ausleihe);

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}

	@Test
	public void isAvailableFromTillWithAusleiheWithConflictOnRightEdge() {
		AusleihItem item = new AusleihItem();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe);

		LocalDate from = LocalDate.of(2000, 6, 1);
		LocalDate till = LocalDate.of(2000, 7, 1);

		assertFalse(itemAvailabilityService.isAvailableFromTill(item, from, till));
	}
}
