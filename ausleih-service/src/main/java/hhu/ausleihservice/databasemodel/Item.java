package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String titel;
	private String beschreibung;
	private int tagessatz;
	private int kautionswert;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	private Abholort abholort;

	private LocalDate availableFrom;
	private LocalDate availableTill;
	@ManyToOne
	private Person besitzer;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen;
	@Lob
	private byte[] picture;

	//Getter and Setter are copying the array to prevent data leaking
	// outside by storing/giving the reference to the array
	@Lob
	public byte[] getPicture() {
		byte[] out = new byte[picture.length];
		System.arraycopy(picture, 0, out, 0, picture.length);
		return out;
	}

	public void setPicture(byte[] in) {
		picture = new byte[in.length];
		System.arraycopy(in, 0, picture, 0, in.length);
	}

	private boolean isInPeriod(LocalDate date, LocalDate start, LocalDate end) {
		return (!date.isBefore(start) && !date.isAfter(end));
	}

	public boolean isAvailable(LocalDate date) {
		return isInPeriod(date, availableFrom, availableTill)
				&& ausleihen.stream().noneMatch((ausleihe)
				-> isInPeriod(date, ausleihe.getStartDatum(), ausleihe.getEndDatum()));
	}

	public void addAusleihe(Ausleihe ausleihe) {
		ausleihen.add(ausleihe);
		ausleihe.setItem(this);
	}

	public void removeAusleihe(Ausleihe ausleihe) {
		ausleihen.remove(ausleihe);
		ausleihe.setItem(null);
	}

	public long getPersonId() {
		return besitzer.getId();
	}

	public String getPersonName() {
		return besitzer.getVorname() + " " + besitzer.getName();
	}
}
