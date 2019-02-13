package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ItemRepository extends CrudRepository<Item, Long> {
	List<Item> findAll();
}
