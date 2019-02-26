package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface ItemBaseRepository<T extends Item> extends CrudRepository<T, Long> {
	List<T> findAll();
	Optional<T> findById(long id);
}
