package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Item {
	@Id
	@GeneratedValue
	private Long id;

	private String titel;
	private int tagessatz;
	private String abholort;
	private int kautionswert;

}
