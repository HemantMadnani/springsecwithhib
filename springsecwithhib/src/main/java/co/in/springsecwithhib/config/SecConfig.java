package co.in.springsecwithhib.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import co.in.springsecwithhib.service.UserSecurityService;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
public class SecConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserSecurityService userSecurityService() {

		return new UserSecurityService();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.
	 * authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	@Transactional
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {

		final UserDetailsService userDetailsService = userSecurityService();
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
		System.err.println(auth);
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {

		final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService());
		authenticationProvider.setPasswordEncoder(getPasswordEncoder());
		return authenticationProvider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.
	 * web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http.anonymous().disable();

		http.csrf().and().authorizeRequests().antMatchers("/home", "/home/**").hasAuthority("ROLE_USER").and().formLogin().loginPage("/login")
				.loginProcessingUrl("/authLog").defaultSuccessUrl("/home").failureUrl("/login?error").usernameParameter("email").passwordParameter("password")
				.and().logout().logoutSuccessUrl("/login?logout").logoutRequestMatcher(new AntPathRequestMatcher("/logout"));

	}

	@Bean
	public BCryptPasswordEncoder getPasswordEncoder() {

		return new BCryptPasswordEncoder();
	}

}
