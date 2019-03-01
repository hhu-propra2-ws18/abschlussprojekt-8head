package hhu.ausleihservice.web.controller;

import hhu.ausleihservice.databasemodel.*;
import hhu.ausleihservice.validators.*;
import hhu.ausleihservice.web.service.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@SuppressWarnings({"WeakerAccess", "unused"})
public class MainController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
	private final PersonService personService;
	private final AusleihItemService ausleihItemService;
	private final KaufItemService kaufItemService;
	private final AbholortService abholortService;
	private final AbholortValidator abholortValidator;
	private final AusleiheService ausleiheService;


	public MainController(AusleiheService ausleiheService,
						  PersonService perService,
						  AusleihItemService ausleihItemService,
						  KaufItemService kaufItemService,
						  AbholortService abholortService,
						  ItemAvailabilityService itemAvailabilityService,
						  AusleihItemValidator ausleihItemValidator,
						  KaufItemValidator kaufItemValidator,
						  AbholortValidator abholortValidator,
						  AusleiheAnfragenValidator ausleiheAnfragenValidator,
						  KaufValidator kaufValidator,
						  ProPayService proPayService,
						  AusleiheService ausleiheService1) {
		this.personService = perService;
		this.ausleihItemService = ausleihItemService;
		this.kaufItemService = kaufItemService;
		this.abholortService = abholortService;
		this.abholortValidator = abholortValidator;
		this.ausleiheService = ausleiheService1;
	}

	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		Person user = personService.get(p);
		model.addAttribute("user", user);
		if (user != null) {
			List<AusleihItem> ausleihItems = new ArrayList<>();
			List<KaufItem> kaufItems = new ArrayList<>();

			for (Item item : user.getItems()) {
				if (item.getClass().getSimpleName().equals("AusleihItem")) {
					ausleihItems.add((AusleihItem) item);
				}

				if (item.getClass().getSimpleName().equals("KaufItem")) {
					kaufItems.add((KaufItem) item);
				}
			}
			model.addAttribute("ausleihItems", ausleihItems);
			model.addAttribute("kaufItems", kaufItems);

			model.addAttribute("lateAusleihen", ausleiheService.findLateAusleihen(user.getAusleihen()));
			System.out.println(ausleihItems);
		}
		model.addAttribute("dateformat", DATEFORMAT);
		return "startseite";
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String q, Principal p) {
		if (q != null) {
			q = q.trim();
		}

		List<AusleihItem> ausleihItems = ausleihItemService.simpleSearch(q);
		List<KaufItem> kaufItems = kaufItemService.simpleSearch(q);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("ausleihItems", ausleihItems);
		model.addAttribute("kaufItems", kaufItems);
		model.addAttribute("ausleihItems", ausleihItems);
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
							   @RequestParam(defaultValue = "2147483647") int tagessatzMax,
							   @RequestParam(defaultValue = "2147483647") int kautionswertMax,
							   @RequestParam
							   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableMin,
							   @RequestParam
							   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableMax, Principal p) {
		model.addAttribute("user", personService.get(p));

		if (query != null) {
			query = query.trim();
		}

		List<AusleihItem> list = ausleihItemService.extendedSearch(query, tagessatzMax, kautionswertMax,
				availableMin, availableMax);
		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("ausleihItems", list);
		model.addAttribute("kaufItems", new ArrayList<KaufItem>());
		return "artikelListe";
	}


	private <T, V extends Validator> BindingResult getBindingResult(T target, V validator) {
		DataBinder dataBinder = new DataBinder(target);
		dataBinder.setValidator(validator);
		dataBinder.validate();
		return dataBinder.getBindingResult();
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
		Person aktuellerNutzer = personService.get(p);
		if (bindingResult.hasErrors()) {
			model.addAttribute("user", aktuellerNutzer);
			model.addAttribute("abholort", abholort);
			model.addAttribute("longitudeErrors", bindingResult.getFieldError("longitude"));
			model.addAttribute("latitudeErrors", bindingResult.getFieldError("latitude"));
			model.addAttribute("beschreibungErrors", bindingResult.getFieldError("beschreibung"));
			return "neuerAbholort";
		}
		abholortService.save(abholort);
		aktuellerNutzer.getAbholorte().add(abholort);
		personService.save(aktuellerNutzer);
		redirAttrs.addFlashAttribute("message", "Abholort erfolgreich hinzugefügt!");
		return "redirect:/";
	}

	@GetMapping("/benutzersuche")
	public String benutzerSuche(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		return "benutzerSuche";
	}

	@PostMapping("/benutzersuche")
	public String benutzerSuche(Model model,
								String query, //For nachname, vorname, username
								Principal p
	) {
		if (query != null) {
			query = query.trim();
		}
		List<Person> list = personService.searchByNames(query);
		model.addAttribute("benutzerListe", list);
		model.addAttribute("user", personService.get(p));
		return "benutzerListe";
	}
}
