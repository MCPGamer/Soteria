package ch.bbw.soteria.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.bbw.soteria.model.AES;
import ch.bbw.soteria.model.Hashing;
import ch.bbw.soteria.model.LoginContext;
import ch.bbw.soteria.model.Password;
import ch.bbw.soteria.model.PasswordContext;
import ch.bbw.soteria.model.PasswordService;
import ch.bbw.soteria.model.User;
import ch.bbw.soteria.model.UserService;

/**
 * @author Duarte Goncalves Mendes & Joel Weiss
 * @version 1.0
 */
@Controller
public class MainController {
	@Autowired
	private UserService userService;
	@Autowired
	private PasswordService passwordService;
	@Autowired
	private AES aesService;
	
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
		return "overview.html";
	}

	@PostMapping("login")
	private String login(@ModelAttribute LoginContext user, Model model) {
		// Hash pwd for Checking Credentials
		String pwd = Hashing.hashString(user.getPassword());

		// Check Credentials
		User loggedInUser = null;
		for (User checkUser : userService.getUserList()) {
			if (checkUser.getUsername().equals(user.getUsername()) && checkUser.getPassword().equals(pwd)) {
				loggedInUser = checkUser;
			}
		}

		if (loggedInUser != null) {
			// Save current credentials in Runtime for Salting new Passwords
			userService.setLoggedInUser(user);

			// Redirect to Password overview
			model.addAttribute("user", loggedInUser);
			return "overview.html";
		} else {
			userService.setLoggedInUser(null);

			// Show "Incorrect Credentials" in index
			model.addAttribute("credentialsWrong", true);
			model.addAttribute("user", user);
			return "index.html";
		}
	}
	
	@GetMapping("addPassword")
	private String addJoke(@ModelAttribute User user, Model model) {
		PasswordContext context = new PasswordContext();

		context.setUserId(user.getId());
		
		model.addAttribute("pwd", context);
		model.addAttribute("action", "new");
		return "overview.html";
	}

	@GetMapping("viewPassword")
	private String viewJoke(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);
		
		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		
		model.addAttribute("pwd", context);
		model.addAttribute("action", "view");
		return "overview.html";
	}
	
	@GetMapping("editPassword")
	private String editJoke(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);

		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		
		model.addAttribute("pwd", context);
		model.addAttribute("action", "edit");
		return "overview.html";
	}
	
	@GetMapping("deletePassword")
	private String deleteJoke(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);

		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		
		model.addAttribute("pwd", context);
		model.addAttribute("action", "delete");
		return "overview.html";
	}
	
	@PostMapping("addPassword")
	private String processAddJoke(@ModelAttribute PasswordContext pwd, Model model) {
		Password password = new Password();
		
		password.setUser(userService.getUser(pwd.getUserId()));
		password.setDomain(pwd.getDomain());
		password.setUsername(pwd.getUsername());
		password.setPassword(aesService.encrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		passwordService.persistPassword(password);
		
		return login(userService.getLoggedInUser(), model);
	}

	@PostMapping("editPassword")
	private String editJoke(@ModelAttribute PasswordContext pwd, Model model) {
		Password password = passwordService.getPassword(pwd.getId());

		password.setDomain(pwd.getDomain());
		password.setUsername(pwd.getUsername());
		password.setPassword(aesService.encrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		passwordService.persistPassword(password);

		return login(userService.getLoggedInUser(), model);
	}
	
	@PostMapping("deletePassword")
	private String deleteJoke(@ModelAttribute PasswordContext pwd, Model model) {
		passwordService.deletePassword(passwordService.getPassword(pwd.getId()));
		
		return login(userService.getLoggedInUser(), model);
	}
}
