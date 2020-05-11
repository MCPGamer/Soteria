/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbw.soteria.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

/**
 * @author Duarte Goncalves Mendes & Joel Weiss
 * @version 1.0
 */
@Service
@SessionScope
public class UserService {
	@Autowired
	private UserRepository repository;
	private ArrayList<User> userList;

	private LoginContext loggedInUser;

	public LoginContext getLoggedInUser() {
		return loggedInUser;
	}

	public void setLoggedInUser(LoginContext loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public UserService() {
		this.userList = new ArrayList<>();
	}

	public void addUser(User user) {
		repository.save(user);
		this.userList.add(user);
	}

	public ArrayList<User> getUserList() {
		return userList;
	}

	public void fillUserFromDB() {
		userList = (ArrayList<User>) repository.findAll();
	}

	public User getUser(long id) {
		return repository.findById(id).get();
	}
}
