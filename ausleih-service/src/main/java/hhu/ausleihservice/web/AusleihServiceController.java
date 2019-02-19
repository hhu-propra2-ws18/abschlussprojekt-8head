package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.security.Principal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AusleihServiceController {

	private PersonService personService;
	private ItemService itemService;

	public AusleihServiceController(PersonService perService, ItemService iService) {
		this.personService = perService;
		this.itemService = iService;
	}

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

	//Checks if a string contains all strings in an array
	private boolean containsArray(String string, String[] array) {
		for (String entry : array) {
			if (!string.contains(entry)) {
				return false;
			}
		}
		return true;
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String q, Principal p) {

		List<Item> list;

		if (q == null || q.isEmpty()) {
			list = itemService.findAll();
		} else {
			//Ignores case
			String[] qArray = q.toLowerCase().split(" ");
			list = itemService.findAll()
					.stream()
					.filter(
							item -> containsArray(
									(item.getTitel()
											+ item.getBeschreibung())
											.toLowerCase(),
									qArray
							)
					)
					.collect(Collectors.toList());
		}

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		model.addAttribute("user", personService.get(p));

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
							   String availableMax,
							   Principal p
	) {
		Stream<Item> listStream = itemService.findAll().stream();

		if (query != null && !query.equals("")) {
			//Ignores Case
			String[] qArray = query.toLowerCase().split(" ");
			listStream = listStream.filter(
					item -> containsArray(
							(item.getTitel() + item.getBeschreibung()).toLowerCase(),
							qArray));
		}

		listStream = listStream.filter(item -> item.getTagessatz() <= tagessatzMax);
		listStream = listStream.filter(item -> item.getKautionswert() <= kautionswertMax);

		listStream = listStream.filter(
				item -> itemService.isAvailableFromTill(item, availableMin, availableMax)
		);

		List<Item> list = listStream.collect(Collectors.toList());
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		System.out.println(personService.get(p).getUsername());
		model.addAttribute("user", personService.get(p));

		return "artikelListe";
	}

	@GetMapping("/benutzersuche")
	public String benutzerSuche(Model model) {
		return "benutzerSuche";
	}

	@PostMapping("/benutzersuche")
	public String benutzerSuche(Model model,
								String query, //For nachname, vorname, username
								Principal p
	) {

		Stream<Person> listStream = personService.findAll().stream();

		if (query != null && !query.equals("")) {
			//Ignores Case
			String[] qArray = query.toLowerCase().split(" ");
			listStream = listStream.filter(
					person -> containsArray(
							(person.getVorname() + " " +
									person.getNachname() + " " +
									person.getUsername())
									.toLowerCase(),
							qArray));
		}

		List<Person> list = listStream.collect(Collectors.toList());
		model.addAttribute("user", personService.get(p));
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("benutzerListe", list);

		return "benutzerListe";
	}


	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id, Principal p) {

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
		model.addAttribute("user", personService.get(p));
		return "startseite";
	}

	@GetMapping("/register")
	public String register(Model model) {
		Person userForm = new Person();
		model.addAttribute("userForm", userForm);
		return "register";
	}

	@PostMapping("/register")
	public String added(@Valid Person userForm, Model model) {
		if (this.personService.getByUsername(userForm.getUsername()) != null) {
			//To-Do: Popup für fehlgeschlagene Registrierung
			return "register";
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userForm.setPassword(encoder.encode(userForm.getPassword()));
		personService.save(userForm);
		return "startseite";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

	@GetMapping("/profil/{id}")
	public String otheruser(Model model, @PathVariable Long id) {
		model.addAttribute("person", personService.findById(id));
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "profil";
	}

	@GetMapping("/bearbeiten/artikel/{id}")
	public String adminEditItem(Model model, @PathVariable Long id) {
		model.addAttribute("artikel", itemService.findById(id));
		return "artikelBearbeitenAdmin";
	}

	@GetMapping("/bearbeiten/benutzer/{id}")
	public String adminEditUser(Model model, @PathVariable Long id) {
		model.addAttribute("benutzer", personService.findById(id));
		return "benutzerBearbeitenAdmin";
	}
}
