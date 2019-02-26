package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class AusleihItem extends Item {

	private Integer tagessatz;
	private Integer kautionswert;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate availableFrom;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate availableTill;
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen = new HashSet<>();

	public void addAusleihe(Ausleihe ausleihe) {
		if (ausleihe != null) {
			ausleihen.add(ausleihe);
			ausleihe.setItem(this);
		}
	}

	public void removeAusleihe(Ausleihe ausleihe) {
		ausleihen.remove(ausleihe);
		ausleihe.setItem(null);
	}

	public ArrayList<Period> getAvailablePeriods() {

		ArrayList<Period> out = new ArrayList<>();

		if (ausleihen.isEmpty()) {
			out.add(new Period(availableFrom, availableTill));
			return out;
		}

		Ausleihe[] sortierteAusleihen = getSortierteAusleihen();
		int length = sortierteAusleihen.length;

		LocalDate start = availableFrom;
		LocalDate end = sortierteAusleihen[0].getStartDatum();

		if (!start.equals(end)) {
			out.add(new Period(start, end.minusDays(1)));
		}

		for (int i = 0; i < length - 1; i++) {

			start = sortierteAusleihen[i].getEndDatum();
			end = sortierteAusleihen[i + 1].getStartDatum();

			if (!start.equals(end)) {
				out.add(new Period(start.plusDays(1), end.minusDays(1)));
			}
		}

		start = sortierteAusleihen[length - 1].getEndDatum();
		end = availableTill;

		if (!start.equals(end)) {
			out.add(new Period(start.plusDays(1), end));
		}

		return out;
	}

	Ausleihe[] getSortierteAusleihen() {

		Ausleihe[] sortierteAusleihen = new Ausleihe[ausleihen.size()];
		List<Ausleihe> tempAusleihen = new ArrayList<>(ausleihen);

		for (int i = 0; i < ausleihen.size(); i++) {

			Ausleihe smallest = tempAusleihen.get(0);
			LocalDate smallestDate = smallest.getStartDatum();

			for (Ausleihe test : tempAusleihen) {
				LocalDate testDate = test.getStartDatum();
				if (testDate.isBefore(smallestDate)) {
					smallest = test;
					smallestDate = smallest.getStartDatum();
				}
			}
			sortierteAusleihen[i] = smallest;
			tempAusleihen.remove(smallest);
		}

		return sortierteAusleihen;
	}

}
