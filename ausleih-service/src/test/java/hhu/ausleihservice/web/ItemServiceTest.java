package hhu.ausleihservice.web;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.service.ItemAvailabilityService;
import hhu.ausleihservice.web.service.ItemService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ItemServiceTest {

	//This field is required for the tests to work but will cause spotBugs to detect dodgy code
	@Rule
	@SuppressFBWarnings("URF_UNREAD_PUBLIC_OR_PROTECTED_FIELD")
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Mock
	private ItemRepository itemRepository;
	private ItemService itemService = new ItemService(null, new ItemAvailabilityService());
	private List<Item> repository = new ArrayList<>();

	private boolean testItemEquality(AusleihItem base, AusleihItem toTest) {
		return base.getId().longValue() == toTest.getId().longValue() &&
				base.getTitel().equals(toTest.getTitel()) &&
				base.getBeschreibung().equals(toTest.getBeschreibung()) &&
				base.getKautionswert().equals(toTest.getKautionswert()) &&
				base.getTagessatz().equals(toTest.getTagessatz()) &&
				base.getAvailableFrom() == toTest.getAvailableFrom() &&
				base.getAvailableTill() == toTest.getAvailableTill();
	}

	@Before
	public void prepareTestData() {
		repository = new ArrayList<>();

		AusleihItem item1 = new AusleihItem();
		item1.setId(1L);
		item1.setTitel("Fahrrad");
		item1.setBeschreibung("Hammer Fahrrad mit Dynamo");
		item1.setKautionswert(200);
		item1.setTagessatz(15);
		item1.setAvailableFrom(LocalDate.of(0, 1, 1));
		item1.setAvailableTill(LocalDate.of(9999, 12, 31));

		AusleihItem item2 = new AusleihItem();
		item2.setId(2L);
		item2.setTitel("Würfelset");
		item2.setBeschreibung("Acht hellgürne Würfel. Sie leuchten im dunkeln");
		item2.setKautionswert(800);
		item2.setTagessatz(2);
		item2.setAvailableFrom(LocalDate.of(2010, 6, 1));
		item2.setAvailableTill(LocalDate.of(2010, 8, 1));

		AusleihItem item3 = new AusleihItem();
		item3.setId(3L);
		item3.setTitel("Rosa Fahrrad");
		item3.setBeschreibung("Rosafarbendes Fahrrad ohne Hinterrad");
		item3.setKautionswert(50);
		item3.setTagessatz(8);
		item3.setAvailableFrom(LocalDate.of(2005, 1, 1));
		item3.setAvailableTill(LocalDate.of(2015, 1, 1));

		AusleihItem item4 = new AusleihItem();
		item4.setId(4L);
		item4.setTitel("Hammer");
		item4.setBeschreibung("Ein großer Vorschlaghammer");
		item4.setKautionswert(65);
		item4.setTagessatz(20);
		item4.setAvailableFrom(LocalDate.of(1990, 1, 1));
		item4.setAvailableTill(LocalDate.of(2011, 1, 1));

		repository.add(item1);
		repository.add(item2);
		repository.add(item3);
		repository.add(item4);

		itemService = new ItemService(itemRepository, new ItemAvailabilityService());

		Mockito.when(itemRepository.findAll()).thenReturn(repository);
	}

	@Test
	public void testSimpleSearch_1_nullQuery() {
		List<Item> searchedList = itemService.simpleSearch(null);

		assertEquals(4, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 2L) {
				assertTrue(testItemEquality(repository.get(1), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testSimpleSearch_2_emptyQuery() {
		List<Item> searchedList = itemService.simpleSearch("");

		assertEquals(4, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 2L) {
				assertTrue(testItemEquality(repository.get(1), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testSimpleSearch_3_oneWordQuery() {

		List<Item> searchedList = itemService.simpleSearch("Fahrrad");

		assertEquals(2, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testSimpleSearch_4_twoWordQuery() {

		List<Item> searchedList = itemService.simpleSearch("Hammer Fahrrad");

		assertEquals(1, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else {
				fail();
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

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_1_nullQuery() {

		List<Item> searchedList = itemService.extendedSearch(null,
				2147483647,
				2147483647,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(4, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 2L) {
				assertTrue(testItemEquality(repository.get(1), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_2_emptyQuery() {

		List<Item> searchedList = itemService.extendedSearch("",
				2147483647,
				2147483647,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(4, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 2L) {
				assertTrue(testItemEquality(repository.get(1), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_3_limitedTagessatz() {

		List<Item> searchedList = itemService.extendedSearch(null,
				15,
				2147483647,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(3, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 2L) {
				assertTrue(testItemEquality(repository.get(1), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else {
				Assert.fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_4_limitedKautionswert() {

		List<Item> searchedList = itemService.extendedSearch(null,
				2147483647,
				100,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(2, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_5_queryFahrrad() {

		List<Item> searchedList = itemService.extendedSearch("Fahrrad",
				2147483647,
				2147483647,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(2, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_6_queryFahrradLimitedKautionswert() {

		List<Item> searchedList = itemService.extendedSearch("Fahrrad",
				2147483647,
				100,
				LocalDate.of(2010, 7, 1),
				LocalDate.of(2010, 7, 1));

		assertEquals(1, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_7_dateRange_1() {

		List<Item> searchedList = itemService.extendedSearch(null,
				2147483647,
				2147483647,
				LocalDate.of(2007, 1, 1),
				LocalDate.of(2012, 1, 1));

		assertEquals(2, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 3L) {
				assertTrue(testItemEquality(repository.get(2), searchedItem));
			} else {
				fail();
			}
		}
	}

	@Test
	public void testExtendedSearch_8_dateRange_2() {

		List<Item> searchedList = itemService.extendedSearch(null,
				2147483647,
				2147483647,
				LocalDate.of(2000, 1, 1),
				LocalDate.of(2003, 1, 1));

		assertEquals(2, searchedList.size());

		for (AusleihItem searchedItem : searchedList) {
			if (searchedItem.getId() == 1L) {
				assertTrue(testItemEquality(repository.get(0), searchedItem));
			} else if (searchedItem.getId() == 4L) {
				assertTrue(testItemEquality(repository.get(3), searchedItem));
			} else {
				fail();
			}
		}
	}
}
