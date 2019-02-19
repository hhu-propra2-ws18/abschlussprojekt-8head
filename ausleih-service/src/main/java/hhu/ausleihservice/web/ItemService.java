package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

	private ItemRepository items;

	public ItemService(ItemRepository itemRep) {
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

	public boolean isAvailable(Item item, LocalDate date) {
		return isInPeriod(date, item.getAvailableFrom(), item.getAvailableTill())
				&& item.getAusleihen().stream().noneMatch((ausleihe)
				-> isInPeriod(date, ausleihe.getStartDatum(), ausleihe.getEndDatum()));
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

}
