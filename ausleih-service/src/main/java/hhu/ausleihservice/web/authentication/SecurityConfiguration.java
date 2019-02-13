package hhu.ausleihservice.web.authentication;

import hhu.ausleihservice.databasemodel.Rolle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private PersonService userDetailsService;


	@Bean
	public PasswordEncoder encoder(){
		System.out.println(new BCryptPasswordEncoder().encode("sibbi"));
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/admin").hasRole(Rolle.ADMIN.name())
				.anyRequest().authenticated()
				.and().formLogin().permitAll()
				.and().logout().permitAll();
	}
}
