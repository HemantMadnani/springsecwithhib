package co.in.springsecwithhib.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import co.in.springsecwithhib.dao.UserDao;
import co.in.springsecwithhib.model.User;
import co.in.springsecwithhib.model.UserSecurity;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserDao userDao;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		final User user = userDao.getUserByEmail(username);
		final List<String> authorities = new ArrayList<>();
		System.err.println(user.getEmail());
		System.err.println(user);
		user.getRoles().forEach(role -> {

			System.err.println(role.getName());
			authorities.add(role.getName());
		});
		user.getRoles().forEach(role -> {
			role.getAuthorities().forEach(auth -> {
				System.err.println(auth.getName());
				authorities.add(auth.getName());
			});
		});
		System.err.println(user.getPassword());
		if (user != null) {
			if (!user.isActive()) {
				System.err.println("locked....");
				throw new DisabledException("Your account is locked");
			} else {
				System.err.println("retunr new user...");
				return new UserSecurity(user, authorities.stream().distinct().collect(Collectors.toList()));
			}
		}

		throw new UsernameNotFoundException("Invalid email or password!!! Please try again");
	}

}
