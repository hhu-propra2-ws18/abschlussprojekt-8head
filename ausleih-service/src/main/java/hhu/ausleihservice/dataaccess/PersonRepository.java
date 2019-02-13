package hhu.ausleihservice.dataaccess;

import hhu.ausleihservice.databasemodel.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {
	List<Person> findAll();
}
