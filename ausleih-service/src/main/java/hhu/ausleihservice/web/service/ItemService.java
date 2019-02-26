package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

	@Autowired
	private ItemRepository items;
	@Autowired
	private ItemAvailabilityService itemAvailabilityService;

	public ItemService() {
	}

	public ItemService(ItemRepository items, ItemAvailabilityService itemAvailabilityService) {
		this.items = items;
		this.itemAvailabilityService = itemAvailabilityService;
	}

	public Item findById(long id) {
		Optional<Item> item = items.findById(id);
		if (!item.isPresent()) {
			throw new ItemNichtVorhanden();
		}
		return item.get();

	}

	List<Item> findAll() {
		return items.findAll();
	}

	public boolean containsArray(String string, String[] array) {
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

	public void save(Item newItem) {
		items.save(newItem);
	}

	public void updateById(Long id, Item newItem) {
		Item toUpdate = this.findById(id);
		System.out.println("Starting item update");
		toUpdate.setTitel(newItem.getTitel());
		toUpdate.setBeschreibung(newItem.getBeschreibung());
		toUpdate.getAbholort().setBeschreibung(newItem.getAbholort().getBeschreibung());
		items.save(toUpdate);
	}
}
