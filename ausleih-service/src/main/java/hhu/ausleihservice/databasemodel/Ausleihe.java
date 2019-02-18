package hhu.ausleihservice.databasemodel;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Ausleihe {
	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	private Long id;
	@ManyToOne
	private Item item;
	@ManyToOne
	private Person ausleiher;
	private LocalDate startDatum;
	private LocalDate endDatum;
	private Long reservationId;

}
