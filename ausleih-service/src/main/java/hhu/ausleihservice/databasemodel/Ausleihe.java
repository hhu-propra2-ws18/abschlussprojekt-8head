package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
public class Ausleihe {
	@Id
	@GeneratedValue
	private Long id;
    @ManyToOne
	private Item item;
	@ManyToOne
	private Person ausleiher;
	private LocalDate startDatum;
	private LocalDate endDatum;
}
