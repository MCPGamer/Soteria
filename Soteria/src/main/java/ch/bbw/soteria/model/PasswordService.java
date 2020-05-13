/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bbw.soteria.model;

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

	public void persistPassword(Password password) {
		repository.save(password);
	}

	public Password getPassword(long id) {
		return repository.findById(id).get();
	}

	public void deletePassword(long id) {
		repository.deleteById(id);;
	}
}
