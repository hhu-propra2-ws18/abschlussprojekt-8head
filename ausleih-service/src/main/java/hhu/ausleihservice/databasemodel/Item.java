package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;

	private String titel = "";
	private String beschreibung = "";
	private Integer tagessatz;
	private Integer kautionswert;

	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn
	private Abholort abholort = new Abholort();
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate availableFrom;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate availableTill;
	@ManyToOne
	private Person besitzer;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen = new HashSet<>();

	@Lob
	private byte[] picture;

	// Getter and Setter are copying the array to prevent
	// data leaking outside by storing/giving the reference to the array
	@Lob
	public byte[] getPicture() {
		if (picture == null) {
			return null;
		}
		byte[] out = new byte[picture.length];
		System.arraycopy(picture, 0, out, 0, picture.length);
		return out;
	}

	public void setPicture(byte[] in) {
		if (in == null) {
			return;
		}
		picture = new byte[in.length];
		System.arraycopy(in, 0, picture, 0, in.length);
	}

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

	public String getBase64EncodedString() {
		return this.getPicture() != null ? Base64.getEncoder().encodeToString(this.getPicture()) : null;
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

	public void trimWhitespace() {
		titel = titel.trim();
		beschreibung = beschreibung.trim();
		abholort.trimWhitespace();
	}

	public void setTitel(String s) {
		if (s != null) titel = s.trim();
	}

	public void setBeschreibung(String s) {
		if (s != null) beschreibung = s.trim();
	}

	public void setAbholort(Abholort a) {
		if (a != null) abholort = a;
	}
}
