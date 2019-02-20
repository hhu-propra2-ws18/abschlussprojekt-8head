package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.PersonNichtVorhanden;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
			System.out.println(u.getRole().name());
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

			grantedAuthorities.add(new SimpleGrantedAuthority("GUEST"));
			if (u.isAdmin()) {
				grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
			}

			return new org.springframework.security.core.userdetails.User(
					u.getUsername(),
					u.getPassword(),
					grantedAuthorities);
		}
		throw new UsernameNotFoundException("Invalid Username");
	}

	public Optional<Person> findByUsername(String username) {
		return users.findByUsername(username);
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

	Person findById(Long id) {
		Optional<Person> person = users.findById(id);
		if (!person.isPresent()) {
			throw new PersonNichtVorhanden();
		}
		return person.get();
	}

	void save(Person person) {
		users.save(person);
	}
	
	void update(Person updatedPerson, Principal principal) {
		Person altePerson = this.get(principal);
		updatedPerson.setId(altePerson.getId());
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		if (updatedPerson.getPassword() != null && !updatedPerson.getPassword().isEmpty()) {
			updatedPerson.setPassword(encoder.encode(updatedPerson.getPassword()));
		} else {
			updatedPerson.setPassword(altePerson.getPassword());
		}

		updatedPerson.setRole(altePerson.getRole());
		updatedPerson.setUsername(altePerson.getUsername());
		this.save(updatedPerson);

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
