package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.imgscalr.Scalr.resize;

@Entity
@Inheritance
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
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn
	private Abholort abholort = new Abholort();
	@ManyToOne
	private Person besitzer;
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

		BufferedImage image250 = resize(fullImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, 250, 250);
		BufferedImage image100 = resize(fullImage, Scalr.Method.ULTRA_QUALITY, Scalr.Mode.FIT_EXACT, 100, 100);

		ByteArrayOutputStream image250Stream = new ByteArrayOutputStream();
		ImageIO.write(image250, "jpg", image250Stream);
		byte[] image250Bytes = image250Stream.toByteArray();

		ByteArrayOutputStream image100Stream = new ByteArrayOutputStream();
		ImageIO.write(image100, "jpg", image100Stream);
		byte[] image100Bytes = image100Stream.toByteArray();

		this.setPicture250(image250Bytes);
		this.setPicture100(image100Bytes);
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
