package co.in.springsecwithhib.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.in.springsecwithhib.dto.RoleDto;
import co.in.springsecwithhib.dto.UserDto;
import co.in.springsecwithhib.model.Authority;
import co.in.springsecwithhib.model.Role;
import co.in.springsecwithhib.model.User;
import co.in.springsecwithhib.service.AuthorityService;
import co.in.springsecwithhib.service.RoleService;
import co.in.springsecwithhib.service.SecurityService;
import co.in.springsecwithhib.service.UserService;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class HomeController {

	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private SecurityService securityService;

	@RequestMapping(value = "/")
	public ModelAndView test(final HttpServletResponse response) {

		return new ModelAndView("home");
	}

	@RequestMapping(value = "/home")
	public ModelAndView testHome(final HttpServletResponse response) {

		return new ModelAndView("welcome");
	}

	@GetMapping("/login")
	public ModelAndView loadLogin() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/");
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			modelAndView.setViewName("login");
			modelAndView.addObject("user", new User());
		}
		return modelAndView;
	}

	// ---Authority start-------
	@GetMapping("/home/addauthority")
	public ModelAndView addAuthority() {

		return new ModelAndView("addauthority", "authority", new Authority());
	}

	@GetMapping("/home/viewauthority")
	public ModelAndView listAuthority() {

		return new ModelAndView("listauthority", "authorities", authorityService.getAllAuthority());
	}

	@PostMapping("/home/addauth")
	public ModelAndView addAuth(@ModelAttribute final Authority authority) {

		final ModelAndView modelAndView = new ModelAndView();
		authorityService.addAuthority(authority);
		modelAndView.setViewName("redirect:/home");

		return modelAndView;
	}

	// -------Authority end-----------------------------------------------

	// -------------Role Start-----------------------------------------
	@GetMapping("/home/viewroles")
	public ModelAndView viewRoles() {

		return new ModelAndView("listroles", "roles", roleService.getAllRole());
	}

	@GetMapping("/home/addrole")
	public ModelAndView addRole() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("authorities", authorityService.getAllAuthority());
		modelAndView.addObject("role", new RoleDto());
		modelAndView.setViewName("addrole");
		return modelAndView;
	}

	@PostMapping("/home/saverole")
	public ModelAndView saveRole(@ModelAttribute final RoleDto roleDto) {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/home");
		System.err.println(roleDto.getAuthlist());
		roleDto.getAuthlist().forEach(authid -> {

			roleDto.getAuthorities().add(authorityService.getSingleAuthority(Integer.parseInt(authid)));
		});
		final Role role = new Role();
		BeanUtils.copyProperties(roleDto, role, "authlist");
		roleService.addRole(role);
		return modelAndView;
	}

	// ------------------Role end-------------------------------------------

	@GetMapping("/register")
	public ModelAndView loadRegistration() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new UserDto());
		modelAndView.setViewName("register");

		return modelAndView;
	}

	@PostMapping("/adduser")
	public ModelAndView addUser(@ModelAttribute final UserDto userDto) {

		final ModelAndView modelAndView = new ModelAndView();
		final User user = new User();
		userDto.getRoles().add(roleService.getRoleByRoleName("ROLE_USER"));
		BeanUtils.copyProperties(userDto, user, "confirmPassword");
		user.setActive(true);
		userService.addUser(user);
		modelAndView.setViewName("redirect:/home");
		return modelAndView;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/home/listusers")
	public ModelAndView listUsers() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("users", userService.getAllUser());
		modelAndView.setViewName("listusers");
		return modelAndView;
	}

	@GetMapping("/addTest")
	public void addTestUser() {

		try {
			final Authority authority = new Authority();
			authority.setName("ADD");
			authority.setDescription("Auth to add object");
			authorityService.addAuthority(authority);

			final Authority authority2 = new Authority();
			authority2.setName("DELETE");
			authority2.setDescription("Auth to delete object");
			authorityService.addAuthority(authority2);

			final Authority authority3 = new Authority();
			authority3.setName("UPDATE");
			authority3.setDescription("Auth to update object");
			authorityService.addAuthority(authority3);

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
			roleService.addRole(role);

			final Role role2 = new Role();
			role2.setName("ROLE_ADMIN");
			role2.setDescription("Role that has every access ");
			role2.setAuthorities(authorities);
			roleService.addRole(role2);

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
			userService.addUser(user);

			final User user2 = new User();
			user2.setEmail("admin@gmail.com");
			user2.setPassword("1234");
			user2.setActive(Boolean.TRUE);
			user2.setRoles(roles2);
			userService.addUser(user2);
		} catch (final Exception e) {

		}
	}
}
