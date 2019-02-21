package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Ausleihe;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ItemAvailabilityService {

	private boolean isInPeriod(LocalDate date, LocalDate start, LocalDate end) {
		return !date.isBefore(start) && !date.isAfter(end);
	}

	public boolean isAvailable(Item item) {
		return isAvailable(item, LocalDate.now());
	}

	boolean isAvailable(Item item, LocalDate date) {
		LocalDate availableFrom = item.getAvailableFrom();
		LocalDate availableTill = item.getAvailableTill();
		Set<Ausleihe> ausleihen = item.getAusleihen();

		return isInPeriod(date, availableFrom, availableTill)
				&& ausleihen.stream().noneMatch((ausleihe)
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
  
  public List<String> getUnavailableDates(Item item) {
		LocalDate temp = item.getAvailableFrom();
		LocalDate end = item.getAvailableTill();
		List<String> unavailabeDates = new ArrayList<>();
		while (!temp.isAfter(end)) {
			if (!isAvailable(item, temp)) {
				unavailabeDates.add(temp.toString());
			}
			temp = temp.plusDays(1);
		}
		return unavailabeDates;
	}
}
