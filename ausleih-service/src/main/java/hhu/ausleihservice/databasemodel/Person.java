package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

	public boolean isAdmin() {
		return this.getRole().equals(Role.ADMIN);
	}

	public void encryptPassword() {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		password = encoder.encode(password);
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
