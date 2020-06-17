package com.sorbSoft.CabAcademie.Controllers;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
public class FacebookProfileController {
	
	@Inject
	private ConnectionRepository connectionRepository;

	@RequestMapping(value="/facebook", method= RequestMethod.GET)
	public String home(Model model) {
		Connection<Facebook> connection = connectionRepository.findPrimaryConnection(Facebook.class);
		if (connection == null) {
			return "redirect:/connect/facebook";
		}
		model.addAttribute("profile", connection.getApi().userOperations().getUserProfile());
		return "facebook/profile";
	}

}