package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(exclude = "ausleihen")
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nachname;
	private String vorname;

	private String username;
	private String password;
	private Rolle rolle;

	private String email;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Item> items = new HashSet<>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen = new HashSet<>();
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	private Set<Abholort> abholorte = new HashSet<>();

	public void addAusleihe(Ausleihe ausleihe) {
		if (ausleihe == null) {
			return;
		}
		ausleihen.add(ausleihe);
		ausleihe.setAusleiher(this);
	}

	public void removeAusleihe(Ausleihe ausleihe) {
		if (ausleihe == null) {
			return;
		}
		ausleihen.remove(ausleihe);
		ausleihe.setAusleiher(null);
	}

	public void addItem(Item item) {
		if (item == null) {
			return;
		}
		items.add(item);
		item.setBesitzer(this);
	}

	public void removeItem(Item item) {
		if (item == null) {
			return;
		}
		items.remove(item);
		item.setBesitzer(null);
	}

	public String getName() {
		return vorname + " " + nachname;
	}
}
