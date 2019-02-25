package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
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
