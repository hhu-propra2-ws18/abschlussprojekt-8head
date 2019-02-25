package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleihItemRepository;
import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

	private AusleihItemRepository items;
	private ItemAvailabilityService itemAvailabilityService;


	public ItemService(AusleihItemRepository itemRep, ItemAvailabilityService itemAvailabilityService) {
		this.items = itemRep;
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
									 LocalDate availableMin,
									 LocalDate availableMax) {
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
				item -> itemAvailabilityService.isAvailableFromTill(item, availableMin, availableMax)
		);

		return listStream.collect(Collectors.toList());
	}

	public void save(Item newItem) {
		items.save(newItem);
	}

	public void updateById(Long id, AusleihItem newItem) {
		AusleihItem toUpdate = this.findById(id);
		System.out.println("Starting item update");
		toUpdate.setTitel(newItem.getTitel());
		toUpdate.setBeschreibung(newItem.getBeschreibung());
		toUpdate.setAvailableFrom(newItem.getAvailableFrom());
		toUpdate.setAvailableTill(newItem.getAvailableTill());
		toUpdate.setTagessatz(newItem.getTagessatz());
		toUpdate.setKautionswert(newItem.getKautionswert());
		toUpdate.getAbholort().setBeschreibung(newItem.getAbholort().getBeschreibung());
		items.save(toUpdate);
	}
}
