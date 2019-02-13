package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AusleihServiceController {

	@Autowired
	ItemRepository itemRepository;


	@GetMapping("/liste")
	public String artikelListe(Model model) {

		model.addAttribute("artikelListe", itemRepository.findAll());

		return "artikelListe";
	}

	@GetMapping("/details")
	public String artikelDetails(Model model, @RequestParam long id) {

		try {
			//TODO fix this
			Item artikel = itemRepository.findById(id);
			model.addAttribute("artikel", artikel);
			return "artikelDetails";

		} catch (IndexOutOfBoundsException e) {

			model.addAttribute("id", id);
			return "artikelNichtGefunden";
		}
	}

	@GetMapping("/")
	public String startseite(Model model) {
		return "startseite";
	}


}
