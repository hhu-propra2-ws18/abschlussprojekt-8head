
package hhu.ausleihservice.databasemodel;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class ItemTests {
	@Test
	public void testGetPictureReturnsByteArrayWithSameLength() {
		Item item = new Item();
		byte[] picture = new byte[256];
		item.setPicture(picture);
		Assert.assertEquals(256, item.getPicture().length);
	}

	@Test
	public void testGetPictureReturnsCopyOfPicture() {
		Item item = new Item();
		byte[] picture = new byte[256];
		item.setPicture(picture);
		byte[] copy = item.getPicture();
		copy[255] = 42;
		Assert.assertEquals(0, item.getPicture()[255]);
	}

	@Test
	public void testGetPictureReturnsNullIfPictureIsNotSet() {
		Item item = new Item();
		Assert.assertNull(item.getPicture());
	}

	@Test
	public void testGetPictureReturnsEmptyByteArrayIfPictureIsEmpty() {
		Item item = new Item();
		item.setPicture(new byte[0]);
		Assert.assertEquals(0, item.getPicture().length);
	}

	@Test
	public void testSetPictureCopiesGivenByteArray() {
		Item item = new Item();
		byte[] original = new byte[256];
		item.setPicture(original);
		original[255] = 77;
		Assert.assertEquals(0, item.getPicture()[255]);
	}

	@Test
	public void testSetPictureNull() {
		Item item = new Item();
		item.setPicture(null);
		Assert.assertNull(item.getPicture());
	}

	@Test
	public void addAusleiheFahrrad() {
		Person burak = new Person();
		burak.setUsername("bumar100");
		Abholort ort = new Abholort();
		ort.setBeschreibung("Zuhause");
		Item fahrrad = new Item();
		fahrrad.setId(1L);
		fahrrad.setBeschreibung("Schnell");
		fahrrad.setTitel("Mountain-Bike");
		fahrrad.setAbholort(ort);

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setAusleiher(burak);
		fahrrad.addAusleihe(ausleihe);
		Assert.assertEquals(fahrrad, ausleihe.getItem());
		Assert.assertEquals("Mountain-Bike", ausleihe.getItem().getTitel());
		Assert.assertEquals("Schnell", ausleihe.getItem().getBeschreibung());
		Assert.assertEquals("Zuhause", fahrrad.getAbholort().getBeschreibung());

	}

	@Test
	public void addAusleiheNull() {
		Item fahrrad = new Item();
		Assert.assertEquals(0, fahrrad.getAusleihen().size());
		fahrrad.addAusleihe(null);
		Assert.assertEquals(0, fahrrad.getAusleihen().size());
	}

	@Test
	public void removeAusleihe() {
		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(1L);
		Item item = new Item();
		item.setId(2L);
		item.addAusleihe(ausleihe);
		item.removeAusleihe(ausleihe);
		Assert.assertEquals(0, item.getAusleihen().size());
		Assert.assertNull(ausleihe.getItem());
	}

	@Test
	public void getSortierteAusleihenWithNoAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(0, sortierteAusleihen.length);
	}

	@Test
	public void getSortierteAusleihenWithOneAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(0L);
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(1, sortierteAusleihen.length);
		assertEquals(ausleihe, sortierteAusleihen[0]);
	}

	@Test
	public void getSortierteAusleihenWithTwoSortedAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 1));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 1));
		item.addAusleihe(ausleihe2);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(2, sortierteAusleihen.length);
		assertEquals(ausleihe1, sortierteAusleihen[0]);
		assertEquals(ausleihe2, sortierteAusleihen[1]);
	}

	@Test
	public void getSortierteAusleihenWithTwoUnsortedAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 1));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 1));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe2.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe2);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(2, sortierteAusleihen.length);
		assertEquals(ausleihe2, sortierteAusleihen[0]);
		assertEquals(ausleihe1, sortierteAusleihen[1]);
	}

	@Test
	public void getSortierteAusleihenWithThreeSortedAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 1));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 1));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 1));
		ausleihe3.setEndDatum(LocalDate.of(2000, 9, 1));
		item.addAusleihe(ausleihe3);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(3, sortierteAusleihen.length);
		assertEquals(ausleihe1, sortierteAusleihen[0]);
		assertEquals(ausleihe2, sortierteAusleihen[1]);
		assertEquals(ausleihe3, sortierteAusleihen[2]);
	}

	@Test
	public void getSortierteAusleihenWithThreeUnsortedAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 1));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 1));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 7, 1));
		ausleihe2.setEndDatum(LocalDate.of(2000, 7, 1));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 1));
		ausleihe3.setEndDatum(LocalDate.of(2000, 9, 1));
		item.addAusleihe(ausleihe3);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(3, sortierteAusleihen.length);
		assertEquals(ausleihe2, sortierteAusleihen[0]);
		assertEquals(ausleihe1, sortierteAusleihen[1]);
		assertEquals(ausleihe3, sortierteAusleihen[2]);
	}

	@Test
	public void getSortierteAusleihenWithThreeLongUnsortedAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 4, 1));
		ausleihe1.setEndDatum(LocalDate.of(2000, 5, 1));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 2, 1));
		ausleihe2.setEndDatum(LocalDate.of(2000, 3, 1));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 1));
		ausleihe3.setEndDatum(LocalDate.of(2000, 10, 1));
		item.addAusleihe(ausleihe3);

		Ausleihe[] sortierteAusleihen = item.getSortierteAusleihen();

		assertEquals(3, sortierteAusleihen.length);
		assertEquals(ausleihe2, sortierteAusleihen[0]);
		assertEquals(ausleihe1, sortierteAusleihen[1]);
		assertEquals(ausleihe3, sortierteAusleihen[2]);
	}


	@Test
	public void getAvailablePeriodsWithNoAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(1, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2001-01-01", periods.get(0).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithOneSingleDayAusleihe() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(0L);
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 5));
		item.addAusleihe(ausleihe);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(2, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-06", periods.get(1).getStart().toString());
		assertEquals("2001-01-01", periods.get(1).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithOneMultipleDayAusleihe() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe = new Ausleihe();
		ausleihe.setId(0L);
		ausleihe.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe.setEndDatum(LocalDate.of(2000, 7, 10));
		item.addAusleihe(ausleihe);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(2, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-11", periods.get(1).getStart().toString());
		assertEquals("2001-01-01", periods.get(1).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithTwoSortedSingleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 5));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 14));
		item.addAusleihe(ausleihe2);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(3, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-06", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-15", periods.get(2).getStart().toString());
		assertEquals("2001-01-01", periods.get(2).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithTwoUnsortedSingleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 14));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe2.setEndDatum(LocalDate.of(2000, 7, 5));
		item.addAusleihe(ausleihe2);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(3, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-06", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-15", periods.get(2).getStart().toString());
		assertEquals("2001-01-01", periods.get(2).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithTwoSortedMultipleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 10));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 20));
		item.addAusleihe(ausleihe2);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(3, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-11", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-21", periods.get(2).getStart().toString());
		assertEquals("2001-01-01", periods.get(2).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithTwoUnsortedMultipleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 20));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe2.setEndDatum(LocalDate.of(2000, 7, 10));
		item.addAusleihe(ausleihe2);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(3, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-11", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-21", periods.get(2).getStart().toString());
		assertEquals("2001-01-01", periods.get(2).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithThreeSortedSingleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 5));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 14));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 17));
		ausleihe3.setEndDatum(LocalDate.of(2000, 9, 17));
		item.addAusleihe(ausleihe3);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(4, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-06", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-15", periods.get(2).getStart().toString());
		assertEquals("2000-09-16", periods.get(2).getEnd().toString());
		assertEquals("2000-09-18", periods.get(3).getStart().toString());
		assertEquals("2001-01-01", periods.get(3).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithThreeUnsortedSingleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 14));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe2.setEndDatum(LocalDate.of(2000, 7, 5));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 17));
		ausleihe3.setEndDatum(LocalDate.of(2000, 9, 17));
		item.addAusleihe(ausleihe3);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(4, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-06", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-15", periods.get(2).getStart().toString());
		assertEquals("2000-09-16", periods.get(2).getEnd().toString());
		assertEquals("2000-09-18", periods.get(3).getStart().toString());
		assertEquals("2001-01-01", periods.get(3).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithThreeSortedMultipleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe1.setEndDatum(LocalDate.of(2000, 7, 10));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe2.setEndDatum(LocalDate.of(2000, 8, 20));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 9, 17));
		ausleihe3.setEndDatum(LocalDate.of(2000, 9, 23));
		item.addAusleihe(ausleihe3);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(4, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-11", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-21", periods.get(2).getStart().toString());
		assertEquals("2000-09-16", periods.get(2).getEnd().toString());
		assertEquals("2000-09-24", periods.get(3).getStart().toString());
		assertEquals("2001-01-01", periods.get(3).getEnd().toString());
	}

	@Test
	public void getAvailablePeriodsWithThreeUnsortedMultipleDayAusleihen() {
		Item item = new Item();
		item.setAvailableFrom(LocalDate.of(2000, 1, 1));
		item.setAvailableTill(LocalDate.of(2001, 1, 1));

		Ausleihe ausleihe1 = new Ausleihe();
		ausleihe1.setId(0L);
		ausleihe1.setStartDatum(LocalDate.of(2000, 8, 14));
		ausleihe1.setEndDatum(LocalDate.of(2000, 8, 20));
		item.addAusleihe(ausleihe1);

		Ausleihe ausleihe2 = new Ausleihe();
		ausleihe2.setId(1L);
		ausleihe2.setStartDatum(LocalDate.of(2000, 9, 17));
		ausleihe2.setEndDatum(LocalDate.of(2000, 9, 23));
		item.addAusleihe(ausleihe2);

		Ausleihe ausleihe3 = new Ausleihe();
		ausleihe3.setId(2L);
		ausleihe3.setStartDatum(LocalDate.of(2000, 7, 5));
		ausleihe3.setEndDatum(LocalDate.of(2000, 7, 10));
		item.addAusleihe(ausleihe3);

		ArrayList<Period> periods = item.getAvailablePeriods();

		assertEquals(4, periods.size());
		assertEquals("2000-01-01", periods.get(0).getStart().toString());
		assertEquals("2000-07-04", periods.get(0).getEnd().toString());
		assertEquals("2000-07-11", periods.get(1).getStart().toString());
		assertEquals("2000-08-13", periods.get(1).getEnd().toString());
		assertEquals("2000-08-21", periods.get(2).getStart().toString());
		assertEquals("2000-09-16", periods.get(2).getEnd().toString());
		assertEquals("2000-09-24", periods.get(3).getStart().toString());
		assertEquals("2001-01-01", periods.get(3).getEnd().toString());
	}
}
