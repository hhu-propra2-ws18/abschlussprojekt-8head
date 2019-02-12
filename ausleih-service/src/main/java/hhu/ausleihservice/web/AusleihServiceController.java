package hhu.ausleihservice.web;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Controller
public class AusleihServiceController {

	//Temporäre Testklasse
	@Data
	private class TestArtikel{
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		private int id;
		private String titel;
		private String beschreibung;
		private String verfuegbarkeit;
		private int ausleihkosten;
		private int kaution;
		private String standort;

		//In richtiger Klasse durch person Objekt ersetzen
		private int personId;

		int getPersonId(){
			//In richtiger Klasse durch person.getId() ersetzen
			return personId;
		}
	}

	@GetMapping("/liste")
	public String artikelListe(Model model){

		return "artikelListe";
	}

	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam(defaultValue = "-1", required = false) long id){

		return "artikelListe";
	}
}
