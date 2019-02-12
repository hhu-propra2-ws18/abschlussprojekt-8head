package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Person {
	@Id
	@GeneratedValue
	private Long id;

	private String name;
	private String vorname;

	private String username;
	private String password;
	private Rolle rolle;


	private String email;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Item> items;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Ausleihe> ausleihen;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Item> abholOrte;

	public void addAusleihe(Ausleihe ausleihe) {
		ausleihen.add(ausleihe);
		ausleihe.setAusleiher(this);
	}

	public void removeAusleihe(Ausleihe ausleihe) {
		ausleihen.remove(ausleihe);
		ausleihe.setAusleiher(null);
	}

	public void addItem(Item item) {
		items.add(item);
		item.setBesitzer(this);
	}

	public void removeItem(Item item) {
		items.remove(item);
		item.setBesitzer(null);
	}
}
