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
	private String email;

	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<Item> ownedItems;
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	private Set<Item> borrowedItems;
}
