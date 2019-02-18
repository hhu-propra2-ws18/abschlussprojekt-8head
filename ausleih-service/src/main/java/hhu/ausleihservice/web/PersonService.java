package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.PersonNichtVorhanden;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PersonService implements UserDetailsService {

	private PersonRepository users;

	PersonService(PersonRepository userRep) {
		this.users = userRep;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Person> user = users.findByUsername(username);
		if (user.isPresent()) {
			Person u = user.get();
			return User.builder()
					.username(u.getUsername())
					.password(u.getPassword())
					.authorities(u.getRolle().name())
					.build();
		}
		throw new UsernameNotFoundException("Invalid Username");
	}

	Person get(Principal p) {
		if (p == null) {
			System.out.println("Null Principal");
			return null;
		}
		Optional<Person> person = users.findByUsername(p.getName());
		if (!person.isPresent()) {
			throw new PersonNichtVorhanden();
		}
		return person.get();
	}

	Person getById(Long id) {
		Optional<Person> person = users.findById(id);
		if (!person.isPresent()) {
			throw new PersonNichtVorhanden();
		}
		return person.get();
	}

	void save(Person person) {
		users.save(person);
	}

	List<Person> findAll() {
		return users.findAll();
	}

	//Checks if a string contains all strings in an array
	private boolean containsArray(String string, String[] array) {
		for (String entry : array) {
			if (!string.contains(entry)) {
				return false;
			}
		}
		return true;
	}

	List<Person> searchByNames(String query) {
		Stream<Person> listStream = findAll().stream();

		if (query != null && !query.equals("")) {
			//Ignores Case
			String[] qArray = query.toLowerCase().split(" ");
			listStream = listStream.filter(
					person -> containsArray(
							(person.getVorname() + " " +
									person.getNachname() + " " +
									person.getUsername())
									.toLowerCase(),
							qArray));
		}

		List<Person> list = listStream.collect(Collectors.toList());

		return list;
	}
}
