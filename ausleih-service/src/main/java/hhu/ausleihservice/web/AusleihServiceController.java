package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Abholort;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Rolle;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AusleihServiceController {

	private PersonService personService;
	private ItemService itemService;
	private AbholortService abholortService;

	public AusleihServiceController(PersonService perService, ItemService iService, AbholortService abholortService) {
		this.personService = perService;
		this.itemService = iService;
		this.abholortService = abholortService;
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
	public String artikelListe(Model model, @RequestParam(required = false) String q) {

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
		model.addAttribute("person", person);
		return "register";
	}

	@PostMapping("/register")
	public String added(Person person, Model model) {
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
	public String otheruser(Model model, @PathVariable Long id) {
		model.addAttribute("person", personService.getById(id));
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "profil";
	}

	@GetMapping("/newitem")
	public String createItem(Model model, Principal p) {
		Person person = personService.get(p);
		//Dummy
		Abholort abholort = new Abholort();
		abholort.setBeschreibung("Dummy");
		abholortService.save(abholort);
		person.getAbholorte().add(abholort);
		personService.save(person);


		model.addAttribute("newitem", new Item());
		model.addAttribute("abholorte", person.getAbholorte());
		return "neuerArtikel";
	}

	@PostMapping("/newitem")
	public String addItem(@ModelAttribute Item newItem, Principal p, @RequestParam("file") MultipartFile picture) {
		Person besitzer = personService.get(p);
		try {
			newItem.setPicture(picture.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
		itemService.save(newItem);
		besitzer.addItem(newItem);
		personService.save(besitzer);
		return "redirect:/";
	}
}
