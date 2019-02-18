package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class ItemServiceTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	ItemRepository itemRepository;

	ItemService itemService = new ItemService(null);
	List<Item> repository = new ArrayList<>();

	@Before
	public void prepareTestData() {
		repository = new ArrayList<>();

		Item item_1 = new Item();
		item_1.setId(1L);
		item_1.setTitel("Fahrrad");
		item_1.setBeschreibung("Hammer Fahrrad mit Dynamo");

		Item item_2 = new Item();
		item_2.setId(2L);
		item_2.setTitel("Würfelset");
		item_2.setBeschreibung("Acht hellgürne Würfel. Sie leuchten im dunkeln");

		Item item_3 = new Item();
		item_3.setId(3L);
		item_3.setTitel("Rosa Fahrrad");
		item_3.setBeschreibung("Rosafarbendes Fahrrad ohne Hinterrad");

		Item item_4 = new Item();
		item_4.setId(4L);
		item_4.setTitel("Hammer");
		item_4.setBeschreibung("Ein großer Vorschlaghammer");

		repository.add(item_1);
		repository.add(item_2);
		repository.add(item_3);
		repository.add(item_4);

		itemService = new ItemService(itemRepository);

		Mockito.when(itemRepository.findAll()).thenReturn(repository);
	}

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

	@Test
	public void testSimpleSearch_1_nullQuery() {
		List<Item> searchedList = itemService.simpleSearch(null);

		assertEquals(4, searchedList.size());

		for (Item searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertEquals(repository.get(0).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(0).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 2L) {
				assertEquals(repository.get(1).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(1).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 3L) {
				assertEquals(repository.get(2).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(2).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 4L) {
				assertEquals(repository.get(3).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(3).getBeschreibung(), searchedItem.getBeschreibung());
			} else {
				assertEquals(true, false);
			}
		}
	}

	@Test
	public void testSimpleSearch_2_emptyQuery() {
		List<Item> searchedList = itemService.simpleSearch("");

		assertEquals(4, searchedList.size());

		for (Item searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertEquals(repository.get(0).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(0).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 2L) {
				assertEquals(repository.get(1).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(1).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 3L) {
				assertEquals(repository.get(2).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(2).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 4L) {
				assertEquals(repository.get(3).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(3).getBeschreibung(), searchedItem.getBeschreibung());
			} else {
				assertEquals(true, false);
			}
		}
	}

	@Test
	public void testSimpleSearch_3_oneWordQuery() {

		List<Item> searchedList = itemService.simpleSearch("Fahrrad");

		assertEquals(2, searchedList.size());

		for (Item searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertEquals(repository.get(0).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(0).getBeschreibung(), searchedItem.getBeschreibung());
			} else if (searchedItem.getId() == 3L) {
				assertEquals(repository.get(2).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(2).getBeschreibung(), searchedItem.getBeschreibung());
			} else {
				assertEquals(true, false);
			}
		}
	}

	@Test
	public void testSimpleSearch_4_twoWordQuery() {

		List<Item> searchedList = itemService.simpleSearch("Hammer Fahrrad");

		assertEquals(1, searchedList.size());

		for (Item searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertEquals(repository.get(0).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(0).getBeschreibung(), searchedItem.getBeschreibung());
			} else {
				assertEquals(true, false);
			}
		}
	}

	@Test
	public void testSimpleSearch_5_queryNotFound() {
		List<Item> searchedList = itemService.simpleSearch("Auto");

		assertEquals(0, searchedList.size());
	}

	@Test
	public void testSimpleSearch_6_twoWordQuerySpanningTitleAndDescription() {

		List<Item> searchedList = itemService.simpleSearch("großer Hammer");

		assertEquals(1, searchedList.size());

		for (Item searchedItem : searchedList) {
			if (searchedItem.getId() == 4L) {
				assertEquals(repository.get(3).getTitel(), searchedItem.getTitel());
				assertEquals(repository.get(3).getBeschreibung(), searchedItem.getBeschreibung());
			} else {
				assertEquals(true, false);
			}
		}
	}
}
