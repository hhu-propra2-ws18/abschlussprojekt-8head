package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.ItemRepository;
import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {
	@Autowired
	private ItemRepository items;

	public Optional<Item> findByID(long id) {
		return items.findById(id);
	}
	public List<Item> findAll() {
		return items.findAll();
	}

}
