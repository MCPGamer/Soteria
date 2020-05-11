package ch.bbw.soteria.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ch.bbw.soteria.model.Hashing;
import ch.bbw.soteria.model.LoginContext;
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
		model.addAttribute("user", new LoginContext());
		model.addAttribute("credentialsWrong", false);
		return "index.html";
	}

	@GetMapping("/")
	private String getMainMenuRoot(Model model) {
		userService.fillUserFromDB();
		model.addAttribute("user", new LoginContext());
		model.addAttribute("credentialsWrong", false);
		return "index.html";
	}

	@PostMapping("register")
	private String register(@ModelAttribute LoginContext user, Model model) {
		// Save current credentials in Runtime for Salting new Passwords
		userService.setLoggedInUser(user);

		// Hash Password for saving and Persist
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		newUser.setPassword(Hashing.hashString(user.getPassword()));
		userService.addUser(newUser);

		// Redirect to Password overview
		model.addAttribute("user", newUser);
		return "index.html";
	}

	@PostMapping("login")
	private String login(@ModelAttribute LoginContext user, Model model) {
		// Hash pwd for Checking Credentials
		String pwd = Hashing.hashString(user.getPassword());

		// Check Credentials
		User loggedInUser = null;
		for(User checkUser : userService.getUserList()) {
			if(checkUser.getUsername().equals(user.getUsername())
					&& checkUser.getPassword().equals(pwd)) {
				loggedInUser = checkUser;
			}
		}
		
		if(loggedInUser != null) {
			// Save current credentials in Runtime for Salting new Passwords
			userService.setLoggedInUser(user);

			// Redirect to Password overview
			model.addAttribute("user", loggedInUser);
			return "index.html";
		} else {
			userService.setLoggedInUser(null);

			// Show "Incorrect Credentials" in index
			model.addAttribute("credentialsWrong", true);
			model.addAttribute("user", user);
			return "index.html";
		}

	}
}
