package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Rolle;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
public class AusleihServiceController {

	@Autowired
	PersonRepository personRepository;


	//Temporäre Testklasse
	@Data
	private class TestArtikel {

		private TestArtikel() {
		}

		private TestArtikel(
				int id,
				String titel,
				String beschreibung,
				String verfuegbarkeit,
				int tagessatz,
				int kaution,
				String standort,
				int personId,
				String personName
		) {
			this.id = id;
			this.titel = titel;
			this.beschreibung = beschreibung;
			this.verfuegbarkeit = verfuegbarkeit;
			this.tagessatz = tagessatz;
			this.kaution = kaution;
			this.standort = standort;
			this.personId = personId;
			this.personName = personName;
		}

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int id;
		private String titel;
		private String beschreibung;
		private String verfuegbarkeit;
		private int tagessatz;
		private int kaution;
		private String standort;

		//In richtiger Klasse durch person Objekt ersetzen
		private int personId;
		private String personName;

		public int getPersonId() {
			//In richtiger Klasse durch person.getId() ersetzen
			return personId;
		}

		public String getPersonName() {
			//In richtiger Klasse durch person.getName() ersetzen
			return personName;
		}
	}

	private List<TestArtikel> testListe = new ArrayList<>(
			Arrays.asList(
					new TestArtikel(0, "Stift", "Zum stiften gehen", "Mo-Fr 08:00-22:00", 3, 1, "Dusseldorf", 0, "Gerold Steiner"),
					new TestArtikel(1, "Fahrrad", "Falls man sich radlos fühlt", "Sa-So 03:00-03-45", 30, 70, "Düsburg", 5, "Volker Racho"),
					new TestArtikel(2, "Pfeil", "Wenn man den Bogen schon raus hat", "Mo,Mi 15:00-17:00", 42, 1337, "Eßn", 7, "Wilma Pause")
			)
	);

	@GetMapping("/liste")
	public String artikelListe(Model model) {

		model.addAttribute("artikelListe", testListe);

		return "artikelListe";
	}

	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam int id) {

		try {
			TestArtikel artikel = testListe.get(id);
			model.addAttribute("artikel", artikel);
			return "artikelDetails";

		} catch (IndexOutOfBoundsException e) {

			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
	}

	@GetMapping("/")
	public String startseite(Model model) {
		Person simon = new Person();
		simon.setUsername("siker102");
		simon.setRolle(Rolle.ADMIN);
		simon.setPassword("$2a$10$mTkoHy1ISOHh.i0l0pJqKur0AOFVeBG1C.zwOkYGcS/T1fDVvEv3i");
		personRepository.save(simon);

		System.out.println(personRepository.findAll().get(0));

		return "startseite";
	}

	@GetMapping("/register")
	public String register(Model model) {
		Person newPerson = new Person();
		model.addAttribute("person", newPerson);
		return "register";
	}

	@PostMapping("/register")
	public String added(Model model, @ModelAttribute("person") Person person) {
		personRepository.save(person);
		return "redirect:http://localhost:8080/startseite/";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

}
