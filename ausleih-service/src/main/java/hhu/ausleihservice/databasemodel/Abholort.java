package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Abholort {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String beschreibung;
}


