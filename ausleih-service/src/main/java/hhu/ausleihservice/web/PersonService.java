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

	void save(Person person) {
		users.save(person);
	}

	List<Person> findAll() {
		return users.findAll();
	}
}