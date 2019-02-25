package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.KaufItem;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends CrudRepository<Item, Long> {
	List<Item> findAll();
	//@Query("select u from AusleihItem")
	List<AusleihItem> findAllAusleihItem();
	List<KaufItem> findAllKaufItem();
	Optional<Item> findById(long id);
	Optional<AusleihItem> findAusleihItemById(long id);
	Optional<KaufItem> findKaufItemById(long id);
}
