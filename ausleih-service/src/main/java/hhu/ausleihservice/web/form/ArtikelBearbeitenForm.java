package hhu.ausleihservice.web.form;

import lombok.Data;

@Data
public class ArtikelBearbeitenForm {
	private String newTitel;
	private String newBeschreibung;
	private int newTagessatz;
	private int newKautionswert;
}
