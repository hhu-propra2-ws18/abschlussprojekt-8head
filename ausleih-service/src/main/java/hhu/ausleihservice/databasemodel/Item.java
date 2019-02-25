package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Base64;

@Data
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Item {
	protected String titel = "";
	@Lob
	protected String beschreibung = "";
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn
	protected Abholort abholort = new Abholort();
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@EqualsAndHashCode.Include
	private Long id;
	@ManyToOne
	private Person besitzer;
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

	public String getBase64EncodedString() {
		return this.getPicture() != null ? Base64.getEncoder().encodeToString(this.getPicture()) : null;
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
