package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Abholort;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class AusleihServiceController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private PersonService personService;
	private ItemService itemService;
	private AbholortService abholortService;

	public AusleihServiceController(PersonService perService, ItemService iService, AbholortService abholortService) {
		this.personService = perService;
		this.itemService = iService;
		this.abholortService = abholortService;
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String query, Principal p) {
		model.addAttribute("person", personService.get(p));

		List<Item> list = itemService.simpleSearch(query);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		model.addAttribute("user", personService.get(p));
		return "artikelListe";
	}

	@GetMapping("/artikelsuche")
	public String artikelSuche(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
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
							   String availableMax,
							   Principal p) {
		model.addAttribute("person", personService.get(p));

		List<Item> list = itemService.extendedSearch(query, tagessatzMax, kautionswertMax, availableMin, availableMax);

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		System.out.println(personService.get(p).getUsername());
		model.addAttribute("user", personService.get(p));

		return "artikelListe";
	}

	@GetMapping("/benutzersuche")
	public String benutzerSuche(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "benutzerSuche";
	}

	@PostMapping("/benutzersuche")
	public String benutzerSuche(Model model,
								String query, //For nachname, vorname, username
								Principal p
	) {
		model.addAttribute("person", personService.get(p));

		List<Person> list = personService.searchByNames(query);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("benutzerListe", list);
		model.addAttribute("user", personService.get(p));
		return "benutzerListe";
	}


	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id, Principal p) {
		model.addAttribute("person", personService.get(p));

		try {
			Item artikel = itemService.findById(id);
			model.addAttribute("artikel", artikel);
		} catch (ItemNichtVorhanden a) {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("user", personService.get(p));
		return "artikelDetails";
	}

	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		model.addAttribute("user", personService.get(p));
		return "startseite";
	}

	@GetMapping("/register")
	public String register(Model model) {
		Person userForm = new Person();
		model.addAttribute("userForm", userForm);
		model.addAttribute("usernameTaken", false);
		model.addAttribute("userForm", userForm);
		return "register";
	}

	@PostMapping("/register")
	public String added(Model model, Person userForm) {
		if (personService.findByUsername(userForm.getUsername()).isPresent()) {
			model.addAttribute("usernameTaken", true);
			model.addAttribute("userForm", userForm);
			return "register";
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userForm.setPassword(encoder.encode(userForm.getPassword()));
		personService.save(userForm);
		return "startseite";
	}

	@GetMapping("/admin")
	public String admin(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "admin";
	}

	@GetMapping("/profil/{id}")
	public String otheruser(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.findById(id));
		model.addAttribute("isOwnProfile", personService.get(p).getId().equals(id));
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		model.addAttribute("isOwnProfile", true);
		return "profil";
	}

	@GetMapping("/bearbeiten/artikel/{id}")
	public String adminEditItem(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.get(p));
		model.addAttribute("artikel", itemService.findById(id));
		return "artikelBearbeitenAdmin";
	}

	@GetMapping("/bearbeiten/benutzer/{id}")
	public String adminEditUser(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.get(p));
		model.addAttribute("benutzer", personService.findById(id));
		return "benutzerBearbeitenAdmin";
	}

	@GetMapping("/newitem")
	public String createItem(Model model, Principal p) {
		Person person = personService.get(p);
		if (person.getAbholorte().isEmpty()) {
			model.addAttribute("message", "Bitte Abholorte hinzufügen");
			return "errorMessage";
		}
		model.addAttribute("person", personService.get(p));
		model.addAttribute("newitem", new Item());
		model.addAttribute("abholorte", person.getAbholorte());
		return "neuerArtikel";
	}

	@PostMapping("/newitem")
	public String addItem(@ModelAttribute Item newItem, Principal p,
						  @RequestParam("file") MultipartFile picture, Model model) {
		Person besitzer = personService.get(p);
		try {
			newItem.setPicture(picture.getBytes());
		} catch (IOException e) {
			model.addAttribute("message", "Datei konnte nicht gespeichert werden");
			return "errorMessage";
		}
		itemService.save(newItem);
		besitzer.addItem(newItem);
		personService.save(besitzer);
		return "redirect:/";
	}

	@GetMapping("/editProfil")
	public String editProfilGet(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "editProfil";
	}

	@PostMapping("/editProfil")
	public String editProfilPost(Model model, Principal p, Person person) {
		model.addAttribute("person", personService.get(p));
		personService.update(person, p);
		return "editProfil";
	}

	@GetMapping("/newlocation")
	public String createNewLocation(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		Abholort abholort = new Abholort();
		abholort.setLatitude(51.227741);
		abholort.setLongitude(6.773456);
		model.addAttribute("abholort", abholort);
		return "neuerAbholort";
	}

	@PostMapping("/newlocation")
	public String saveNewLocation(@ModelAttribute Abholort abholort, Principal p) {
		Person aktuellerNutzer = personService.get(p);
		abholortService.save(abholort);
		aktuellerNutzer.getAbholorte().add(abholort);
		personService.save(aktuellerNutzer);
		return "redirect:/";
	}

}
