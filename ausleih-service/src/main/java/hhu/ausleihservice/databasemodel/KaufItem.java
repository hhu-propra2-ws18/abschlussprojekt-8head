package hhu.ausleihservice.databasemodel;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class KaufItem extends Item {
	private int kaufpreis;
}
