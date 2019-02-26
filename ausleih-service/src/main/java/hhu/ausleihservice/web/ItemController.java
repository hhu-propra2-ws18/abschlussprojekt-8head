package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.*;
import hhu.ausleihservice.form.AusleihForm;
import hhu.ausleihservice.validators.AbholortValidator;
import hhu.ausleihservice.validators.AusleiheValidator;
import hhu.ausleihservice.validators.ItemValidator;
import hhu.ausleihservice.web.responsestatus.ItemNichtVorhanden;
import hhu.ausleihservice.web.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ItemController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private final PersonService personService;
	private final ItemService itemService;
	private final AbholortService abholortService;
	private final ItemAvailabilityService itemAvailabilityService;
	private ItemValidator itemValidator;
	private AbholortValidator abholortValidator;
	private AusleiheValidator ausleiheValidator;
	private AusleiheService ausleiheService;

	public ItemController(AusleiheService ausleiheService,
						  PersonService perService,
						  ItemService iService,
						  AbholortService abholortService,
						  ItemAvailabilityService itemAvailabilityService,
						  ItemValidator itemValidator,
						  AbholortValidator abholortValidator,
						  AusleiheValidator ausleiheValidator
	) {
		this.ausleiheService = ausleiheService;
		this.personService = perService;
		this.itemService = iService;
		this.abholortService = abholortService;
		this.itemAvailabilityService = itemAvailabilityService;
		this.itemValidator = itemValidator;
		this.abholortValidator = abholortValidator;
		this.ausleiheValidator = ausleiheValidator;
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String q, Principal p) {
		if (q != null) {
			q = q.trim();
		}
		List<Item> list = itemService.simpleSearch(q);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);
		model.addAttribute("user", personService.get(p));
		return "artikelListe";
	}

	@GetMapping("/artikelsuche")
	public String artikelSuche(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		model.addAttribute("today", LocalDateTime.now().format(DATEFORMAT));
		return "artikelSuche";
	}

	@PostMapping("/artikelsuche")
	public String artikelSuche(Model model,
							   String query, //For titel or beschreibung
							   @RequestParam(defaultValue = "2147483647")
									   int tagessatzMax,
							   @RequestParam(defaultValue = "2147483647")
									   int kautionswertMax,
							   @RequestParam
							   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
									   LocalDate availableMin,
							   @RequestParam
							   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
									   LocalDate availableMax,
							   Principal p) {
		model.addAttribute("user", personService.get(p));

		if (query != null) {
			query = query.trim();
		}

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
			Item artikel = itemService.findById(id);
			model.addAttribute("artikel", artikel);
			model.addAttribute("availabilityList", itemAvailabilityService.getUnavailableDates(artikel));
		} catch (ItemNichtVorhanden a) {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}

		model.addAttribute("ausleihForm", new AusleihForm());
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("user", personService.get(p));
		return "artikelDetails";
	}

	@PostMapping("/details/{id}")
	public String bearbeiteArtikel(Model model,
								   @PathVariable long id,
								   Principal p,
								   @RequestParam(
										   name = "editArtikel", defaultValue = "false"
								   ) final boolean changeArticleDetails,
								   @ModelAttribute("artikel") Item artikel,
								   BindingResult bindingResult
	) {
		System.out.println("Post triggered at /details/" + id);
		System.out.println(artikel + " " + changeArticleDetails);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("user", personService.get(p));
		model.addAttribute("ausleihForm", new AusleihForm());
		Item item = new Item();
		System.out.println(item);
		itemValidator.validate(artikel, bindingResult);

		if (changeArticleDetails) {
			if (bindingResult.hasErrors()) {
				model.addAttribute("artikel", itemService.findById(id));
				model.addAttribute("beschreibungErrors", bindingResult.getFieldError("beschreibung"));
				model.addAttribute("titelErrors", bindingResult.getFieldError("titel"));
				model.addAttribute("tagessatzErrors", bindingResult.getFieldError("tagessatz"));
				model.addAttribute("kautionswertErrors", bindingResult.getFieldError("kautionswert"));
				model.addAttribute("availableFromErrors", bindingResult.getFieldError("availableFrom"));
				model.addAttribute("abholortErrors", bindingResult.getFieldError("abholort"));
				model.addAttribute("dateformat", DATEFORMAT);
				return "artikelDetails";
			}
			itemService.updateById(id, artikel);
		}
		model.addAttribute("artikel", itemService.findById(id));

		return "artikelDetails";
	}

	//2019-05-02 - 2019-05-09
	@PostMapping("/ausleihen/{id}")
	public String ausleihen(@PathVariable Long id, @ModelAttribute AusleihForm ausleihForm, Principal p, Model model) {
		Item artikel = itemService.findById(id);
		Ausleihe ausleihe = new Ausleihe();
		Person user = personService.get(p);

		//Please refactor TODO
		String startDatum = ausleihForm.getDate().substring(0, 10);
		String endDatum = ausleihForm.getDate().substring(13);

		ausleihe.setStartDatum(LocalDate.parse(startDatum));
		ausleihe.setEndDatum(LocalDate.parse(endDatum));
		ausleihe.setStatus(Status.ANGEFRAGT);

		ausleihe.setAusleiher(user);
		ausleihe.setItem(artikel);

		DataBinder dataBinder = new DataBinder(ausleihe);
		dataBinder.setValidator(ausleiheValidator);
		dataBinder.validate();
		BindingResult bindingResult = dataBinder.getBindingResult();
		if (bindingResult.hasErrors()) {
			model.addAttribute("startDatumErrors", bindingResult.getFieldError("startDatum"));
			model.addAttribute("endDatumErrors", bindingResult.getFieldError("endDatum"));
			model.addAttribute("ausleiherErrors", bindingResult.getFieldError("ausleiher"));
			model.addAttribute("artikel", artikel);
			model.addAttribute("availabilityList", itemAvailabilityService.getUnavailableDates(artikel));
			model.addAttribute("ausleihForm", new AusleihForm());
			model.addAttribute("dateformat", DATEFORMAT);
			model.addAttribute("user", user);
			return "artikelDetails";
		}

		user.addAusleihe(ausleihe);
		artikel.addAusleihe(ausleihe);

		ausleiheService.save(ausleihe);
		personService.save(user);
		itemService.save(artikel);

		return "redirect:/";
	}


	@GetMapping("/newitem")
	public String createItem(Model model, Principal p) {
		Person user = personService.get(p);
		if (user.getAbholorte().isEmpty()) {
			model.addAttribute("message", "Bitte Abholorte hinzufügen");
			return "errorMessage";
		}
		model.addAttribute("user", user);
		model.addAttribute("newitem", new Item());
		model.addAttribute("abholorte", user.getAbholorte());
		model.addAttribute("today", LocalDateTime.now().format(DATEFORMAT));
		return "neuerArtikel";
	}

	@PostMapping("/newitem")
	public String addItem(Model model,
						  @ModelAttribute Item newItem,
						  Principal p,
						  @RequestParam("file") MultipartFile picture,
						  BindingResult bindingResult,
						  RedirectAttributes redirAttrs) {
		itemValidator.validate(newItem, bindingResult);
		Person besitzer = personService.get(p);
		if (bindingResult.hasErrors()) {
			model.addAttribute("abholorte", personService.get(p).getAbholorte());
			model.addAttribute("newitem", newItem);
			model.addAttribute("beschreibungErrors", bindingResult.getFieldError("beschreibung"));
			model.addAttribute("titelErrors", bindingResult.getFieldError("titel"));
			model.addAttribute("kautionswertErrors", bindingResult.getFieldError("kautionswert"));
			model.addAttribute("availableFromErrors", bindingResult.getFieldError("availableFrom"));
			model.addAttribute("abholortErrors", bindingResult.getFieldError("abholort"));

			if (besitzer.getAbholorte().isEmpty()) {
				model.addAttribute("message", "Bitte Abholorte hinzufügen");
				return "errorMessage";
			}
			model.addAttribute("user", besitzer);
			model.addAttribute("abholorte", besitzer.getAbholorte());
			return "neuerArtikel";
		}
		try {
			newItem.setPicture(picture.getBytes());
		} catch (IOException e) {
			model.addAttribute("message", "Datei konnte nicht gespeichert werden");
			return "errorMessage";
		}
		itemService.save(newItem);
		besitzer.addItem(newItem);
		personService.save(besitzer);
		redirAttrs.addFlashAttribute("message", "Artikel erfolgreich hinzugefügt!");
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
	public String saveNewLocation(@ModelAttribute Abholort abholort,
								  Principal p,
								  BindingResult bindingResult,
								  Model model,
								  RedirectAttributes redirAttrs) {
		abholortValidator.validate(abholort, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("abholort", abholort);
			model.addAttribute("longitudeErrors", bindingResult.getFieldError("longitude"));
			model.addAttribute("latitudeErrors", bindingResult.getFieldError("latitude"));
			model.addAttribute("beschreibungErrors", bindingResult.getFieldError("beschreibung"));
			return "neuerAbholort";
		}
		Person aktuellerNutzer = personService.get(p);
		abholortService.save(abholort);
		aktuellerNutzer.getAbholorte().add(abholort);
		personService.save(aktuellerNutzer);
		redirAttrs.addFlashAttribute("message", "Abholort erfolgreich hinzugefügt!");
		return "redirect:/";
	}
}
