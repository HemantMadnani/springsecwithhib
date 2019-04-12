package co.in.springsecwithhib.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.in.springsecwithhib.dto.UserDto;
import co.in.springsecwithhib.model.User;

@RestController
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class HomeController extends BaseController {

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

	@GetMapping("/register")
	public ModelAndView loadRegistration() {

		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user", new UserDto());
		modelAndView.setViewName("register");

		return modelAndView;
	}

}
