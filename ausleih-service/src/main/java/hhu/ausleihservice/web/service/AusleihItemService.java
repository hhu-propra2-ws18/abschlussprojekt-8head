package hhu.ausleihservice.web.service;

import hhu.ausleihservice.dataaccess.AusleihItemRepository;
import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AusleihItemService extends ItemService {

	private AusleihItemRepository items;
	private ItemAvailabilityService itemAvailabilityService;


	public AusleihItemService(AusleihItemRepository itemRep, ItemAvailabilityService itemAvailabilityService) {
		this.items = itemRep;
		this.itemAvailabilityService = itemAvailabilityService;
	}

	@Override
	public AusleihItem findById(long id) {
		Optional<AusleihItem> item = items.findById(id);
		if (!item.isPresent()) {
			throw new ItemNichtVorhanden();
		}
		return item.get();
	}

	List<AusleihItem> findAllAusleihItem() {
		return items.findAll();
	}

	public List<AusleihItem> extendedSearch(String query,
											int tagessatzMax,
											int kautionswertMax,
											LocalDate availableMin,
											LocalDate availableMax) {
		Stream<AusleihItem> listStream = findAllAusleihItem().stream();

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

	public void updateById(Long id, AusleihItem newItem) {
		AusleihItem toUpdate = findById(id);
		System.out.println("Starting ausleih item update");
		toUpdate.setTitel(newItem.getTitel());
		toUpdate.setBeschreibung(newItem.getBeschreibung());
		toUpdate.setAvailableFrom(newItem.getAvailableFrom());
		toUpdate.setAvailableTill(newItem.getAvailableTill());
		toUpdate.setTagessatz(newItem.getTagessatz());
		toUpdate.setKautionswert(newItem.getKautionswert());
		toUpdate.getAbholort().setBeschreibung(newItem.getAbholort().getBeschreibung());
		items.save(toUpdate);
	}

	public void save(AusleihItem newItem) {
		items.save(newItem);
	}

}
