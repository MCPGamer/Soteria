/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbw.soteria.model;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Duarte Goncalves Mendes
 * @version 1.0
 */
@Service
public class UserService {
	@Autowired
	private UserRepository repository;
	private ArrayList<User> userList;

	public UserService() {
		this.userList = new ArrayList<>();
	}

	public void addMedia(User user) {
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
