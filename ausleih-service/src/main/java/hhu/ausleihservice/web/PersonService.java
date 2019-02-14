package hhu.ausleihservice.web;

import hhu.ausleihservice.dataaccess.PersonRepository;
import hhu.ausleihservice.databasemodel.Item;
import hhu.ausleihservice.databasemodel.Person;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Autowired
	private PersonRepository users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Person> user = users.findByUsername(username);
		if (user.isPresent()) {
			Person u = user.get();
			UserDetails userdetails = User.builder()
					.username(u.getUsername())
					.password(u.getPassword())
					.authorities(u.getRolle().name())
					.build();
			return userdetails;
		}
		throw new UsernameNotFoundException("Invalid Username");
	}

	public Person get(Principal p){
		if(p==null){
			return null;
		}
		return users.findByUsername(p.getName()).get();
	}

	public void save(Person person) {
		users.save(person);
	}
	public List<Person> findAll() {
		return users.findAll();
	}
}
