package co.in.springsecwithhib.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import co.in.springsecwithhib.model.Authority;

@RestController
public class AuthorityController extends BaseController {

	@GetMapping("/home/addauthority")
	public ModelAndView addAuthority() {

		return new ModelAndView("addauthority", "authority", new Authority());
	}

	@GetMapping("/home/viewauthority")
	public ModelAndView listAuthority() {

		return new ModelAndView("listauthority", "authorities", getServiceRegistry().getAuthorityService().getAllAuthority());
	}

	@PostMapping("/home/addauth")
	public ModelAndView addAuth(@ModelAttribute final Authority authority) {

		final ModelAndView modelAndView = new ModelAndView();
		getServiceRegistry().getAuthorityService().addAuthority(authority);
		modelAndView.setViewName("redirect:/home");

		return modelAndView;
	}
}
