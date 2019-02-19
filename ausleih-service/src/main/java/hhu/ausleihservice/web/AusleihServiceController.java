package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Rolle;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AusleihServiceController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private PersonService personService;
	private ItemService itemService;

	public AusleihServiceController(PersonService perService, ItemService iService) {
		this.personService = perService;
		this.itemService = iService;
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String query) {

		List<Item> list = itemService.simpleSearch(query);

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);

		return "artikelListe";
	}

	@GetMapping("/artikelsuche")
	public String artikelSuche(Model model) {
		model.addAttribute("datum", LocalDateTime.now().format(DATEFORMAT));
		return "artikelSuche";
	}

	@PostMapping("/artikelsuche")
	public String artikelSuche(Model model,
	                           String query, //For titel or beschreibung
	                           @RequestParam(defaultValue = "2147483647")
			                           int tagessatzMax,
	                           @RequestParam(defaultValue = "2147483647")
			                           int kautionswertMax,
	                           String availableMin, //YYYY-MM-DD
	                           String availableMax
	) {
		List<Item> list = itemService.extendedSearch(query, tagessatzMax, kautionswertMax, availableMin, availableMax);

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);

		return "artikelListe";
	}

	@GetMapping("/benutzersuche")
	public String benutzerSuche(Model model) {
		return "benutzerSuche";
	}

	@PostMapping("/benutzersuche")
	public String benutzerSuche(Model model,
	                            String query //For nachname, vorname, username
	) {
		List<Person> list = personService.searchByNames(query);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("benutzerListe", list);

		return "benutzerListe";
	}


	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id) {

		try {
			Item artikel = itemService.findByID(id);
			model.addAttribute("artikel", artikel);
		} catch (ItemNichtVorhanden a) {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
		model.addAttribute("dateformat", DATEFORMAT);
		return "artikelDetails";
	}

	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		Person person = personService.get(p);
		model.addAttribute("person", person);

		return "startseite";
	}

	@GetMapping("/register")
	public String register(Model model) {
		Person person = new Person();
		model.addAttribute("usernameTaken", false);
		model.addAttribute("person", person);
		return "register";
	}

	@PostMapping("/register")
	public String added(Model model, Person person) {
		if (personService.findByUsername(person.getUsername()).isPresent()) {
			model.addAttribute("usernameTaken", true);
			model.addAttribute("person", person);
			return "register";
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		person.setPassword(encoder.encode(person.getPassword()));
		person.setRolle(Rolle.USER);
		personService.save(person);
		return startseite(model, null);
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

	@GetMapping("/profil/{id}")
	public String otheruser(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.getById(id));
		model.addAttribute("isOwnProfile", personService.get(p).getId().equals(id));
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		model.addAttribute("isOwnProfile", true);
		return "profil";
	}

	@GetMapping("/editProfil")
	public String editProfilGet(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "editProfil";
	}

	@PostMapping("/editProfil")
	public String editProfilPost(Model model, Principal p, Person person) {
		personService.update(person, p);
		return "editProfil";
	}
}
