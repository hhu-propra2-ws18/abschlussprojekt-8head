package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Type;
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
	private String beschreibung = "";
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn
	private Abholort abholort = new Abholort();
	@ManyToOne
	private Person besitzer;


	@Column(columnDefinition = "text")
	private String picture;
	@Column(columnDefinition = "text")
	private String picture250;
	@Column(columnDefinition = "text")
	private String picture100;


	public void setPicture(byte[] in) {
		if (in == null) {
			return;
		}
		picture = Base64.getEncoder().encodeToString(in);
		try {
			resizeImages(in);
		} catch (IOException e) {
			System.out.println("Error resizing images. Resized Images are not changed.");
		}
	}

	private void setPicture250(byte[] in) {
		this.picture250=Base64.getEncoder().encodeToString(in);
	}

	private void setPicture100(byte[] in) {
		this.picture100 = Base64.getEncoder().encodeToString(in);
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
