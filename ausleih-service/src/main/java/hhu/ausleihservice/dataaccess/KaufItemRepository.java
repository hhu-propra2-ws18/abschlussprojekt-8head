package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.KaufItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public interface KaufItemRepository extends ItemBaseRepository<KaufItem> {
	List<KaufItem> findAll();

	Optional<KaufItem> findById(long id);
}
