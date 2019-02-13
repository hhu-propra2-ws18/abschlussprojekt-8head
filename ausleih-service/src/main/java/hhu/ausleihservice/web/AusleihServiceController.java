package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class AusleihServiceController {

	@Autowired
	private ItemRepository itemRepository;

	private final static DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_DATE;


	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String query) {

		List<Item> list;

		if(query == null || query.isEmpty()){
			list = itemRepository.findAll();
		} else {
			//Ignores case
			String[] qArray = query.toLowerCase().split(" ");
			list = itemRepository.findAll()
				.stream()
				.filter(item -> containsArray(item.getTitel().toLowerCase(), qArray))
				.collect(Collectors.toList());
		}

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);

		return "artikelListe";
	}

	private boolean containsArray(String string, String[] array){
		for (String entry : array) {
			if(!string.contains(entry)){return false;}
		}
		return true;
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
	public String startseite(Model model) {
		return "startseite";
	}


}
