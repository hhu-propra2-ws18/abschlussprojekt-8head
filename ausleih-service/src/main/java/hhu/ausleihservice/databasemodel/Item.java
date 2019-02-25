package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static org.imgscalr.Scalr.Mode;
import static org.imgscalr.Scalr.Method;
import static org.imgscalr.Scalr.resize;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;

	private String titel = "";
	@Lob
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

	@Lob
	private byte[] picture250;

	@Lob
	private byte[] picture100;

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

		try {
			resizeImages(in);
		} catch (IOException e) {
			System.out.println("Error resizing images. Resized Images are not changed.");
		}
	}

	@Lob
	public byte[] getPicture250() {
		if (picture250 == null) {
			return null;
		}
		byte[] out = new byte[picture250.length];
		System.arraycopy(picture250, 0, out, 0, picture250.length);
		return out;
	}

	private void setPicture250(byte[] in) {
		if (in == null) {
			return;
		}
		picture250 = new byte[in.length];
		System.arraycopy(in, 0, picture250, 0, in.length);
	}

	@Lob
	public byte[] getPicture100() {
		if (picture100 == null) {
			return null;
		}
		byte[] out = new byte[picture100.length];
		System.arraycopy(picture100, 0, out, 0, picture100.length);
		return out;
	}

	private void setPicture100(byte[] in) {
		if (in == null) {
			return;
		}
		picture100 = new byte[in.length];
		System.arraycopy(in, 0, picture100, 0, in.length);
	}

	private void resizeImages(byte[] in) throws IOException {
		BufferedImage fullImage = ImageIO.read(new ByteArrayInputStream(in));

		if (fullImage == null) {
			throw new IOException();
		}

		BufferedImage image250 = resize(fullImage, Method.ULTRA_QUALITY, Mode.FIT_EXACT, 250, 250);
		BufferedImage image100 = resize(fullImage, Method.ULTRA_QUALITY, Mode.FIT_EXACT, 100, 100);

		ByteArrayOutputStream image250Stream = new ByteArrayOutputStream();
		ImageIO.write(image250, "png", image250Stream);
		byte[] image250Bytes = image250Stream.toByteArray();

		ByteArrayOutputStream image100Stream = new ByteArrayOutputStream();
		ImageIO.write(image100, "png", image100Stream);
		byte[] image100Bytes = image100Stream.toByteArray();

		this.setPicture250(image250Bytes);
		this.setPicture100(image100Bytes);
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

	public String getPictureBase64EncodedString() {
		return this.getPicture() != null ? Base64.getEncoder().encodeToString(this.getPicture()) : null;
	}

	public String getPicture250Base64EncodedString() {
		return this.getPicture250() != null ? Base64.getEncoder().encodeToString(this.getPicture250()) : null;
	}

	public String getPicture100Base64EncodedString() {
		return this.getPicture100() != null ? Base64.getEncoder().encodeToString(this.getPicture100()) : null;
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

	public void setTitel(String s) {
		if (s != null) {
			titel = s.trim();
		}
	}

	public void setBeschreibung(String s) {
		if (s != null) {
			beschreibung = s.trim();
		}
	}

	public void setAbholort(Abholort a) {
		if (a != null) {
			abholort = a;
		}
	}
}
