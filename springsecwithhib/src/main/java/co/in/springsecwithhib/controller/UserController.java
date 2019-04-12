package co.in.springsecwithhib.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.in.springsecwithhib.dto.UserDto;
import co.in.springsecwithhib.model.Authority;
import co.in.springsecwithhib.model.Role;
import co.in.springsecwithhib.model.User;

@RestController
public class UserController extends BaseController {

	@PostMapping("/adduser")
	public ModelAndView addUser(@ModelAttribute final UserDto userDto) {

		final ModelAndView modelAndView = new ModelAndView();
		final User user = new User();
		userDto.getRoles().add(getServiceRegistry().getRoleService().getRoleByRoleName("ROLE_USER"));
		BeanUtils.copyProperties(userDto, user, "confirmPassword");
		user.setActive(true);
		getServiceRegistry().getUserService().addUser(user);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/home/listusers")
	public ModelAndView listUsers() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", getServiceRegistry().getUserService().getAllUser());
		modelAndView.setViewName("listusers");
		return modelAndView;
	}

	@GetMapping("/addTest")
	public void addTestUser() {

		try {
			final Authority authority = new Authority();
			authority.setName("ADD");
			authority.setDescription("Auth to add object");
			getServiceRegistry().getAuthorityService().addAuthority(authority);

			final Authority authority2 = new Authority();
			authority2.setName("DELETE");
			authority2.setDescription("Auth to delete object");
			getServiceRegistry().getAuthorityService().addAuthority(authority2);

			final Authority authority3 = new Authority();
			authority3.setName("UPDATE");
			authority3.setDescription("Auth to update object");
			getServiceRegistry().getAuthorityService().addAuthority(authority3);

			final Set<Authority> authorities = new HashSet<>();
			final Set<Authority> authorities2 = new HashSet<>();
			authorities2.add(authority);
			authorities.add(authority);
			authorities.add(authority2);
			authorities.add(authority3);

			final Role role = new Role();
			role.setName("ROLE_USER");
			role.setDescription("Role that every user has");
			role.setAuthorities(authorities2);
			getServiceRegistry().getRoleService().addRole(role);

			final Role role2 = new Role();
			role2.setName("ROLE_ADMIN");
			role2.setDescription("Role that has every access ");
			role2.setAuthorities(authorities);
			getServiceRegistry().getRoleService().addRole(role2);

			final Set<Role> roles = new HashSet<>();
			roles.add(role);

			final Set<Role> roles2 = new HashSet<>();
			roles2.add(role);
			roles2.add(role2);

			final User user = new User();
			user.setEmail("abc@gmail.com");
			user.setPassword("1234");
			user.setActive(Boolean.TRUE);
			user.setRoles(roles);
			getServiceRegistry().getUserService().addUser(user);

			final User user2 = new User();
			user2.setEmail("admin@gmail.com");
			user2.setPassword("1234");
			user2.setActive(Boolean.TRUE);
			user2.setRoles(roles2);
			getServiceRegistry().getUserService().addUser(user2);
		} catch (final Exception e) {

		}
	}

}
