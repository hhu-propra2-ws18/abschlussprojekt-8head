package hhu.ausleihservice.web.authentication;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/img/8head.jpg").permitAll()
				.antMatchers("/bearbeiten**").hasRole("ADMIN")
				.antMatchers("/admin**").access("hasAuthority('ADMIN')")
				.antMatchers("/style.css").permitAll()
				.anyRequest().authenticated()
				.and().formLogin().permitAll()
				.and().logout().permitAll();
	}
}
