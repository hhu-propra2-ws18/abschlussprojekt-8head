package hhu.ausleihservice.web.authentication;

import hhu.ausleihservice.databasemodel.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {
	@Autowired
	private PersonProvider users;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Person> user = users.findByUsername(username);
		if (user.isPresent()) {
			Person u = user.get();
			UserDetails userdetails = User.builder()
					.username(u.getUsername())
					.password(u.getPassword())
					.authorities(u.getRolle().toString())
					.build();
			return userdetails;
		}
		throw new UsernameNotFoundException("Invalid Username");
	}

}
