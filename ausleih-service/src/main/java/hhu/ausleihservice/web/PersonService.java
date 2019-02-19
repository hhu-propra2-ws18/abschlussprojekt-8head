package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Person;
import hhu.ausleihservice.web.responsestatus.PersonNichtVorhanden;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

	List<Person> findAll() {
		return users.findAll();
	}

	Person getByUsername(String username) {
		Optional<Person> person = users.findByUsername(username);
		if (!person.isPresent()) {
			return null;
		}
		return person.get();
	}
}
