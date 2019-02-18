package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	private Long reservationId;


	@Override
	public String toString() {
		return "Ausleihe("
				+ "id=" + ((id == null) ? "null" : id.toString()) + ", "
				+ "item="
				+ ((item == null) ? "null" :
				"Item("
						+ "id=" + item.getId() + ", "
						+ "titel=" + item.getTitel())
				+ "), "
				+ "ausleiher=" + ((ausleiher == null) ? "null" : ausleiher.getName()) + ", "
				+ "startDatum=" + ((startDatum == null) ? "null" : startDatum.toString()) + ", "
				+ "endDatum=" + ((endDatum == null) ? "null" : endDatum.toString()) + ", "
				+ "reservationId=" + ((reservationId == null) ? "null" : reservationId.toString())
				+ ")";
	}
}
