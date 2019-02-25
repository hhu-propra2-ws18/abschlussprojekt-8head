package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.AusleihItem;
import hhu.ausleihservice.databasemodel.Item;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AusleihItemRepository extends CrudRepository<AusleihItem, Long> {
	List<AusleihItem> findAll();

	Optional<AusleihItem> findById(long id);
}
