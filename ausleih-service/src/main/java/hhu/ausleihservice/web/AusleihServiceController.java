package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Controller
public class AusleihServiceController {

	private static final DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_DATE;
	@Autowired
	private ItemRepository itemRepository;

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

		if (artikel.isPresent()) {
			model.addAttribute("artikel", artikel.get());
			return "artikelDetails";
		} else {
			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
	}

	@GetMapping("/")
	public String startseite(Model model) {
		return "startseite";
	}


}
