package hhu.ausleihservice.web.authentication;

import java.util.Optional;

import hhu.ausleihservice.databasemodel.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonProvider extends JpaRepository<Person, Long> {

	Optional<Person> findByUsername(String username);

}
