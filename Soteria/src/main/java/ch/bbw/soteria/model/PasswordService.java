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
public class PasswordService {
	@Autowired
	private PasswordRepository repository;
	private ArrayList<Password> passwordList;
	public PasswordService() {
		this.passwordList = new ArrayList<>();
	}

	public void persistPassword(Password password) {
		repository.save(password);
		this.passwordList.add(password);
	}

	public Password getPassword(long id) {
		return repository.findById(id).get();
	}

	public void deletePassword(Password password) {
		this.passwordList.remove(password);
		repository.delete(password);
	}

	public ArrayList<Password> getPasswordList() {
		return passwordList;
	}

	public void fillPasswordsFromDB() {
		passwordList = (ArrayList<Password>) repository.findAll();
	}

}
