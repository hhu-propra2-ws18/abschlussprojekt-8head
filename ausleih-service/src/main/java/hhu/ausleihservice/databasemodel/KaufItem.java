package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class KaufItem {
	private int kaufpreis;
}
