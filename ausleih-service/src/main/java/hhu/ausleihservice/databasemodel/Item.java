package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Item {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	private String titel;
	private String beschreibung;
	private int tagessatz;
	private int kautionswert;
	private Abholort standort;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Abholort abholort;

	private LocalDate availableFrom;
	private LocalDate availableTill;
	@ManyToOne
	private Person besitzer;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen;
	@Lob
	private byte[] picture;

	private boolean isInPeriod(LocalDate date, LocalDate start, LocalDate end) {
		return (date.isAfter(start)
				&& date.isBefore(end))
				|| (date.isEqual(start)
				|| date.isEqual(end));
	}

	public boolean isAvailable(LocalDate date) {
		if (!isInPeriod(date, availableFrom, availableTill)) {
			return false;
		}
		for (Ausleihe ausleihe : ausleihen) {
			LocalDate startDatum = ausleihe.getStartDatum();
			LocalDate endDatum = ausleihe.getEndDatum();
			if (isInPeriod(date, startDatum, endDatum)) {
				return false;
			}
		}
		return true;
	}

	public void addAusleihe(Ausleihe ausleihe) {
		ausleihen.add(ausleihe);
		ausleihe.setItem(this);
	}

	public void removeAusleihe(Ausleihe ausleihe) {
		ausleihen.remove(ausleihe);
		ausleihe.setItem(null);
	}

	public boolean available(){
		return availableTill.isBefore(LocalDate.now());
	}

	public long getPersonId(){
		return besitzer.getId();
	}

	public String getPersonName(){
		return besitzer.getVorname() + " " + besitzer.getName();
	}
}
