package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.web.form.ArtikelBearbeitenForm;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ItemService {

	private ItemRepository items;
	private ItemAvailabilityService itemAvailabilityService;


	ItemService(ItemRepository itemRep, ItemAvailabilityService itemAvailabilityService) {
		this.items = itemRep;
		this.itemAvailabilityService = itemAvailabilityService;
	}

	Item findById(long id) {
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
				item -> itemAvailabilityService.isAvailableFromTill(item, availableMin, availableMax)
		);

		List<Item> list = listStream.collect(Collectors.toList());

		return list;
	}

	public void save(Item newItem) {
		items.save(newItem);
	}

	public void updateById(Long id, ArtikelBearbeitenForm artikelBearbeitenForm) {
		Item toUpdate = this.findById(id);
		if (!(artikelBearbeitenForm.getNewTitel().equals(""))) {
			toUpdate.setTitel(artikelBearbeitenForm.getNewTitel());
		}
		if (!(artikelBearbeitenForm.getNewBeschreibung().equals(""))) {
			toUpdate.setBeschreibung(artikelBearbeitenForm.getNewBeschreibung());
		}
		if (!(artikelBearbeitenForm.getNewKautionswert()==0)) {
			toUpdate.setKautionswert(artikelBearbeitenForm.getNewKautionswert());
		}
		if (!(artikelBearbeitenForm.getNewTagessatz()==0)) {
			toUpdate.setTagessatz(artikelBearbeitenForm.getNewTagessatz());
		}
		items.save(toUpdate);
	}
}
