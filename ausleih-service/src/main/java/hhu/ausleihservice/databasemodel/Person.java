package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Person {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String nachname = "";
	private String vorname = "";

	@EqualsAndHashCode.Include
	private String username = "";
	private String password = "";
	private Role role = Role.USER;

	private String email = "";

	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Item> items = new HashSet<>();
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen = new HashSet<>();
	@ToString.Exclude
	@OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
	private Set<Abholort> abholorte = new HashSet<>();
	@ToString.Exclude
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Kauf> kaeufe = new HashSet<>();

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

	public void addKauf(Kauf kauf) {
		if (kauf == null) {
			return;
		}
		kaeufe.add(kauf);
		kauf.setKaeufer(this);
	}

	public void removeKauf(Kauf kauf) {
		if (kauf == null) {
			return;
		}
		kaeufe.remove(kauf);
		kauf.setKaeufer(null);
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

	public boolean isAdmin() {
		return this.getRole().equals(Role.ADMIN);
	}

	public boolean isOwner(Item artikel) {
		return this.getId().equals(artikel.getBesitzer().getId());
	}

	public boolean isHimself(Person person) {
		return this.getId().equals(person.getId());
	}

	public void setNachname(String s) {
		if (s != null) {
			nachname = s.trim();
		}
	}

	public void setVorname(String s) {
		if (s != null) {
			vorname = s.trim();
		}
	}

	public void setUsername(String s) {
		if (s != null) {
			username = s.trim();
		}
	}

	public void setEmail(String s) {
		if (s != null) {
			email = s.trim();
		}
	}
}
