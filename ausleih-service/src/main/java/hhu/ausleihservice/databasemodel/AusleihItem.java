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
}
