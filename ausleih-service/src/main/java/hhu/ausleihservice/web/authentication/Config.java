package hhu.ausleihservice.web.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class Config extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//super.configure(http);
		http.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/admin").hasRole("ADMIN")

				.anyRequest().authenticated()
				.and().formLogin().permitAll()
				.and().logout().permitAll();
	}
}
