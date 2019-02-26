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
public class AusleihItemService extends ItemService {

	private AusleihItemRepository items;
	private ItemAvailabilityService itemAvailabilityService;

	public AusleihItemService(AusleihItemRepository itemRep, ItemAvailabilityService itemAvailabilityService) {
		this.items = itemRep;
		this.itemAvailabilityService = itemAvailabilityService;
	}

	public AusleihItem findAusleihItemById(long id) {
		Optional<AusleihItem> item = items.findById(id);
		if (!item.isPresent()) {
			throw new ItemNichtVorhanden();
		}
		return item.get();
	}

	List<AusleihItem> findAllAusleihItem() {
		return items.findAll();
	}

	public List<AusleihItem> extendedDateSearch(LocalDate availableMin, LocalDate availableMax) {
		return items.extendedDateSearch(availableMin,availableMax);
	}

	public List<AusleihItem> extendedSearch(String query,
												int tagessatzMax,
												int kautionswertMax,
												LocalDate availableMin,
												LocalDate availableMax) {
		if (query == null || query.isEmpty()) return items.extendedDateSearch(availableMin,availableMax);
		return items.extendedSearch(query,availableMin,availableMax);
	}

	public void updateById(Long id, AusleihItem newItem) {
		AusleihItem toUpdate = findAusleihItemById(id);
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
