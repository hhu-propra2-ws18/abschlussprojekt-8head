package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Abholort;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import hhu.ausleihservice.web.service.*;
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
public class ItemController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private final PersonService personService;
	private ItemService itemService;
	private AbholortService abholortService;

	public ItemController(PersonService perService, ItemService iService, AbholortService abholortService) {
		this.personService = perService;
		this.itemService = iService;
		this.abholortService = abholortService;
	}


	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		return "startseite";
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String query, Principal p) {
		List<Item> list = itemService.simpleSearch(query);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		model.addAttribute("user", personService.get(p));
		return "artikelListe";
	}

	@GetMapping("/artikelsuche")
	public String artikelSuche(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
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
		model.addAttribute("user", personService.get(p));

		List<Item> list = itemService.extendedSearch(query, tagessatzMax, kautionswertMax, availableMin, availableMax);

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);

		return "artikelListe";
	}

	@GetMapping("/details/{id}")
	public String artikelDetails(Model model,
								 @PathVariable long id,
								 Principal p) {
		try {
			model.addAttribute("artikel", itemService.findById(id));
		} catch (ItemNichtVorhanden a) {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("user", personService.get(p));
		return "artikelDetails";
	}

	@PostMapping("/details/{id}")
	public String bearbeiteArtikel(Model model,
								   @PathVariable long id,
								   Principal p,
								   @RequestParam(name = "editArtikel", defaultValue = "false")
									   final boolean changeArticleDetails,
								   @ModelAttribute("artikel") Item artikel
	) {
		System.out.println("Post triggered at /details/" + id);

		if (changeArticleDetails) {
			itemService.updateById(id, artikel);
		}
		model.addAttribute("artikel", itemService.findById(id));
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("user", personService.get(p));

		return "artikelDetails";
	}

	@GetMapping("/newitem")
	public String createItem(Model model, Principal p) {
		Person person = personService.get(p);
		if (person.getAbholorte().isEmpty()) {
			model.addAttribute("message", "Bitte Abholorte hinzufügen");
			return "errorMessage";
		}
		model.addAttribute("user", personService.get(p));
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

	@GetMapping("/newlocation")
	public String createNewLocation(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
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
