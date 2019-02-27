package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {

	@Autowired
	private ItemRepository items;

	public ItemService() {
	}

	public ItemService(ItemRepository items) {
		this.items = items;
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
}
