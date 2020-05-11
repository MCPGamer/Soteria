package ch.bbw.soteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ch.bbw.soteria.model.User;
import ch.bbw.soteria.model.UserService;

/**
 * @author Duarte Goncalves Mendes
 * @version 1.0
 */
@Controller
public class MainMenuController {
	@Autowired
	private UserService userService;

	@GetMapping("index")
	private String getMainMenu(Model model) {
		userService.fillUserFromDB();
		return "index.html";
	}

	@GetMapping("/")
	private String getMainMenuRoot(Model model) {
		userService.fillUserFromDB();
		model.addAttribute("user", new User());
		return "index.html";
	}

	@PostMapping("register")
	private String register(@ModelAttribute User user, Model model) {
//		userService.set
		return "index.html";
	}

	@PostMapping("login")
	private String login(@ModelAttribute User user, Model model) {
		return "index.html";
	}
}
