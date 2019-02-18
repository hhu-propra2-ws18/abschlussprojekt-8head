package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

	private ItemRepository items;

	ItemService(ItemRepository itemRep) {
		this.items = itemRep;
	}

	Item findByID(long id) {
		Optional<Item> item = items.findById(id);
		if (!item.isPresent()) {
			throw new ItemNichtVorhanden();
		}
		return item.get();

	}

	List<Item> findAll() {
		return items.findAll();
	}

	private boolean isInPeriod(LocalDate date, LocalDate start, LocalDate end) {
		return (!date.isBefore(start) && !date.isAfter(end));
	}

	public boolean isAvailable(Item item) {
		return isAvailable(item, LocalDate.now());
	}

	boolean isAvailable(Item item, LocalDate date) {
		LocalDate availableFrom = item.getAvailableFrom();
		LocalDate availableTill = item.getAvailableTill();
		Set<Ausleihe> ausleihen = item.getAusleihen();

		if (!isInPeriod(date, availableFrom, availableTill)) {
			return false;
		}
		for (Ausleihe ausleihe : ausleihen) {
			LocalDate startDatum = ausleihe.getStartDatum();
			LocalDate endDatum = ausleihe.getEndDatum();
			if (isInPeriod(date, startDatum, endDatum)) {
				return false;
			}
		}
		return true;
	}

	//Format of input is "YYYY-MM-DD"
	public boolean isAvailableFromTill(Item item, String from, String till) {
		LocalDate temp = LocalDate.parse(from);
		LocalDate end = LocalDate.parse(till);
		while (!temp.equals(end.plusDays(1))) {
			if (!isAvailable(item, temp)) {
				return false;
			}
			temp = temp.plusDays(1);
		}
		return true;
	}

	private boolean containsArray(String string, String[] array) {
		for (String entry : array) {
			if (!string.contains(entry)) {
				return false;
			}
		}
		return true;
	}

	public List<Item> simpleSearch(String query) {
		List<Item> list;

		if (query == null || query.isEmpty()) {
			list = findAll();
		} else {
			//Ignores case
			String[] qArray = query.toLowerCase().split(" ");
			list = findAll()
					.stream()
					.filter(
							item -> containsArray(
									(item.getTitel()
											+ item.getBeschreibung())
											.toLowerCase(),
									qArray
							)
					)
					.collect(Collectors.toList());
		}

		return list;
	}

	public List<Item> extendedSearch(String query,
									 int tagessatzMax,
									 int kautionswertMax,
									 String availableMin,
									 String availableMax) {
		Stream<Item> listStream = findAll().stream();

		if (query != null && !query.equals("")) {
			//Ignores Case
			String[] qArray = query.toLowerCase().split(" ");
			listStream = listStream.filter(
					item -> containsArray(
							(item.getTitel() + item.getBeschreibung()).toLowerCase(),
							qArray));
		}

		listStream = listStream.filter(item -> item.getTagessatz() <= tagessatzMax);
		listStream = listStream.filter(item -> item.getKautionswert() <= kautionswertMax);

		listStream = listStream.filter(
				item -> isAvailableFromTill(item, availableMin, availableMax)
		);

		List<Item> list = listStream.collect(Collectors.toList());

		return list;
	}

}
