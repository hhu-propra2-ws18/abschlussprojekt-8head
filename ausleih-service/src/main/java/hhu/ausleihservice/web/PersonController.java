package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class PersonController {
	private PersonService personService;

	PersonController(PersonService perService) {
		this.personService = perService;
	}

	@GetMapping("/profil/{id}")
	public String otherUser(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.findById(id));
		model.addAttribute("user", personService.get(p));
		return "profil";
	}

	@PostMapping("/profil/{id}")
	public String editUser(Model model,
						   @PathVariable Long id,
						   Principal p,
						   @RequestParam(name = "editPerson", defaultValue = "false") final boolean changePerson,
						   @ModelAttribute("person") Person person
	) {
		System.out.println("Post triggered at /profil/" + id);
		if (changePerson) {
			System.out.println("Now updating..");
			personService.updateById(id, person);
		}
		model.addAttribute("person", personService.findById(id));
		model.addAttribute("user", personService.get(p));
		return "profil";

	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		return otherUser(model, personService.get(p).getId(), p);
	}

	@PostMapping("/profil")
	public String editUser(Model model,
						   Principal p,
						   @RequestParam(name = "editPerson", defaultValue = "false") final boolean changePerson
	) {
		return editUser(model, personService.get(p).getId(), p, changePerson, personService.get(p));
	}

	@GetMapping("/benutzersuche")
	public String benutzerSuche(Model model, Principal p) {
		model.addAttribute(personService.get(p));
		return "benutzerSuche";
	}

	@PostMapping("/benutzersuche")
	public String benutzerSuche(Model model,
								String query, //For nachname, vorname, username
								Principal p
	) {
		List<Person> list = personService.searchByNames(query);
		model.addAttribute("benutzerListe", list);
		model.addAttribute("user", personService.get(p));
		return "benutzerListe";
	}

	@GetMapping("/register")
	public String register(Model model) {
		Person userForm = new Person();
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
		personService.save(userForm);
		return "startseite";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

}
