package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.databasemodel.Rolle;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class AusleihServiceController {

	private PersonRepository personRepository;
	private ItemRepository itemRepository;

	public AusleihServiceController(PersonRepository perRepository, ItemRepository iRepository){
		this.personRepository = perRepository;
		this.itemRepository = iRepository;
	}

	private final static DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_DATE;


	@GetMapping("/liste")
	public String artikelListe(Model model) {

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", itemRepository.findAll());

		return "artikelListe";
	}

	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id) {

		Optional<Item> artikel = itemRepository.findById(id);
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
	public String startseite() {
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
		personRepository.save(person);
		System.out.println(personRepository.findAll().get(0));
		return "redirect:http://localhost:8080/";
	}

	@GetMapping("/admin")
	public String admin(Model model){
		return "admin";
	}


	@ModelAttribute(value = "person")
	public Person newPerson(){
		return new Person();
	}

}
