package hhu.ausleihservice.web;

import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Rolle;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class AusleihServiceController {

	private PersonService personService;
	private ItemService itemService;

	public AusleihServiceController(PersonService perService, ItemService iService){
		this.personService = perService;
		this.itemService = iService;
	}

	private final static DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_DATE;


	@GetMapping("/liste")
	public String artikelListe(Model model) {

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", itemService.findAll());

		return "artikelListe";
	}

	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id) {

		Optional<Item> artikel = itemService.findByID(id);
		model.addAttribute("dateformat", DATEFORMAT);

		if(artikel.isPresent()) {
			model.addAttribute("artikel", artikel.get());
			return "artikelDetails";
		} else {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
	}

	@GetMapping("/")
	public String startseite(Model model, Principal p) {
		//boolean a = false;
		Person person = personService.get(p);
		model.addAttribute("person", person);

		return "startseite";
	}

	@GetMapping("/register")
	public String register(Model model){
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
	public String admin(Model model){
		return "admin";
	}

	@GetMapping("/profil")
	public String user(Model model, Principal p) {
		model.addAttribute("username", p.getName());
		return "profil";
	}

	@ModelAttribute(value = "person")
	public Person newPerson(){
		return new Person();
	}

}
