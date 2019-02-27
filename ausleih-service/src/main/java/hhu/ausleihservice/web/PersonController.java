package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.*;
import hhu.ausleihservice.validators.AusleiheValidator;
import hhu.ausleihservice.validators.PersonValidator;
import hhu.ausleihservice.web.service.AusleiheService;
import hhu.ausleihservice.web.service.PersonService;
import hhu.ausleihservice.web.service.ProPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.soap.Addressing;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PersonController {
	private PersonService personService;
	private PersonValidator personValidator;
	private ProPayService proPayService;
	private AusleiheService ausleiheService;
	private final AusleiheValidator ausleiheValidator;

	@Autowired
	PersonController(PersonService personService, PersonValidator personValidator,
					 ProPayService proPayService, AusleiheService ausleiheService, AusleiheValidator ausleiheValidator) {
		this.personService = personService;
		this.personValidator = personValidator;
		this.proPayService = proPayService;
		this.ausleiheService = ausleiheService;
		this.ausleiheValidator = ausleiheValidator;
	}

	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		return "startseite";
	}

	@GetMapping("/profil/{id}")
	public String otherUser(Model model, @PathVariable Long id, Principal p) {
		Person benutzer = personService.findById(id);
		List<Ausleihe> verleihen = ausleiheService.findAllByAusleiherId(id);
		List<AusleihItem> ausleihenItems = new ArrayList<>();
		for(Item item :benutzer.getItems()){
			if(item instanceof AusleihItem){
				ausleihenItems.add((AusleihItem)item);
			}
		}
		model.addAttribute("benutzer", benutzer);
		model.addAttribute("moneten", proPayService.getProPayKontostand(benutzer));
		model.addAttribute("user", personService.get(p));
		model.addAttribute("ausleihen", ausleihenItems);
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		return otherUser(model, personService.get(p).getId(), p);
	}

	@PostMapping("/profil/{id}")
	public String editUser(Model model,
						   @PathVariable Long id,
						   Principal p,
						   @RequestParam(name = "editPerson", defaultValue = "false") final boolean changePerson,
						   @ModelAttribute("benutzer") Person benutzer,
						   BindingResult bindingResult
	) {
		System.out.println("Post triggered at /profil/" + id);
		if (benutzer.getUsername().equals("")) {
			System.out.println("Da Username leer, setze auf alten, damit Validierung funktioniert.\n" +
					"Alter Username: " + personService.findById(id).getUsername());
			benutzer.setUsername(personService.findById(id).getUsername());
		}
		personValidator.validate(benutzer, bindingResult);

		if (changePerson) {
			if (bindingResult.hasErrors()) {
				model.addAttribute("benutzer", benutzer);
				model.addAttribute("usernameErrors", bindingResult.getFieldError("username"));
				model.addAttribute("vornameErrors", bindingResult.getFieldError("vorname"));
				model.addAttribute("nachnameErrors", bindingResult.getFieldError("nachname"));
				model.addAttribute("passwordErrors", bindingResult.getFieldError("password"));
				model.addAttribute("emailErrors", bindingResult.getFieldError("email"));
				model.addAttribute("benutzer", personService.findById(id));
				model.addAttribute("user", personService.get(p));
				return "profil";
			}
			System.out.println("Now updating..");
			personService.updateById(id, benutzer);
			model.addAttribute("benutzer", personService.findById(id));
			model.addAttribute("moneten", proPayService.getProPayKontostand(personService.findById(id)));
			model.addAttribute("user", personService.get(p));
			return "profil";
		}
		return "redirect:/profil/" + id;
	}

	@PostMapping("/profil")
	public String editUser(Model model,
						   Principal p,
						   @RequestParam(name = "editPerson", defaultValue = "false") final boolean changePerson,
						   BindingResult bindingResult
	) {
		return editUser(model, personService.get(p).getId(), p, changePerson, personService.get(p), bindingResult);
	}

	@PostMapping("/profiladdmoney/{id}")
	public String chargeProPayById
			(Model model, @RequestParam("moneten") double moneten, @PathVariable Long id, Principal p) {
		if (personService.get(p).isHimself(personService.findById(id)) && moneten > 0) {
			proPayService.addFunds(personService.findById(id), moneten);
			return "redirect:/profil/" + id;
		} else {
			model.addAttribute("message", "Du bist die falsche Person");
			return "errorMessage";
		}
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

	@GetMapping("/register")
	public String register(Model model, Principal p) {
		if (personService.get(p) != null) {
			return "redirect:/";
		}
		Person userForm = new Person();
		model.addAttribute("usernameTaken", false);
		model.addAttribute("userForm", userForm);
		return "register";
	}

	@PostMapping("/register")
	public String added(Model model, Person userForm, BindingResult bindingResult) {
		personValidator.validate(userForm, bindingResult);
		if (bindingResult.hasErrors()) {
			model.addAttribute("userForm", userForm);
			model.addAttribute("usernameErrors", bindingResult.getFieldError("username"));
			model.addAttribute("vornameErrors", bindingResult.getFieldError("vorname"));
			model.addAttribute("nachnameErrors", bindingResult.getFieldError("nachname"));
			model.addAttribute("passwordErrors", bindingResult.getFieldError("password"));
			model.addAttribute("emailErrors", bindingResult.getFieldError("email"));
			return "register";
		}
		personService.encrypteAndSave(userForm);
		return startseite(model, null);
	}

	@GetMapping("/admin")
	public String admin(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		return "admin";
	}

	@GetMapping("/admin/allconflicts")
	public String showAllconflicts(Model model, Principal p) {
		model.addAttribute("user", personService.get(p));
		List<Ausleihe> konflikte = ausleiheService.findAllConflicts();
		model.addAttribute("konflikte", konflikte);
		return "alleKonflikte";
	}


	@GetMapping("/admin/conflict/{id}")
	public String showConflict(Model model, Principal p, @PathVariable Long id) {
		model.addAttribute("user", personService.get(p));
		Ausleihe konflikt = ausleiheService.findById(id);
		model.addAttribute("konflikt", konflikt);
		return "konflikt";
	}


	@PostMapping("/admin/conflict/{id}")
	public String resolveConflict
			(Principal p, @PathVariable Long id, @RequestParam("entscheidung") String entscheidung) {
		Ausleihe konflikt = ausleiheService.findById(id);
		if (entscheidung.equals("bestrafen")) {
			proPayService.punishRerservation(konflikt);
		} else {
			proPayService.releaseReservation(konflikt);
		}
		konflikt.setKonflikt(false);
		ausleiheService.save(konflikt);
		return "redirect:/admin/allconflicts/";
	}

	@PostMapping("/ausleihe/bestaetigen/{id}")
	public String ausleiheBestaetigen(@PathVariable Long id, Principal principal){
		Ausleihe ausleihe = ausleiheService.findById(id);
		Person person = personService.get(principal);
		ausleihe.setStatus(Status.BESTAETIGT);
		personService.save(person);
		proPayService.kautionReservieren(ausleihe);
		return "redirect:/profil/"+person.getId();
	}

	@PostMapping("/ausleihe/ablehnen/{id}")
	public String ausleiheAblehnen(@PathVariable Long id, Principal principal){
		Ausleihe ausleihe = ausleiheService.findById(id);
		Person person = personService.get(principal);
		ausleihe.setStatus(Status.ABGELEHNT);
		personService.save(person);
		return "redirect:/profil/"+person.getId();
	}
}
