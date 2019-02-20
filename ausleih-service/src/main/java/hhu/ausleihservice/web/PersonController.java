package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.service.PersonService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class PersonController {
	private PersonService personService;

	PersonController(PersonService perService) {
		this.personService = perService;
	}

	@GetMapping("/profil/{id}")
	public String otheruser(Model model, @PathVariable Long id, Principal p) {
		model.addAttribute("person", personService.findById(id));
		model.addAttribute("user", personService.get(p));
		return "profil";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		return otheruser(model, personService.get(p).getId(), p);
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
		List<Person> list = personService.searchByNames(query);
		model.addAttribute("benutzerListe", list);
		model.addAttribute("user", personService.get(p));
		return "benutzerListe";
	}

	@GetMapping("/editProfil")
	public String editProfilGet(Model model, Principal p) {
		model.addAttribute("person", personService.get(p));
		return "editProfil";
	}

	@PostMapping("/editProfil")
	public String editProfilPost(Model model, Principal p, Person person) {
		personService.update(person, personService.get(p));
		return "editProfil";
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
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userForm.setPassword(encoder.encode(userForm.getPassword()));
		personService.save(userForm);
		return "startseite";
	}

	@GetMapping("/admin")
	public String admin(Model model) {
		return "admin";
	}

}
