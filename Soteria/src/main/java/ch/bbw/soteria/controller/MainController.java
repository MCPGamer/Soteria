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
		return "redirect:/overview";
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
			return "redirect:/overview";
		} else {
			userService.setLoggedInUser(null);

			// Show "Incorrect Credentials" in index
			model.addAttribute("credentialsWrong", true);
			model.addAttribute("user", user);
			return "index.html";
		}
	}

	@GetMapping("overview")
	private String openOverview(Model model) {
		LoginContext context = userService.getLoggedInUser();

		if (context == null) {
			return "redirect:/index";
		}

		// Hash pwd for Checking Credentials
		String pwd = Hashing.hashString(context.getPassword());

		// Check Credentials
		User loggedInUser = null;
		for (User checkUser : userService.getUserList()) {
			if (checkUser.getUsername().equals(context.getUsername()) && checkUser.getPassword().equals(pwd)) {
				loggedInUser = checkUser;
			}
		}

		model.addAttribute("user", userService.getUser(loggedInUser.getId()));
		return "overview.html";
	}

	@GetMapping("addPassword")
	private String addPassword(Model model) {
		PasswordContext context = new PasswordContext();

		LoginContext login = userService.getLoggedInUser();
		String loggedInPwd = Hashing.hashString(login.getPassword());
		for (User user : userService.getUserList()) {
			if (user.getUsername().equals(login.getUsername()) && user.getPassword().equals(loggedInPwd)) {
				context.setUserId(user.getId());
			}
		}

		model.addAttribute("pwd", context);
		model.addAttribute("action", "new");
		return "password.html";
	}

	@GetMapping("viewPassword")
	private String viewPassword(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);

		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));

		model.addAttribute("pwd", context);
		model.addAttribute("action", "view");
		return "password.html";
	}

	@GetMapping("editPassword")
	private String editPassword(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);

		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));

		model.addAttribute("pwd", context);
		model.addAttribute("action", "edit");
		return "password.html";
	}

	@GetMapping("deletePassword")
	private String deletePassword(@RequestParam int id, Model model) {
		PasswordContext context = new PasswordContext();
		Password pwd = passwordService.getPassword(id);

		context.setId(pwd.getId());
		context.setUserId(pwd.getUser().getId());
		context.setDomain(pwd.getDomain());
		context.setUsername(pwd.getUsername());
		context.setPassword(aesService.decrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));

		model.addAttribute("pwd", context);
		model.addAttribute("action", "delete");
		return "password.html";
	}

	@PostMapping("addPassword")
	private String processAddPassword(@ModelAttribute PasswordContext pwd, Model model) {
		Password password = new Password();

		User user = userService.getUser(pwd.getUserId());

		password.setUser(user);
		password.setDomain(pwd.getDomain());
		password.setUsername(pwd.getUsername());
		password.setPassword(aesService.encrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		passwordService.persistPassword(password);
		user.getPasswords().add(password);

		return "redirect:/overview";
	}

	@PostMapping("viewPassword")
	private String processViewPassword(@ModelAttribute PasswordContext pwd, Model model) {
		return "redirect:/overview";
	}

	@PostMapping("editPassword")
	private String editPassword(@ModelAttribute PasswordContext pwd, Model model) {
		Password password = passwordService.getPassword(pwd.getId());

		password.setDomain(pwd.getDomain());
		password.setUsername(pwd.getUsername());
		password.setPassword(aesService.encrypt(pwd.getPassword(), userService.getLoggedInUser().getPassword()));
		passwordService.persistPassword(password);

		return "redirect:/overview";
	}

	@PostMapping("deletePassword")
	private String deletePassword(@ModelAttribute PasswordContext pwd, Model model) {
		passwordService.deletePassword(pwd.getId());

		return "redirect:/overview";
	}
}
