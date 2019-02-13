package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AusleihServiceController {

	@Autowired
	private ItemRepository itemRepository;

	private final static DateTimeFormatter DATEFORMAT = DateTimeFormatter.ISO_DATE;

	//Checks if a string contains all strings in an array
	private boolean containsArray(String string, String[] array){
		for (String entry : array) {
			if(!string.contains(entry)){return false;}
		}
		return true;
	}

	@GetMapping("/liste")
	public String artikelListe(Model model, @RequestParam(required = false) String q) {

		List<Item> list;

		if(q == null || q.isEmpty()){
			list = itemRepository.findAll();
		} else {
			//Ignores case
			String[] qArray = q.toLowerCase().split(" ");
			list = itemRepository.findAll()
				.stream()
				.filter(
					item -> containsArray(
						(item.getTitel() + item.getBeschreibung()).toLowerCase(),
						qArray
					)
				)
				.collect(Collectors.toList());
		}

		model.addAttribute("dateformat", DATEFORMAT);
		model.addAttribute("artikelListe", list);

		return "artikelListe";
	}

	//TODO create artikelsuche.html, benutzersuche.html, benutzerListe.html
	@GetMapping("/artikelsuche")
	public String artikelSuche(Model model){
		return "artikelSuche";
	}


	@PostMapping("/artikelsuche")
	public String artikelSuche(Model model,
	                           long idMin,
	                           long idMax,
	                           String query, //For titel or beschreibung (clarify this on page)
                               int tagessatzMax,
                               int kautionswertMax,
                               int availableFromDay, int availableFromMonth, int availableFromYear,
                               int availableTillDay, int availableTillMonth, int availableTillYear
	                           ){
		Stream<Item> listStream = itemRepository.findAll().stream();

		listStream = listStream.filter(item -> (idMin <= item.getId() && item.getId() <= idMax) );

		if(query != null && !query.equals("")){
			//Ignores Case
			String[] qArray = query.toLowerCase().split(" ");
			listStream = listStream.filter(
					item -> containsArray(
							(item.getTitel() + item.getBeschreibung()).toLowerCase(),
							qArray));
		}


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
	public String startseite(Model model) {
		return "startseite";
	}


}
