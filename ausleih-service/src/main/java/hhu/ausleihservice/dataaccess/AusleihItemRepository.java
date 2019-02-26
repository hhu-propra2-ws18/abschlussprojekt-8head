package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.AusleihItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface AusleihItemRepository extends ItemBaseRepository<AusleihItem> {
	List<AusleihItem> findAll();

	Optional<AusleihItem> findById(long id);
}
