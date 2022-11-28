package service;

import model.*;

public interface Auth {
	public User Validate(String email, String password);
}
